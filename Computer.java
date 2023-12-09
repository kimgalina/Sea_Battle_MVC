import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

public class Computer extends Player {

    private final Object lock;
    private Model model;
    private int[][] computerPov;
    private boolean smartPlayMode;
    private final int EMPTY = 0;
    private final int HIT_SHOT = 1;
    private final int MISS_SHOT = 2;
    private final int SINK_SHIP = 3;

    public Computer(Model model, ShotsQueue shotsQueue) {
        super(shotsQueue);
        this.model = model;
        computerPov = new int[10][10];
        reset();
        lock = model.getLock();
        smartPlayMode = true;
    }

    public void doAction() {
        synchronized (lock) {
            waitForTurn();
            produceShot();
        }
    }

    public void reset() {
        for (int i = 0; i < computerPov.length; i++) {
          for (int j = 0; j < computerPov[i].length; j++) {
              computerPov[j][i] = 0;
          }
        }
        smartPlayMode = true;
    }

    private void refreshComputerPovBoard() {
        Cell[][] realUserCell = model.getUserBoardArray();
        for (int i = 0; i < computerPov.length; i++) {
          for (int j = 0; j < computerPov[i].length; j++) {
              if (realUserCell[i][j].getValue() == 4) {
                  computerPov[i][j] = 3;
              }
              if (realUserCell[i][j].getValue() == 2) {
                  computerPov[i][j] = 1;
              }
              if (realUserCell[i][j].getValue() == 3) {
                  computerPov[i][j] = 2;
              }
          }
        }
        int shipsRemains = model.getComputerShipsNumber();
        if (shipsRemains < 4 && !smartPlayMode) {
            smartPlayMode = true;
        }
    }

