import javax.swing.*;
import java.awt.Image;

public class Model {

    private Viewer viewer;
    private FieldGenerator fieldGenerator;
    private Player user;
    private Player computer;
    private GameLogic gameLogic;
    private int x;
    private int y;
    private Cell[][] boardArray;
    private Cell enemyBoard;

    public Model(Viewer viewer) {
        this.viewer = viewer;
        x = -1;
        y = -1;
        fieldGenerator = new FieldGenerator();
        boardArray = fieldGenerator.getGeneratedField();

        for (int i = 0; i < boardArray.length; i++) {
            for (int j = 0; j < boardArray[i].length; j++) {
                System.out.print(boardArray[i][j].getValue());
            }
            System.out.println();
        }

        enemyBoard = new Cell(50, 100, 10 * 50, 10 * 50, 0);
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

    public Cell[][] getBoardArray() {
        return boardArray;
    }

    public Cell getEnemyBoard() {
        return enemyBoard;
    }

    public void doAction(int x, int y) {

        System.out.println(x + "    " + y);


        if (enemyBoard.contains(x, y)) {
            System.out.println("In Enemy board pressed mouse!!!");
             makeUserShot();

            int indexY = (y - 100) / 50;
            int indexX = (x - 50) / 50;
            Ship ship = boardArray[indexY][indexX].getShip();
            Cell[] cells = ship.getCells();

            for (int i = 0; i < cells.length; i++) {
                Cell cell = cells[i];
                if (cell.equals(boardArray[indexY][indexX]) && cell.getValue() < 2) {
                    cell.setValue(2);
                    updateCellImage(i, cells.length, cell);
                }
            }

            if (isShipSink(cells)) {
                for (int i = 0; i < cells.length; i++) {
                    Cell cell = cells[i];
                    cell.setValue(4);
                    updateCellImage(i, cells.length, cell);
                }
            }

            viewer.update();
        }
    }

    private void updateCellImage(int i, int shipSize, Cell cell) {
        switch (shipSize) {
            case 1:
                if (cell.getValue() == 2) {
                    cell.setImage(new ImageIcon("images/sinkSmallShip").getImage());
                    cell.setValue(4);
                }
                break;
            case 2:
                if (cell.getValue() == 2) {
                    cell.setImage(new ImageIcon("images/paddedTwoSizeShip" + i + ".png").getImage());
                } else if (cell.getValue() == 4) {
                    cell.setImage(new ImageIcon("images/sinkTwoSizeShip" + i + ".png").getImage());
                }
                break;
            case 3:
                if (cell.getValue() == 2) {
                    cell.setImage(new ImageIcon("images/paddedThreeSizeShip" + i + ".png").getImage());
                } else if (cell.getValue() == 4) {
                    cell.setImage(new ImageIcon("images/sinkThreeSizeShip" + i + ".png").getImage());
                }
                break;
            case 4:
                if (cell.getValue() == 2) {
                    cell.setImage(new ImageIcon("images/paddedFourSizeShip" + i + ".png").getImage());
                } else if (cell.getValue() == 4) {
                    cell.setImage(new ImageIcon("images/sinkFourSizeShip" + i + ".png").getImage());
                }
                break;
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

    public void mouseEnteredInEnemyBoard(int x, int y) {
        System.out.println("Mouse entered in JFrame");
        if (enemyBoard.contains(x, y)) {
            System.out.println("Mouse entered in enemy board!!!");
        }
    }

    private void makeUserShot() {
        user.notifyTurn();
        computer.notifyTurn();
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
