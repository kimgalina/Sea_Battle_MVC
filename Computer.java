import java.util.Random;

public class Computer extends Player {

    private final Object lock;
    private Model model;

    public Computer(Model model, ShotsQueue shotsQueue) {
        super(shotsQueue);
        this.model = model;
        lock = model.getLock();
    }

    public void doAction() {
        synchronized (lock) {
            waitForTurn();
            if (!model.isUserTurn()) {
                printUserBoard();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                produceShot();
            }
        }
    }

    private void printUserBoard() {
        System.out.println("\nUSER");
        Cell[][] board = model.getUserBoardArray();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j].getValue());
            }
            System.out.println();
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
        int[] shotCoordinates = generateRandomShot();
        int y = shotCoordinates[0];
        int x = shotCoordinates[1];
        return new Shot(y, x, PlayerType.COMPUTER); //logic of computer's shot generation
    }

    private int[] generateRandomShot() {
        int[] shotCoordinates = new int[2];
        Random random = new Random();
        Cell[][] playerBoard = model.getUserBoardArray();
        while(true) {
          int y = random.nextInt(10);
          int x = random.nextInt(10);
          if (playerBoard[y][x].getValue() == 0) {
              shotCoordinates[0] = y;
              shotCoordinates[1] = x;
              return shotCoordinates;
          }
        }
    }

    private int[][] findShipsCoordinates() {
        int[][] shipsFoundCoordinates = new int[24][3];
        int shipsCount = 0;
        Cell[][] playerBoard = model.getVisualUserBoard();
        for (int i = 0; i < playerBoard.length; i++) {
            for (int j = 0; j < playerBoard[i].length; j++) {
                if (playerBoard[j][i].getValue() == 1 || playerBoard[j][i].getValue() == 3) {
                    shipsFoundCoordinates[shipsCount][0] = j;
                    shipsFoundCoordinates[shipsCount][1] = i;
                    shipsFoundCoordinates[shipsCount][2] = playerBoard[j][i].getValue();
                    shipsCount++;
                }
            }
        }
        return shipsFoundCoordinates;
    }

    private int[][] calculatePossibleShots(int[][] shipsFoundCoordinates) {
        int[][] possibleShotsCoordinates = new int[100][2];
        for (int i = 0; i < shipsFoundCoordinates.length; i++) {
            //in proccess
        }
        return null;
    }
    //0 nothing
    //1 shot
    //2 miss
    //3 destroyed
}