    private void waitForTurn() {
        try {
            lock.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void produceShot() {
        Shot shot = generateShot();
        produceShotMusic(shot);

        ShotsQueue shots = getShotsQueue();
        try {
            shots.add(shot);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void produceShotMusic(Shot shot) {
        int indexY = shot.getY();
        int indexX = shot.getX();
        System.out.println("coord of computer shot >>> " + indexX + " " + indexY);
        Cell shottedCell = model.getUserBoardArray()[indexY][indexX];
        if(shottedCell.getValue() == 0) {
            System.out.println("Звук плеска воды стреляет комп");
//            model.getShotSound().play();
            model.getWaterShotSound().play();


        } else if(shottedCell.getValue() == 1) {
            System.out.println("Звук попадания в корабль стреляет комп");
//            model.getShotSound().play();
            model.getSuccessShotSound().play();
        }
    }


    private Shot generateShot() {
        refreshComputerPovBoard();
        if (!smartPlayMode) {
            Shot specialShot = generateSpecialShot();
            if (specialShot != null) {
                System.out.println("!!!!!!!! specialShot");
                return specialShot;
            }
        }
        if (smartPlayMode) {
            Shot smartShot = generateSmartShot();
            if (smartShot != null) {
                System.out.println("!!!!!!!! smartShot");
                return smartShot;
            }
        }
        Shot randomShot = generateRandomShot();
        System.out.println("!!!!!!!! randomShot");
        return randomShot;
    }

    private Shot generateRandomShot() {
        Random random = new Random();
        while(true) {
          int y = random.nextInt(10);
          int x = random.nextInt(10);
          if (computerPov[y][x] == 0) {
              return new Shot(x, y, PlayerType.COMPUTER);
          }
        }
    }

    private Shot generateSpecialShot() {
      int[] shotCoordinates = new int[2];
      ArrayList<int[]> visibleShips = findShipsCoordinates();
      if (visibleShips != null) {
          ArrayList<int[]> findNearShots = calculatePossibleShots(visibleShips);
          if (findNearShots.size() != 0) {
              int index = decideShotIndex(findNearShots.size());
              shotCoordinates = findNearShots.get(index);
              int x = shotCoordinates[1];
              int y = shotCoordinates[0];
              return new Shot(x, y, PlayerType.COMPUTER);
          }
      }
      return null;
    }

    private Shot generateSmartShot() {
        int[] shotCoordinates = new int[2];
        ArrayList<int[]> visibleShips = findShipsCoordinates();
        if (visibleShips != null) {
            ArrayList<int[]> blackListShots = calculateUselessShots(visibleShips);
            ArrayList<int[]> findNearShots = calculatePossibleShots(visibleShips);
            if ((blackListShots.size() != 0) && (findNearShots.size() != 0)) {
                ArrayList<int[]> shotsList = getSmartShotsList(blackListShots, findNearShots);
                if (shotsList.size() != 0) {
                  int index = decideShotIndex(shotsList.size());
                  shotCoordinates = findNearShots.get(index);
                  int x = shotCoordinates[1];
                  int y = shotCoordinates[0];
                  return new Shot(x, y, PlayerType.COMPUTER);
                }
            }
        }
        return null;
    }

    private ArrayList<int[]> findShipsCoordinates() {
        ArrayList<int[]> shipsFoundCoordinates = new ArrayList<>();
        int shipsCount = 0;
        for (int i = 0; i < computerPov.length; i++) {
            for (int j = 0; j < computerPov[i].length; j++) {
                if (computerPov[j][i] == 1) {
                    int[] coordinates = {j, i, 1};
                    shipsFoundCoordinates.add(coordinates);
                    shipsCount++;
                }
            }
        }
        if (shipsCount > 0) {
            return shipsFoundCoordinates;
        }
        return null;
    }

    private ArrayList<int[]> calculatePossibleShots(ArrayList<int[]> shipsFoundCoordinates) {
        ArrayList<int[]> possibleShotsCoordinates = new ArrayList<>();
        int possibleShotsCounter = 0;
        for (int i = 0; i < shipsFoundCoordinates.size(); i++) {
            if (shipsFoundCoordinates.get(i)[2] == -1) {
                return possibleShotsCoordinates;
            }
            if (shipsFoundCoordinates.get(i)[2] != HIT_SHOT) {
                continue;
            }
            int y = shipsFoundCoordinates.get(i)[0];
            int x = shipsFoundCoordinates.get(i)[1];
            if (topIsReachable(y) && computerPov[y - 1][x] == 0) {
                possibleShotsCoordinates.add(new int[] {y - 1, x});
                possibleShotsCounter++;
            }
            if (botIsReachable(y) && computerPov[y + 1][x] == 0) {
                possibleShotsCoordinates.add(new int[] {y + 1, x});
                possibleShotsCounter++;
            }
            if (leftIsReachable(x) && computerPov[y][x - 1] == 0) {
                possibleShotsCoordinates.add(new int[] {y, x - 1});
                possibleShotsCounter++;
            }
            if (rightIsReachable(x) && computerPov[y][x + 1] == 0) {
                possibleShotsCoordinates.add(new int[] {y, x + 1});
                possibleShotsCounter++;
            }
        }
        return possibleShotsCoordinates;
    }

    private ArrayList<int[]> calculateUselessShots(ArrayList<int[]> shipsFoundCoordinates) {
        ArrayList<int[]> uselessShotsCoordinates = new ArrayList<>();
        int uselessShotsCounter = 0;
        for (int i = 0; i < shipsFoundCoordinates.size(); i++) {
            if (shipsFoundCoordinates.get(i)[2] == -1) {
                return uselessShotsCoordinates;
            }
            if (shipsFoundCoordinates.get(i)[2] != SINK_SHIP) {
                continue;
            }
            int y = shipsFoundCoordinates.get(i)[0];
            int x = shipsFoundCoordinates.get(i)[1];
            if (topIsReachable(y) && computerPov[y - 1][x] != HIT_SHOT) {
                uselessShotsCoordinates.add(new int[] {y - 1, x});
                uselessShotsCounter++;
            }
            if (botIsReachable(y) && computerPov[y + 1][x] != HIT_SHOT) {
                uselessShotsCoordinates.add(new int[] {y + 1, x});
                uselessShotsCounter++;
            }
            if (leftIsReachable(x) && computerPov[y][x - 1] != HIT_SHOT) {
                uselessShotsCoordinates.add(new int[] {y, x - 1});
                uselessShotsCounter++;
            }
            if (rightIsReachable(x) && computerPov[y][x + 1] != HIT_SHOT) {
                uselessShotsCoordinates.add(new int[] {y, x + 1});
                uselessShotsCounter++;
            }

            if (topLeftIsReachable(x, y) && computerPov[y - 1][x - 1] != HIT_SHOT) {
                uselessShotsCoordinates.add(new int[] {y - 1, x - 1});
                uselessShotsCounter++;
            }
            if (topRightIsReachable(x, y) && computerPov[y - 1][x + 1] != HIT_SHOT) {
                uselessShotsCoordinates.add(new int[] {y - 1, x + 1});
                uselessShotsCounter++;
            }
            if (botLeftIsReachable(x, y) && computerPov[y + 1][x - 1] != HIT_SHOT) {
                uselessShotsCoordinates.add(new int[] {y + 1, x - 1});
                uselessShotsCounter++;
            }
            if (topRightIsReachable(x, y) && computerPov[y + 1][x + 1] != HIT_SHOT) {
                uselessShotsCoordinates.add(new int[] {y + 1, x + 1});
                uselessShotsCounter++;
            }
        }
        return uselessShotsCoordinates;
    }

    private ArrayList<int[]> getSmartShotsList(ArrayList<int[]> blackListCoordinates, ArrayList<int[]> shotListCoordinates) {
      ArrayList<int[]> resultList = new ArrayList<>();

      for (int[] coordinates : shotListCoordinates) {
          boolean shouldAdd = true;
          for (int[] blCoordinates : blackListCoordinates) {
              if (Arrays.equals(coordinates, blCoordinates)) {
                  shouldAdd = false;
                  break;
              }
          }
          if (shouldAdd) {
              resultList.add(coordinates);
          }
        }
        return resultList;
    }

    private int decideShotIndex(int length) {
        Random random = new Random();
        int index = random.nextInt(length);
        return index;
    }

    private boolean topIsReachable(int y) {
        return (y - 1 >= 0);
    }

    private boolean botIsReachable(int y) {
        return (y + 1 <= 9);
    }

    private boolean leftIsReachable(int x) {
        return (x - 1 >= 0);
    }

    private boolean rightIsReachable(int x) {
        return (x + 1 <= 9);
    }

    private boolean topLeftIsReachable(int x, int y) {
        return topIsReachable(y) && leftIsReachable(x);
    }

    private boolean topRightIsReachable(int x, int y) {
        return topIsReachable(y) && rightIsReachable(x);
    }

    private boolean botLeftIsReachable(int x, int y) {
        return botIsReachable(y) && leftIsReachable(x);
    }

    private boolean botRightIsReachable(int x, int y) {
        return botIsReachable(y) && rightIsReachable(x);
    }
}
