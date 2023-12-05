import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.awt.Image;
import javax.imageio.ImageIO;

public class FieldGenerator {

    private Random random;

    public FieldGenerator() {
        random = new Random();
    }

    public Cell[][] getGeneratedField(int xOffset, int yOffset) {
        Cell[][] generatedField = generateField(xOffset, yOffset);
        for(int i = 0; i < generatedField.length; i++) {
            for(int j = 0; j < generatedField[i].length; j++) {
                System.out.print(generatedField[i][j].getValue() + " ");
            }
            System.out.println();
        }
        return generatedField;
    }
    private Cell[][] generateField(int xOffset, int yOffset) {
        Cell[][] matrix = new Cell[10][10];
        initializeField(matrix, xOffset, yOffset);
        for (int i = 4, k = 1; i > 0; i--, k++) {
            for (int j = k; j > 0; j--) {
                // создаем обьект корабль и каждой клетке этого корабля кидаем на него ссылку
                Ship ship = new Ship(i);
                buildShip(i, matrix, ship);
                ///////////////
                System.out.println("Новый корабль построен >>> " + ship.getHealth());
                // проверяем клетки корабля
                Cell[] shipCells = ship.getCells();
                for(int m = 0; m < shipCells.length; m++) {
                    System.out.println("x = " + shipCells[m].getX() + " y = " + shipCells[m].getY());
                }
            }
        }

        return matrix;
    }

