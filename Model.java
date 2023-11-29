  public class Model {
    private Viewer viewer;
    private int x;
    private int y;

    private Cell cell1;
    private Cell cell2;
    private Cell[][] board;

    public Model(Viewer viewer) {
      this.viewer = viewer;
      x = -1;
      y = -1;

      cell1 = new Cell("RED", 100, 100, 100, 100);
      cell2 = new Cell("YELLOW", 250, 100, 100, 100);
      board = new Cell[10][10];
    }

    public Cell[][] getBoard() {
        return board;
    }

    public void doAction(int x, int y) {
      this.x = x;
      this.y = y;
      viewer.update();
    }

    public int getX() {
      return x;
    }

    public int getY() {
      return y;
    }

    public Cell getCell1() {
      return cell1;
    }

    public Cell getCell2() {
      return cell2;
    }

    public int getBoardSize() {
        return 10;
    }

    public boolean isCellOccupied(int row, int col) {
        return cell1.contains(row, col) || cell2.contains(row, col);
    }

    public void placeShip(int row, int col, int shipSize, boolean isHorizontal) {
        // Проверки на корректность координат и направления корабля
        // ...

        // Смещение начальных координат для центрирования
        int xOffset = (10 - shipSize) / 2; // смещение по горизонтали
        int yOffset = (10 - shipSize) / 2; // смещение по вертикали

        // Заполнение соответствующих ячеек в массиве
        for (int i = 0; i < shipSize; i++) {
            int adjustedRow = row + yOffset;
            int adjustedCol = col + xOffset;

            if (isHorizontal) {
                board[adjustedRow][adjustedCol + i] = new Cell("SHIP", adjustedCol + i, adjustedRow, 40, 40);
            } else {
                board[adjustedRow + i][adjustedCol] = new Cell("SHIP", adjustedCol, adjustedRow + i, 40, 40);
            }
        }
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
