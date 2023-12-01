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
        drawShips(g2d, new Cell("", 0, 0, 0, 0, 0)); // todo: how to update right image
    }

    private void drawGrid(Graphics2D g2d) {


        int cellSize = 50;
        int boardSize = model.getBoardSize();

        // Вычисляем смещение по горизонтали и вертикали для центрирования левой половины поля
        int xOffset = (getWidth() - boardSize * cellSize * 2) / 4; // Общее смещение
        int yOffset = (getHeight() - boardSize * cellSize) / 2; // Вертикальное смещение

        Cell enemyBoard = model.getBoardEnemyBoard();
        enemyBoard.setLocation(xOffset, yOffset);
        g2d.setColor(Color.GRAY);
        g2d.fill(enemyBoard);
        g2d.setColor(Color.WHITE);
        g2d.draw(enemyBoard);

        // Размер шрифта для цифр
        Font numberFont = new Font("Arial", Font.PLAIN, 20);
        g2d.setFont(numberFont);

        // Рисуем сетку для вашего поля
        for (int i = 0; i <= boardSize; i++) {
            g2d.drawLine(i * cellSize + xOffset, yOffset, i * cellSize + xOffset, boardSize * cellSize + yOffset);

            // Добавляем цифры слева
            if (i > 0 && i <= boardSize) {
                String number = Integer.toString(i);
                int numberX = xOffset / 2 - g2d.getFontMetrics().stringWidth(number) / 2;
                int numberY = i * cellSize + yOffset - cellSize / 2 + g2d.getFontMetrics().getAscent() / 2;
                g2d.drawString(number, numberX, numberY);
            }
        }

        for (int i = 0; i <= boardSize; i++) {
            g2d.drawLine(xOffset, i * cellSize + yOffset, boardSize * cellSize + xOffset, i * cellSize + yOffset);

            // Добавляем буквы сверху
            if (i > 0 && i <= boardSize) {
                char letter = (char) ('A' + i - 1);
                String letterStr = Character.toString(letter);
                int letterX = i * cellSize + xOffset - cellSize / 2 - g2d.getFontMetrics().stringWidth(letterStr) / 2;
                int letterY = yOffset / 2 + g2d.getFontMetrics().getAscent() / 2;
                g2d.drawString(letterStr, letterX, letterY);
            }
        }

        // Рисуем разделительную полосу
        g2d.drawLine(boardSize * cellSize + xOffset * 2, 0, boardSize * cellSize + xOffset * 2, getHeight());

        // Рисуем сетку для поля противника
        int enemyXOffset = getWidth() / 2 + boardSize * cellSize / 10; // Смещение вправо от разделительной полосы
        int enemyYOffset = yOffset; // Также вертикальное смещение для правой половины окна

        // Рисуем сетку для правой половины поля противника
        for (int i = 0; i <= boardSize; i++) {
            g2d.drawLine(i * cellSize + enemyXOffset, enemyYOffset, i * cellSize + enemyXOffset, boardSize * cellSize + enemyYOffset);

            // Добавляем цифры справа
            if (i > 0 && i <= boardSize) {
                String number = Integer.toString(i);
                int numberX = boardSize * cellSize + xOffset * 2 + xOffset / 2 - g2d.getFontMetrics().stringWidth(number) / 2;
                int numberY = i * cellSize + enemyYOffset - cellSize / 2 + g2d.getFontMetrics().getAscent() / 2;
                g2d.drawString(number, numberX, numberY);
            }
        }

        for (int i = 0; i <= boardSize; i++) {
            g2d.drawLine(enemyXOffset, i * cellSize + enemyYOffset, boardSize * cellSize + enemyXOffset, i * cellSize + enemyYOffset);

            // Добавляем буквы сверху
            if (i > 0 && i <= boardSize) {
                char letter = (char) ('A' + i - 1);
                String letterStr = Character.toString(letter);
                int letterX = boardSize * cellSize + xOffset * 2 + i * cellSize + xOffset - cellSize / 2 - g2d.getFontMetrics().stringWidth(letterStr) / 2;
                int letterY = yOffset / 2 + g2d.getFontMetrics().getAscent() / 2;
                g2d.drawString(letterStr, letterX, letterY);
            }
        }
    }



    private void drawShips(Graphics2D g2d, Cell cell) {
        Cell[][] board = model.getBoardArray();
        int cellSize = 50;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Cell cell1 = board[i][j];
                if (cell1 != null) {
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(cell.x, cell.y, cell.width, cell.height);
                    g2d.setColor(Color.RED);
                    g2d.drawRect(cell.x, cell.y, cell.width, cell.height);
                }
            }
        }
    }
}
