import javax.swing.ImageIcon;

public class Model {

    private Viewer viewer;
    private FieldGenerator fieldGenerator;
    private Player user;
    private Player computer;
    private GameLogic gameLogic;
    private final Object lock;
    private int x;
    private int y;
    private Cell[][] userBoardArray;
    private Cell[][] enemyBoardArray;
    private Cell enemyBoard;
    private Cell userBoard;
    private Cell exitButton;
    private Cell restartButton;
    private Cell startButton;

    public Model(Viewer viewer) {
        this.viewer = viewer;
        x = -1;
        y = -1;
        fieldGenerator = new FieldGenerator();
        userBoardArray = fieldGenerator.getGeneratedField(50, 100);
        enemyBoardArray = fieldGenerator.getGeneratedField(650, 100);

        for (int i = 0; i < userBoardArray.length; i++) {
            for (int j = 0; j < userBoardArray[i].length; j++) {
                System.out.print(userBoardArray[i][j].getValue());
            }
            System.out.println();
        }

        lock = new Object();
        enemyBoard = new Cell(650, 100, 10 * 50, 10 * 50, 0);
        userBoard = new Cell(50, 100, 10 * 50, 10 * 50, 0);
        exitButton = new Cell(100, 620, 100, 50, 0);
        restartButton = new Cell(250, 620, 100, 50, 0);
        startButton = new Cell(400, 620, 100, 50, 0);
        startGame();
    }

    private void startGame() {
        ShotsQueue buffer = new ShotsQueue(1);
        user = new User(this, buffer);
        computer = new Computer(this, buffer);
        gameLogic = new GameLogic(buffer);

        user.start();
        computer.start();
        gameLogic.start();
    }

    public void doAction(int x, int y) {
        this.x = x;
        this.y = y;

            if(enemyBoard.contains(x, y) || userBoard.contains(x, y)) {
                updateBoard(userBoard, userBoardArray, 50, 100);
                updateBoard(enemyBoard, enemyBoardArray, 650, 100);

                viewer.update();
            }

        if (startButton.contains(x, y)) {
            System.out.println("Do something for START");
            viewer.update();
        } else if (restartButton.contains(x, y)) {
            System.out.println("Do something for RESTART");
            viewer.update();
        } else if (exitButton.contains(x, y)) {
            System.out.println("Do something for EXIT");
            viewer.update();
        }
    }

    private void updateBoard(Cell board, Cell[][] boardArray, int xOffset, int yOffset) {
        if (board.contains(x, y)) {
            System.out.println("In pressed mouse!!!");

            int indexY = (y - yOffset) / 50;
            int indexX = (x - xOffset) / 50;

            if (boardArray[indexY][indexX].isVisible()) {
                boardArray[indexY][indexX].setVisible(false);
            }

            Ship ship = boardArray[indexY][indexX].getShip();

            if (ship != null) {
                Cell[] shipCells = ship.getCells();

                for (Cell cell : shipCells) {
                    if (cell.equals(boardArray[indexY][indexX]) && cell.getValue() < 2) {
                        cell.setValue(2);
                        String imagePath = cell.getImagePath();
                        String sharpedImagePath = imagePath.substring(0, imagePath.length() - 4) + "-sharped.png";
                        cell.setImage(new ImageIcon(sharpedImagePath).getImage());
                    }
                }

                if (isShipSink(shipCells)) {
                    for (Cell cell : shipCells) {
                        cell.setValue(4);
                    }
                }
            }
        }
    }

    private boolean isShipSink(Cell[] cells) {
        for (Cell cell : cells) {
            if (cell.getValue() == 1) {
                return false;
            }
        }

        return true;
    }

    private void makeUserShot() {
        synchronized (lock) {
            lock.notify();
        }
    }

    public Object getLock() {
        return lock;
    }

    public Cell[][] getUserBoardArray() {
        return userBoardArray;
    }

    public Cell[][] getEnemyBoardArray() {
        return enemyBoardArray;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean validateBattlefield(Cell[][] field) {
        if (field.length != 10 || field[0].length != 10) {
            return false;
        }

        int[] shipsCount = {4, 3, 2, 1};
        boolean[][] visited = new boolean[10][10];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int cellValue = field[i][j].getValue();

                if (cellValue != 0 && cellValue != 1) {
                    return false;
                }

                if (cellValue == 1 && !visited[i][j]) {
                    int shipSizeHorizontal = getShipSize(field, visited, i, j, true);
                    int shipSizeVertical = getShipSize(field, visited, i, j, false);
                    int shipSize = Math.max(shipSizeHorizontal, shipSizeVertical);

                    if (shipSize > 4 || (shipSizeHorizontal != 1 && shipSizeVertical != 1)) {
                        return false;
                    }

                    if (!isValidPosition(field, i, j, shipSizeHorizontal, shipSizeVertical)) {
                        return false;
                    }

                    shipsCount[shipSize - 1]--;
                }
            }
        }

        for (int count : shipsCount) {
            if (count != 0) {
                return false;
            }
        }

        return true;
    }

    private int getShipSize(Cell[][] field, boolean[][] visited, int i, int j, boolean isHorizontal) {
        int size = 1;

        if (isHorizontal) {
            while (j + size < 10 && field[i][j + size].getValue() == 1) {
                visited[i][j + size] = true;
                size++;
            }
        } else {
            while (i + size < 10 && field[i + size][j].getValue() == 1) {
                visited[i + size][j] = true;
                size++;
            }
        }

        return size;
    }

    private boolean isValidPosition(Cell[][] field, int i, int j, int shipSizeHorizontal, int shipSizeVertical) {
        if (i - 1 >= 0) {
            if (j - 1 >= 0 && field[i - 1][j - 1].getValue() == 1) {
                return false;
            } else if (j + shipSizeHorizontal < 10 && field[i - 1][j + shipSizeHorizontal].getValue() == 1) {
                return false;
            }
        } else if (i + shipSizeVertical < 10) {
            if (j - 1 >= 10 && field[i + shipSizeVertical][j - 1].getValue() == 1) {
                return false;
            } else if (j + shipSizeHorizontal < 10 && field[i + shipSizeVertical][j + shipSizeHorizontal].getValue() == 1) {
                return false;
            }
        }
        return true;
    }
}
