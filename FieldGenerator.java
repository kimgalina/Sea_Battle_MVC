import java.util.Random;


public class FieldGenerator {
    private Random random;

    public FieldGenerator() {
        random = new Random();
    }

    public Cell[][] getGeneratedField() {
        int[][] field = generateField();
        Cell[][] generatedField = new Cell[field.length][10];
        for(int i = 0; i < field.length; i++) {
            for(int j = 0; j < field[i].length; j++) {
                if (field[i][j] == 1) {
                    Ship ship = new Ship(1);
                    generatedField[i][j] = new Cell(i * 50 + 50, j * 50 + 100, 50, 50, field[i][j], ship);
                    continue;
                }
                generatedField[i][j] = new Cell(i * 50 + 50, j * 50 + 100, 50, 50, field[i][j], null);
            }
        }
        return generatedField;
    }
    private int[][] generateField() {
        int[][] matrix = new int[10][10];
        for (int i = 4, k = 1; i > 0; i--, k++) {
            for (int j = k; j > 0; j--) {
                buildShip(i, matrix);
            }
        }

        return matrix;
    }

    private int generateCoordinate() {
        return random.nextInt(10);
    }

    private int generateDirection() {
        return random.nextInt(4);
    }

    private void buildShip(int decksCount, int[][] matrix) {
        int x;
        int y;
        int direction;
        do {
            do {
                x = generateCoordinate();
                y = generateCoordinate();
            } while (matrix[x][y] != 0);// while cell is not empty
            direction = generateDirection();
        } while (!isDirectionValid(decksCount, matrix, direction, x, y));
        setShip(x, y, direction, decksCount, matrix);

    }

    private void setShip(int x, int y, int direction, int decksCount, int[][] matrix) {
        switch (direction) {
            case 0:// up
                for (int i = 0; i < decksCount; i++, x--) {
                    matrix[x][y] = 1;
                }
                return;
            case 1:// right
                for (int i = 0; i < decksCount; i++, y++) {
                    matrix[x][y] = 1;
                }
                return;
            case 2:// down
                for (int i = 0; i < decksCount; i++, x++) {
                    matrix[x][y] = 1;
                }
                return;
            case 3:// left
                for (int i = 0; i < decksCount; i++, y--) {
                    matrix[x][y] = 1;
                }
                return;
        }
    }

    private boolean isDirectionValid(int decksCount, int[][] matrix, int direction, int x, int y) {
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

    private int isValidAngle(int x, int y, int[][] matrix) { // return 0 if it is valid angle , 1 if it is not angle or -1 not valid angle
        if (x == 0 && y == 0) {
            if (matrix[x][y + 1] != 0 || matrix[x + 1][y + 1] != 0 || matrix[x + 1][y] != 0) {
                return -1;
            }
            return 0;
        } else if (x == 0 && y == 9) {
            if (matrix[x][y - 1] != 0 || matrix[x + 1][y - 1] != 0 || matrix[x + 1][y] != 0) {
                return -1;
            }
            return 0;
        } else if (y == 0 && x == 9) {
            if (matrix[x][y + 1] != 0 || matrix[x - 1][y + 1] != 0 || matrix[x - 1][y] != 0) {
                return -1;
            }
            return 0;
        } else if (x == 9 && y == 9) {
            if (matrix[x][y - 1] != 0 || matrix[x - 1][y - 1] != 0 || matrix[x - 1][y] != 0) {
                return -1;
            }
            return 0;
        }
        return 1;
    }

    private boolean isValidCell(int x, int y, int[][] matrix) {
        if (x == 0) {
            if (matrix[x][y - 1] != 0 || matrix[x][y + 1] != 0 || matrix[x + 1][y - 1] != 0 || matrix[x + 1][y + 1] != 0 || matrix[x + 1][y] != 0) {
                return false;
            }
        } else if (y == 0) {
            if (matrix[x][y + 1] != 0 || matrix[x + 1][y + 1] != 0 || matrix[x - 1][y + 1] != 0 || matrix[x + 1][y] != 0 || matrix[x - 1][y] != 0) {
                return false;
            }
        } else if (x == 9) {
            if (matrix[x][y - 1] != 0 || matrix[x][y + 1] != 0 || matrix[x - 1][y - 1] != 0 || matrix[x - 1][y + 1] != 0 || matrix[x - 1][y] != 0) {
                return false;
            }
        } else if (y == 9) {
            if (matrix[x][y - 1] != 0 || matrix[x + 1][y - 1] != 0 || matrix[x - 1][y - 1] != 0 || matrix[x + 1][y] != 0 || matrix[x - 1][y] != 0) {
                return false;
            }
        } else {
            if (matrix[x][y - 1] != 0 || matrix[x][y + 1] != 0 || matrix[x + 1][y - 1] != 0 || matrix[x + 1][y + 1] != 0 ||
                    matrix[x - 1][y - 1] != 0 || matrix[x - 1][y + 1] != 0 || matrix[x + 1][y] != 0 || matrix[x - 1][y] != 0) {

                return false;
            }
        }
        return true;
    }

    private boolean checkCell(int x, int y, int[][] matrix) {
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

    private boolean checkUp(int decksCount, int x, int y, int[][] matrix) {
        for (int i = 0; i < decksCount; i++, x--) {
            if (x < 0 || !checkCell(x, y, matrix)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkRight(int decksCount, int x, int y, int[][] matrix) {
        for (int i = 0; i < decksCount; i++, y++) {
            if (y > 9 || !checkCell(x, y, matrix)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkDown(int decksCount, int x, int y, int[][] matrix) {
        for (int i = 0; i < decksCount; i++, x++) {
            if (x > 9 || !checkCell(x, y, matrix)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkLeft(int decksCount, int x, int y, int[][] matrix) {
        for (int i = 0; i < decksCount; i++, y--) {
            if (y < 0 || !checkCell(x, y, matrix)) {
                return false;
            }
        }
        return true;
    }


}