    private void initializeField(Cell[][] matrix, int xOffset, int yOffset) {
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = new Cell(j * 50 + xOffset, i * 50 + yOffset, 50, 50, 0);
            }
        }
    }

    private int generateCoordinate() {
        return random.nextInt(10);
    }

    private int generateDirection() {
        return random.nextInt(4);
    }

    private void buildShip(int decksCount, Cell[][] matrix, Ship ship) {
        int x;
        int y;
        int direction;
        do {
            do {
                x = generateCoordinate();
                y = generateCoordinate();
            } while (matrix[x][y].getValue() != 0);// while cell is not empty
            direction = generateDirection();
        } while (!isDirectionValid(decksCount, matrix, direction, x, y));
        try {
            setShip(x, y, direction, decksCount, matrix, ship);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setShip(int x, int y, int direction, int decksCount, Cell[][] matrix, Ship ship) throws IOException {
        Cell[] decks = new Cell[decksCount];
        switch (direction) {
            case 0:// up
                for (int i = decksCount,j = 0; i > 0; i--, x--,j++) {
                    matrix[x][y].setValue(1);
                    // каждой клетке указываем к какому кораблю она принадлежит
                    matrix[x][y].setShip(ship);
                    // присваиваем массиву клеток для одного корабля клетки
                    decks[j] =  matrix[x][y];
                    // каждой клетке в зависимости от ее положения ставим картинку части корабля
                    String imagePath =  "images/" + decksCount + "ship-" + i + ".png";
                    File file = new File(imagePath);
                    matrix[x][y].setImagePath(imagePath); // сохраняем какой это путь для изменения на подбитый
                    matrix[x][y].setImage(ImageIO.read(file));
                }
                // присваиваем кораблю его клетки
                ship.setCells(decks);
                return;
            case 1:// right

                for (int i = 1, j = 0; i <= decksCount; i++, y++, j++) {
                    matrix[x][y].setValue(1);

                    // каждой клетке указываем к какому кораблю она принадлежит
                    matrix[x][y].setShip(ship);
                    // присваиваем массиву клеток для одного корабля клетки
                    decks[j] =  matrix[x][y];
                    // каждой клетке в зависимости от ее положения ставим картинку части корабля
                    String imagePath =  "images/" + decksCount + "ship-" + i + ".png";
                    File file = new File(imagePath);
                    matrix[x][y].setImagePath(imagePath); // сохраняем какой это путь для изменения на подбитый
                    matrix[x][y].setImage(ImageIO.read(file));
                }
                ship.setHorizontal(true);
                // присваиваем кораблю его клетки
                ship.setCells(decks);
                return;
            case 2:// down
                for (int i = 1, j = 0; i <= decksCount; i++, x++, j++) {
                    matrix[x][y].setValue(1);

                    // каждой клетке указываем к какому кораблю она принадлежит
                    matrix[x][y].setShip(ship);
                    // присваиваем массиву клеток для одного корабля клетки
                    decks[j] =  matrix[x][y];

                    // каждой клетке в зависимости от ее положения ставим картинку части корабля
                    String imagePath =  "images/" + decksCount + "ship-" + i + ".png";
                    File file = new File(imagePath);
                    matrix[x][y].setImagePath(imagePath); // сохраняем какой это путь для изменения на подбитый
                    matrix[x][y].setImage(ImageIO.read(file));
                }
                // присваиваем кораблю его клетки
                ship.setCells(decks);
                return;
            case 3:// left
                for (int i = decksCount, j = 0; i > 0; i--, y--, j++) {
                    matrix[x][y].setValue(1);

                    // каждой клетке указываем к какому кораблю она принадлежит
                    matrix[x][y].setShip(ship);
                    // присваиваем массиву клеток для одного корабля клетки
                    decks[j] =  matrix[x][y];

                    // каждой клетке в зависимости от ее положения ставим картинку части корабля
                    String imagePath = "images/" + decksCount + "ship-" + i + ".png";
                    File file = new File(imagePath);
                    matrix[x][y].setImagePath(imagePath); // сохраняем какой это путь для изменения на подбитый
                    matrix[x][y].setImage(ImageIO.read(file));
                }
                ship.setHorizontal(true);
                // присваиваем кораблю его клетки
                ship.setCells(decks);
                return;
        }
    }

    private boolean isDirectionValid(int decksCount, Cell[][] matrix, int direction, int x, int y) {
        switch (direction) {
            case 0:// up
                return checkUp(decksCount, x, y, matrix);
            case 1:// right
                return checkRight(decksCount, x, y, matrix);
            case 2:// down
                return checkDown(decksCount, x, y, matrix);
            case 3:// left
                return checkLeft(decksCount, x, y, matrix);
        }
        return false;
    }

    private int isValidAngle(int x, int y, Cell[][] matrix) { // return 0 if it is valid angle , 1 if it is not angle or -1 not valid angle
        if (x == 0 && y == 0) {
            if (matrix[x][y + 1].getValue() != 0 || matrix[x + 1][y + 1].getValue() != 0 || matrix[x + 1][y].getValue() != 0) {
                return -1;
            }
            return 0;
        } else if (x == 0 && y == 9) {
            if (matrix[x][y - 1].getValue() != 0 || matrix[x + 1][y - 1].getValue() != 0 || matrix[x + 1][y].getValue() != 0) {
                return -1;
            }
            return 0;
        } else if (y == 0 && x == 9) {
            if (matrix[x][y + 1].getValue() != 0 || matrix[x - 1][y + 1].getValue() != 0 || matrix[x - 1][y].getValue() != 0) {
                return -1;
            }
            return 0;
        } else if (x == 9 && y == 9) {
            if (matrix[x][y - 1].getValue() != 0 || matrix[x - 1][y - 1].getValue() != 0 || matrix[x - 1][y].getValue() != 0) {
                return -1;
            }
            return 0;
        }
        return 1;
    }

    private boolean isValidCell(int x, int y, Cell[][] matrix) {
        if (x == 0) {
            if (matrix[x][y - 1].getValue() != 0 || matrix[x][y + 1].getValue() != 0 || matrix[x + 1][y - 1].getValue() != 0 || matrix[x + 1][y + 1].getValue() != 0 || matrix[x + 1][y].getValue() != 0) {
                return false;
            }
        } else if (y == 0) {
            if (matrix[x][y + 1].getValue() != 0 || matrix[x + 1][y + 1].getValue() != 0 || matrix[x - 1][y + 1].getValue() != 0 || matrix[x + 1][y].getValue() != 0 || matrix[x - 1][y].getValue() != 0) {
                return false;
            }
        } else if (x == 9) {
            if (matrix[x][y - 1].getValue() != 0 || matrix[x][y + 1].getValue() != 0 || matrix[x - 1][y - 1].getValue() != 0 || matrix[x - 1][y + 1].getValue() != 0 || matrix[x - 1][y].getValue() != 0) {
                return false;
            }
        } else if (y == 9) {
            if (matrix[x][y - 1].getValue() != 0 || matrix[x + 1][y - 1].getValue() != 0 || matrix[x - 1][y - 1].getValue() != 0 || matrix[x + 1][y].getValue() != 0 || matrix[x - 1][y].getValue() != 0) {
                return false;
            }
        } else {
            if (matrix[x][y - 1].getValue() != 0 || matrix[x][y + 1].getValue() != 0 || matrix[x + 1][y - 1].getValue() != 0 || matrix[x + 1][y + 1].getValue() != 0 ||
                    matrix[x - 1][y - 1].getValue() != 0 || matrix[x - 1][y + 1].getValue() != 0 || matrix[x + 1][y].getValue() != 0 || matrix[x - 1][y].getValue() != 0) {

                return false;
            }
        }
        return true;
    }

    private boolean checkCell(int x, int y, Cell[][] matrix) {
        int result = isValidAngle(x, y, matrix);
        switch (result) {
            case 0:
                return true;
            case -1:
                return false;
            case 1:
                return isValidCell(x, y, matrix);
        }
        return false;
    }

    private boolean checkUp(int decksCount, int x, int y, Cell[][] matrix) {
        for (int i = 0; i < decksCount; i++, x--) {
            if (x < 0 || !checkCell(x, y, matrix)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkRight(int decksCount, int x, int y, Cell[][] matrix) {
        for (int i = 0; i < decksCount; i++, y++) {
            if (y > 9 || !checkCell(x, y, matrix)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkDown(int decksCount, int x, int y, Cell[][] matrix) {
        for (int i = 0; i < decksCount; i++, x++) {
            if (x > 9 || !checkCell(x, y, matrix)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkLeft(int decksCount, int x, int y, Cell[][] matrix) {
        for (int i = 0; i < decksCount; i++, y--) {
            if (y < 0 || !checkCell(x, y, matrix)) {
                return false;
            }
        }
        return true;
    }


}
