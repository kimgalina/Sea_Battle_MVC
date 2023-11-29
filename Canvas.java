import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;

public class Canvas extends JPanel {
    private Model model;
    private Font font;

    public Canvas(Model model) {
        this.model = model;
        setBackground(new Color(0, 119, 190));
        font = new Font("Arial", Font.PLAIN, 20);
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        drawGrid(g2d);
        drawShips(g2d);
    }

    private void drawGrid(Graphics2D g2d) {
       int cellSize = 40;
       int boardSize = model.getBoardSize();

       // Вычисляем смещение по горизонтали и вертикали для центрирования левой половины поля
       int xOffset = (getWidth() - boardSize * cellSize * 2) / 4; // Общее смещение
       int yOffset = (getHeight() - boardSize * cellSize) / 2; // Вертикальное смещение

       // Рисуем сетку для вашего поля
       for (int i = 0; i <= boardSize; i++) {
           g2d.drawLine(i * cellSize + xOffset, yOffset, i * cellSize + xOffset, boardSize * cellSize + yOffset);
       }

       for (int i = 0; i <= boardSize; i++) {
           g2d.drawLine(xOffset, i * cellSize + yOffset, boardSize * cellSize + xOffset, i * cellSize + yOffset);
       }

       // Рисуем разделительную полосу
       g2d.drawLine(boardSize * cellSize + xOffset * 2, 0, boardSize * cellSize + xOffset * 2, getHeight());

       // Рисуем сетку для поля противника
       int enemyXOffset = getWidth() / 2 + boardSize * cellSize / 4; // Смещение вправо от разделительной полосы
       int enemyYOffset = yOffset; // Также вертикальное смещение для правой половины окна

       // Рисуем сетку для правой половины поля противника
       for (int i = 0; i <= boardSize; i++) {
           g2d.drawLine(i * cellSize + enemyXOffset, enemyYOffset, i * cellSize + enemyXOffset, boardSize * cellSize + enemyYOffset);
       }

       for (int i = 0; i <= boardSize; i++) {
           g2d.drawLine(enemyXOffset, i * cellSize + enemyYOffset, boardSize * cellSize + enemyXOffset, i * cellSize + enemyYOffset);
       }
   }


    private void drawShips(Graphics2D g2d) {
        Cell[][] board = model.getBoard();
        int cellSize = 40;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Cell cell = board[i][j];
                if (cell != null) {
                    g2d.setColor(Color.GRAY);
                    g2d.fillRect(cell.x, cell.y, cell.width, cell.height);
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(cell.x, cell.y, cell.width, cell.height);
                }
            }
        }
    }
}
