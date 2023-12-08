import java.util.Random;
import java.util.ArrayList;

public class Computer extends Player {

    private final Object lock;
    private Model model;
    private int[][] computerPov;

    public Computer(Model model, ShotsQueue shotsQueue) {
        super(shotsQueue);
        this.model = model;
        computerPov = new int[10][10];
        resetPov();
        lock = model.getLock();
    }

    public void doAction() {
        synchronized (lock) {
            waitForTurn();
            produceShot();
        }
    }

    public void resetPov() {
        for (int i = 0; i < computerPov.length; i++) {
          for (int j = 0; j < computerPov[i].length; j++) {
              computerPov[j][i] = 0;
          }
        }
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
        ShotsQueue shots = getShotsQueue();
        try {
            shots.add(shot);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Shot generateShot() {
        refreshComputerPovBoard();
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
        shotCoordinates = generateRandomShot();
        int x = shotCoordinates[1];
        int y = shotCoordinates[0];
        return new Shot(x, y, PlayerType.COMPUTER);
    }

    private int[] generateRandomShot() {
        int[] shotCoordinates = new int[2];
        Random random = new Random();
        while(true) {
          int y = random.nextInt(10);
          int x = random.nextInt(10);
          if (computerPov[y][x] == 0) {
              shotCoordinates[0] = y;
              shotCoordinates[1] = x;
              return shotCoordinates;
          }
        }
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
            if (shipsFoundCoordinates.get(i)[2] != 1) {
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
}
