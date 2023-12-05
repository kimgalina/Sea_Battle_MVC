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

    public Model(Viewer viewer) {
        this.viewer = viewer;
        x = -1;
        y = -1;
        fieldGenerator = new FieldGenerator();
        userBoardArray = fieldGenerator.getGeneratedField(50,100);
        enemyBoardArray = fieldGenerator.getGeneratedField(650,100);

        for (int i = 0; i < userBoardArray.length; i++) {
            for (int j = 0; j < userBoardArray[i].length; j++) {
                 System.out.print(userBoardArray[i][j].getValue());
            }
             System.out.println();
        }
        lock = new Object();
        enemyBoard = new Cell(650, 100, 10 * 50, 10 * 50, 0);
        userBoard = new Cell(50, 100, 10 * 50, 10 * 50, 0);
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

    public Cell getEnemyBoard() {
        return enemyBoard;
    }

    public void doAction(int x, int y) {
        this.x = x;
        this.y = y;

        if (enemyBoard.contains(x, y)) {
            System.out.println("In Enemy board pressed mouse!!!");
            makeUserShot();
            int indexY = (y - 100) / 50;
            int indexX = (x - 650) / 50;
            if(enemyBoardArray[indexY][indexX].isVisible()) {
                enemyBoardArray[indexY][indexX].setVisible();
            }

            Ship ship = enemyBoardArray[indexY][indexX].getShip();

            if(ship != null) {
                Cell[] shipCells = ship.getCells();

                for (int i = 0; i < shipCells.length; i++) {
                    Cell cell = shipCells[i];
                    if (cell.equals(enemyBoardArray[indexY][indexX]) && cell.getValue() < 2) {
                        cell.setValue(2);
                        String imagePath = cell.getImagePath();
                        String sharpedImagePath = imagePath.substring(0,imagePath.length() - 4) + "-sharped.png";
                        cell.setImage(new ImageIcon(sharpedImagePath).getImage());

                    }
                }

                if (isShipSink(shipCells)) {
                    for (int i = 0; i < shipCells.length; i++) {
                        Cell cell = shipCells[i];
                        cell.setValue(4);

                    }
                }

            }

            viewer.update();
        }

        if(userBoard.contains(x,y)) {
            System.out.println("In User board pressed mouse!!!");
            int indexY = (y - 100) / 50;
            int indexX = (x - 50) / 50;
            if(userBoardArray[indexY][indexX].isVisible()) {
                userBoardArray[indexY][indexX].setVisible();
            }
            viewer.update();
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

    public Cell getBoardEnemyBoard() {
        return enemyBoard;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getBoardSize() {
        return 10;
    }

    public int[][] findShipCoordinates(int[][] field, int x, int y) {
        int shipStartX = x;
        int shipStartY = y;
        int shipEndX = x;
        int shipEndY = y;

        if (field[x][y] != 1) {
            return null;
        }

        while (shipStartY > 0 && field[x][shipStartY - 1] == 1) {
            shipStartY--;
        }

        while (shipEndY < field[0].length - 1 && field[x][shipEndY + 1] == 1) {
            shipEndY++;
        }

        while (shipStartX > 0 && field[shipStartX - 1][y] == 1) {
            shipStartX--;
        }

        while (shipEndX < field.length - 1 && field[shipEndX + 1][y] == 1) {
            shipEndX++;
        }

        return new int[][]{{shipStartX, shipStartY}, {shipEndX, shipEndY}};
    }

    public boolean validateBattlefield(int[][] field) {
        if (field.length != 10 || field[0].length != 10) {
            return false;
        }

        int[] shipsCount = {4, 3, 2, 1};
        boolean[][] visited = new boolean[10][10];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int cellValue = field[i][j];

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

    private int getShipSize(int[][] field, boolean[][] visited, int i, int j, boolean isHorizontal) {
        int size = 1;

        if (isHorizontal) {
            while (j + size < 10 && field[i][j + size] == 1) {
                visited[i][j + size] = true;
                size++;
            }
        } else {
            while (i + size < 10 && field[i + size][j] == 1) {
                visited[i + size][j] = true;
                size++;
            }
        }

        return size;
    }

    private boolean isValidPosition(int[][] field, int i, int j, int shipSizeHorizontal, int shipSizeVertical) {
        if (i - 1 >= 0) {
            if (j - 1 >= 0 && field[i - 1][j - 1] == 1) {
                return false;
            } else if (j + shipSizeHorizontal < 10 && field[i - 1][j + shipSizeHorizontal] == 1) {
                return false;
            }
        } else if (i + shipSizeVertical < 10) {
            if (j - 1 >= 10 && field[i + shipSizeVertical][j - 1] == 1) {
                return false;
            } else if (j + shipSizeHorizontal < 10 && field[i + shipSizeVertical][j + shipSizeHorizontal] == 1) {
                return false;
            }
        }

        return true;
    }
}
