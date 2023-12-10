import java.util.Random;
import java.util.ArrayList;

public class Computer extends Player {

    private final Object lock;
    private Model model;
    private int[][] computerPov;
    private ArrayList<int[]> blackListCoordinates;
    private boolean smartPlay;

    public Computer(Model model, ShotsQueue shotsQueue) {
        super(shotsQueue);
        this.model = model;
        computerPov = new int[10][10];
        reset();
        lock = model.getLock();
        smartPlay = false;
    }

    public void doAction() {
        synchronized (lock) {
            waitForTurn();
            if (!model.isUserTurn()) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                produceShot();
            }
        }
    }

    public void reset() {
        for (int i = 0; i < computerPov.length; i++) {
            for (int j = 0; j < computerPov[i].length; j++) {
                computerPov[j][i] = 0;
            }
        }
        smartPlay = false;
        blackListCoordinates = null;
    }

    private void refreshComputerPovBoard() {
        Cell[][] realUserCell = model.getUserBoardArray();

        for (int i = 0; i < computerPov.length; i++) {
            for (int j = 0; j < computerPov[i].length; j++) {
                if (realUserCell[i][j].getValue() == 4) {
                    computerPov[i][j] = 3;
                    if (smartPlay) {
                        addToBlackList(new int[]{i, j});
                    }
                }
                if (realUserCell[i][j].getValue() == 2) {
                    computerPov[i][j] = 1;
                }
                if (realUserCell[i][j].getValue() == 3) {
                    computerPov[i][j] = 2;
                }
            }
        }

        int ComputerShipsNumber = model.getComputerShipsNumber();
        int userShipsNumber = model.getUserShipsNumber();
        if (!smartPlay && userShipsNumber <= 9) {
            smartPlay = true;
            blackListCoordinates = new ArrayList<>();
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

        while (true) {
            int y = random.nextInt(10);
            int x = random.nextInt(10);
            if (computerPov[y][x] == 0) {
                shotCoordinates[0] = y;
                shotCoordinates[1] = x;
                if (smartPlay && checkInBlackList(new int[]{y, x})) {
                    return generateRandomShot();
                }
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
                if (!(smartPlay && checkInBlackList(new int[]{y - 1, x}))) {
                    possibleShotsCoordinates.add(new int[]{y - 1, x});
                }
            }
            if (botIsReachable(y) && computerPov[y + 1][x] == 0) {
                if (!(smartPlay && checkInBlackList(new int[]{y + 1, x}))) {
                    possibleShotsCoordinates.add(new int[]{y + 1, x});
                }
            }
            if (leftIsReachable(x) && computerPov[y][x - 1] == 0) {
                if (!(smartPlay && checkInBlackList(new int[]{y, x - 1}))) {
                    possibleShotsCoordinates.add(new int[]{y, x - 1});
                }
            }
            if (rightIsReachable(x) && computerPov[y][x + 1] == 0) {
                if (!(smartPlay && checkInBlackList(new int[]{y, x + 1}))) {
                    possibleShotsCoordinates.add(new int[]{y, x + 1});
                }
            }
        }
        return possibleShotsCoordinates;
    }

    private boolean checkInBlackList(int[] coordinates) {
        for (int i = 0; i < blackListCoordinates.size(); i++) {
            if (blackListCoordinates.get(i)[0] == coordinates[0] &&
                    blackListCoordinates.get(i)[1] == coordinates[1]) {
                return true;
            }
        }
        return false;
    }

    private void addToBlackList(int[] coordinates) {
        int x = coordinates[1];
        int y = coordinates[0];
        if (topIsReachable(y) && computerPov[y - 1][x] == 0) {
            if (!(checkInBlackList(new int[]{(y - 1), x}))) {
                blackListCoordinates.add(new int[]{(y - 1), x});
            }
        }
        if (botIsReachable(y) && computerPov[y + 1][x] == 0) {
            if (!(checkInBlackList(new int[]{(y + 1), x}))) {
                blackListCoordinates.add(new int[]{(y + 1), x});
            }
        }
        if (leftIsReachable(x) && computerPov[y][x - 1] == 0) {
            if (!(checkInBlackList(new int[]{y, (x - 1)}))) {
                blackListCoordinates.add(new int[]{y, (x - 1)});
            }
        }
        if (rightIsReachable(x) && computerPov[y][x + 1] == 0) {
            if (!(checkInBlackList(new int[]{y, (x + 1)}))) {
                blackListCoordinates.add(new int[]{y, (x + 1)});
            }
        }
        if (topLeftIsReachable(x, y) && computerPov[y - 1][x - 1] == 0) {
            if (!(checkInBlackList(new int[]{(y - 1), (x - 1)}))) {
                blackListCoordinates.add(new int[]{(y - 1), (x - 1)});
            }
        }
        if (topRightIsReachable(x, y) && computerPov[y - 1][x + 1] == 0) {
            if (!(checkInBlackList(new int[]{(y - 1), (x + 1)}))) {
                blackListCoordinates.add(new int[]{(y - 1), (x + 1)});
            }
        }
        if (botLeftIsReachable(x, y) && computerPov[y + 1][x - 1] == 0) {
            if (!(checkInBlackList(new int[]{(y + 1), (x - 1)}))) {
                blackListCoordinates.add(new int[]{(y + 1), (x - 1)});
            }
        }
        if (botRightIsReachable(x, y) && computerPov[y + 1][x + 1] == 0) {
            if (!(checkInBlackList(new int[]{(y + 1), (x + 1)}))) {
                blackListCoordinates.add(new int[]{(y + 1), (x + 1)});
            }
        }
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

    private boolean botLeftIsReachable(int x, int y) {
        return botIsReachable(y) && leftIsReachable(x);
    }

    private boolean topRightIsReachable(int x, int y) {
        return topIsReachable(y) && rightIsReachable(x);
    }

    private boolean botRightIsReachable(int x, int y) {
        return botIsReachable(y) && rightIsReachable(x);
    }
}
