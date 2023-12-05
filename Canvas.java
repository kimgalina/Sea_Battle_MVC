import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;

public class Canvas extends JPanel {

    private Model model;
    private Font font;


    public Canvas(Model model, Controller controller) {

        this.model = model;
        setBackground(new Color(0, 119, 190));
        font = new Font("Arial", Font.PLAIN, 20);
        addMouseListener(controller);
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        drawGrid(g2d);
        drawShips(g2d);
    }

    private void drawGrid(Graphics2D g2d) {
        int cellSize = 50;
        int boardSize = model.getBoardSize();

        int xOffset = 50;
        int yOffset = 100;

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
        Cell[][] userBoard = model.getUserBoardArray();
        Cell[][] enemyBoard = model.getEnemyBoardArray();

        for (int i = 0; i < userBoard.length; i++) {
            for (int j = 0; j < userBoard[i].length; j++) {
                Cell cell = userBoard[i][j];
                if (cell.getValue() == 1 ) {
                    if(cell.getShip().isHorizontal()) {
                        g2d.rotate(Math.toRadians(270), cell.x + cell.width / 2, cell.y + cell.height / 2);
                        g2d.drawImage(cell.getImage(), cell.x, cell.y, cell.width, cell.height, null);
                        g2d.rotate(Math.toRadians(90), cell.x + cell.width / 2, cell.y + cell.height / 2);
                    } else {
                        g2d.setColor(Color.WHITE);
                        g2d.drawImage(cell.getImage(), cell.x, cell.y, cell.width, cell.height, null);
                    }
                    continue;
                }
                if (cell.isVisible()) {
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(cell.x, cell.y, cell.width, cell.height);
                    g2d.setColor(Color.RED);
                    g2d.drawRect(cell.x, cell.y, cell.width, cell.height);

                }
            }
        }

        for (int i = 0; i < enemyBoard.length; i++) {
            for (int j = 0; j < enemyBoard[i].length; j++) {
                Cell cell = enemyBoard[i][j];
                if (cell.getValue() == 1 || cell.getValue() == 2 || cell.getValue() == 4 ) {
                    if(cell.getShip().isHorizontal()) {
                        g2d.rotate(Math.toRadians(270), cell.x + cell.width / 2, cell.y + cell.height / 2);
                        g2d.drawImage(cell.getImage(), cell.x, cell.y, cell.width, cell.height, null);
                        g2d.rotate(Math.toRadians(90), cell.x + cell.width / 2, cell.y + cell.height / 2);
                    } else {
                        g2d.drawImage(cell.getImage(), cell.x, cell.y, cell.width, cell.height, null);
                    }
                    continue;
                }
                if (cell.isVisible()) {
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(cell.x, cell.y, cell.width, cell.height);
                    g2d.setColor(Color.RED);
                    g2d.drawRect(cell.x, cell.y, cell.width, cell.height);
                    continue;
                }
            }
        }
    }
}
