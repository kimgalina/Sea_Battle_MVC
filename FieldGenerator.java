import java.util.Random;


public class FieldGenerator {
    private Random random;

    public FieldGenerator() {
        random = new Random();
    }


    public int[][] generateField() {
        int[][] matrix = new int[10][10];
        for(int i = 4,k = 1; i > 0; i--,k++) {
            for(int j = k; j > 0; j--) {
                buildShip(i, matrix);
                System.out.println("build ship with " + i + " decks");
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
            do{
                System.out.println("generating new coordinates >>>");
                x = generateCoordinate();
                y = generateCoordinate();
                System.out.println("x = " + x + " y = " + y);
            } while(matrix[x][y] != 0);// while cell is not empty
            direction = generateDirection();
            System.out.println("direction = " + direction);
        } while (!isDirectionValid(decksCount, matrix, direction, x, y));
        setShip(x, y, direction, decksCount, matrix);

    }

    private void setShip(int x , int y, int direction, int decksCount, int[][] matrix) {
        System.out.println("SET SHIP");
        switch(direction) {
            case 0:// up
               for(int i = 0; i < decksCount; i++, x--) {
                   matrix[x][y] = 1;
               }
               return;
            case 1:// right
                for(int i = 0; i < decksCount; i++, y++) {
                    matrix[x][y] = 1;
                }
                return;
            case 2:// down
                for(int i = 0; i < decksCount; i++, x++) {
                    matrix[x][y] = 1;
                }
                return;
            case 3:// left
                for(int i = 0; i < decksCount; i++, y--) {
                    matrix[x][y] = 1;
                }
                return;
        }
    }
    private boolean isDirectionValid(int decksCount, int[][] matrix, int direction, int x, int y) {
        switch(direction) {
            case 0:// up
                return checkUp(decksCount, x , y, matrix);
            case 1:// right
                return checkRight(decksCount, x , y, matrix);
            case 2:// down
                return checkDown(decksCount, x , y, matrix);
            case 3:// left
                return checkLeft(decksCount, x , y, matrix);
        }
        return false;
    }
    private int isValidAngle(int x, int y, int[][] matrix) { // return 0 if it is valid angle , 1 if it is not angle or -1 not valid angle
        if(x == 0 && y == 0) {
            if (matrix[x][y+1] != 0 || matrix[x + 1][y + 1] != 0 || matrix[x + 1][y] != 0 ) {
                // if there is other ship
                System.out.println("NOT valid angle");
                return -1;
            }
            System.out.println("Valid angle");
            return 0;
        } else if (x == 0 && y == 9) {
            if (matrix[x][y-1] != 0 || matrix[x + 1][y - 1] != 0 || matrix[x + 1][y] != 0 ) {
                // if there is other ship
                return -1;
            }
            System.out.println("Valid angle");
            return 0;
        } else if (y == 0 && x == 9) {
            if (matrix[x][y+1] != 0 || matrix[x - 1][y + 1] != 0 || matrix[x - 1][y] != 0) {
                // if there is other ship
                return -1;
            }
            System.out.println("Valid angle");
            return 0;
        } else if (x == 9 && y == 9) {
            if (matrix[x][y-1] != 0 || matrix[x - 1][y - 1] != 0 || matrix[x - 1][y] != 0) {
                // if there is other ship
                return -1;
            }
            System.out.println("Valid angle");
            return 0;
        }
        System.out.println("not angle");
        return 1;
    }
    private boolean isValidCell(int x, int y, int[][] matrix) {
        if(x == 0) {
            if (matrix[x][y-1] != 0 || matrix[x][y+1] != 0 || matrix[x + 1][y - 1] != 0 || matrix[x + 1][y + 1] != 0 || matrix[x + 1][y] != 0) {
                // if there is other ship
                return false;
            }
        } else if (y == 0) {
            if (matrix[x][y+1] != 0 || matrix[x + 1][y + 1] != 0 || matrix[x - 1][y + 1] != 0 || matrix[x + 1][y] != 0 || matrix[x - 1][y] != 0) {
                // if there is other ship
                return false;
            }
        } else if (x == 9) {
            if (matrix[x][y-1] != 0 || matrix[x][y+1] != 0 || matrix[x - 1][y - 1] != 0 || matrix[x - 1][y + 1] != 0 || matrix[x - 1][y] != 0) {
                // if there is other ship
                return false;
            }
        } else if (y == 9) {
            if (matrix[x][y-1] != 0 || matrix[x + 1][y - 1] != 0 || matrix[x - 1][y - 1] != 0 || matrix[x + 1][y] != 0 || matrix[x - 1][y] != 0) {
                // if there is other ship
                return false;
            }
        } else {
            if (matrix[x][y-1] != 0 || matrix[x][y+1] != 0 || matrix[x + 1][y - 1] != 0 || matrix[x + 1][y + 1] != 0 ||
                    matrix[x - 1][y - 1] != 0 || matrix[x - 1][y + 1] != 0 || matrix[x + 1][y] != 0 || matrix[x - 1][y] != 0) {
                // if there is other ship
                return false;
            }
        }
        return true;
    }

    private boolean checkCell(int x, int y, int[][] matrix) {
        int result = isValidAngle(x, y, matrix);
        switch(result) {
            case 0:
                return true;
            case -1:
                return false;
            case 1:
                return isValidCell(x, y , matrix);
        }
        return false;
    }

    private boolean checkUp(int decksCount, int x, int y, int[][] matrix) {
        System.out.println("Check UP");
        for(int i = 0; i < decksCount; i++, x--) {
            System.out.println("x = " + x + "  y = " + y);
            if(x < 0 || !checkCell(x, y, matrix)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkRight(int decksCount, int x, int y, int[][] matrix) {
        System.out.println("Check Right");
        for(int i = 0; i < decksCount; i++, y++) {
            System.out.println("x = " + x + "  y = " + y);
            if(y > 9 || !checkCell(x, y, matrix)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkDown(int decksCount, int x, int y, int[][] matrix) {
        System.out.println("Check DOWN");
        for(int i = 0; i < decksCount; i++, x++) {
            System.out.println("x = " + x + "  y = " + y);
            if(x > 9 || !checkCell(x, y, matrix)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkLeft(int decksCount, int x, int y, int[][] matrix) {
        System.out.println("Check LEFT");
        for(int i = 0; i < decksCount; i++, y--) {
            System.out.println("x = " + x + "  y = " + y);
            if(y < 0 || !checkCell(x, y, matrix)) {
                return false;
            }
        }
        return true;
    }


}
