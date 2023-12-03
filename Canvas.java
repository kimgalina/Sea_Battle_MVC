import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Image;

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
//        drawShips(g2d);
    }

    private void drawGrid(Graphics2D g2d) {
        int cellSize = 50;
        int boardSize = model.getBoardSize();

        int xOffset = 50;
        int yOffset = 100;

        Cell enemyBoard = model.getEnemyBoard();
//        enemyBoard.setLocation(xOffset, yOffset);
        g2d.setColor(Color.GRAY);
        g2d.fill(enemyBoard);
        g2d.setColor(Color.WHITE);
        g2d.draw(enemyBoard);

        Font numberFont = new Font("Arial", Font.PLAIN, 20);
        g2d.setFont(numberFont);

        for (int i = 0; i <= boardSize; i++) {
            g2d.drawLine(i * cellSize + xOffset, yOffset, i * cellSize + xOffset, boardSize * cellSize + yOffset);

            if (i > 0 && i <= boardSize) {
                String number = Integer.toString(i);
                int numberX = xOffset / 2 - g2d.getFontMetrics().stringWidth(number) / 2;
                int numberY = i * cellSize + yOffset - cellSize / 2 + g2d.getFontMetrics().getAscent() / 2;
                g2d.drawString(number, numberX, numberY);
            }
        }

        for (int i = 0; i <= boardSize; i++) {
            g2d.drawLine(xOffset, i * cellSize + yOffset, boardSize * cellSize + xOffset, i * cellSize + yOffset);

            if (i > 0 && i <= boardSize) {
                char letter = (char) ('A' + i - 1);
                String letterStr = Character.toString(letter);
                int letterX = i * cellSize + xOffset - cellSize / 2 - g2d.getFontMetrics().stringWidth(letterStr) / 2;
                int letterY = yOffset / 2 + g2d.getFontMetrics().getAscent() / 2;
                g2d.drawString(letterStr, letterX, letterY);
            }
        }

        int enemyXOffset = 650;
        int enemyYOffset = yOffset;

        for (int i = 0; i <= boardSize; i++) {
            g2d.drawLine(i * cellSize + enemyXOffset, enemyYOffset, i * cellSize + enemyXOffset, boardSize * cellSize + enemyYOffset);

            if (i > 0 && i <= boardSize) {
                String number = Integer.toString(i);
                int numberX = boardSize * cellSize + xOffset * 2 + xOffset / 2 - g2d.getFontMetrics().stringWidth(number) / 2;
                int numberY = i * cellSize + enemyYOffset - cellSize / 2 + g2d.getFontMetrics().getAscent() / 2;
                g2d.drawString(number, numberX, numberY);
            }
        }

        for (int i = 0; i <= boardSize; i++) {
            g2d.drawLine(enemyXOffset, i * cellSize + enemyYOffset, boardSize * cellSize + enemyXOffset, i * cellSize + enemyYOffset);

            if (i > 0 && i <= boardSize) {
                char letter = (char) ('A' + i - 1);
                String letterStr = Character.toString(letter);
                int letterX = boardSize * cellSize + xOffset * 2 + i * cellSize + xOffset - cellSize / 2 - g2d.getFontMetrics().stringWidth(letterStr) / 2;
                int letterY = yOffset / 2 + g2d.getFontMetrics().getAscent() / 2;
                g2d.drawString(letterStr, letterX, letterY);
            }
        }
    }

    private void drawShips(Graphics2D g2d) {
        Cell[][] board = model.getBoardArray();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Cell cell = board[i][j];
//                x = ((getWidth() - 10 * cellSize * 2) / 4) + cellSize * j;
//                y = ((getHeight() - 10 * cellSize) / 2) + cellSize * i;
//                cell.setLocation(x, y);
                if (cell != null) {
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(cell.x, cell.y, cell.width, cell.height);
                    g2d.setColor(Color.RED);
                    g2d.drawRect(cell.x, cell.y, cell.width, cell.height);
                }
            }
        }
//        Image image1 = new ImageIcon("images/2ship-1.png").getImage();
//        Image image2 = new ImageIcon("images/2ship-2.png").getImage();
//        g2d.drawImage(image1, board[2][2].x, board[2][2].y, 50, 50, null);
//        g2d.drawImage(image2, board[2][3].x, board[2][3].y, 50, 50, null);
    }
}
