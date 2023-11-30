public class Model {
    private Viewer viewer;

    public Model(Viewer viewer) {
        this.viewer = viewer;
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
