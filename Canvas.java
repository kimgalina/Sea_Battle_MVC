import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Image;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.io.ObjectOutputStream;

@SuppressWarnings("serial")
public class Canvas extends JPanel {

    private Model model;

    public Canvas(Model model, Controller controller) {

        this.model = model;
        addMouseListener(controller);

    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        drawGrid(g2d);
        drawBoards(g2d);
    }


    private void drawGrid(Graphics2D g2d) {
        drawBackground(g2d);
        drawSeaBattleImages(g2d);
        drawBoardNames(g2d);
        drawButtons(g2d);
    }

    private void drawBackground(Graphics2D g2d) {
        Image backgroundImage = new ImageIcon("images/background.jpg").getImage();
        g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    private void drawSeaBattleImages(Graphics2D g2d) {
        Image seaBattleImage = new ImageIcon("images/sea-battle-cell.png").getImage();
        g2d.drawImage(seaBattleImage, 0, 50, 600, 600, this);
        g2d.drawImage(seaBattleImage, 600, 50, 600, 600, this);
    }

    private void drawBoardNames(Graphics2D g2d) {
        Font numberFont = new Font("Franklin Gothic Heavy", Font.PLAIN, 30);

        drawBoardName(g2d, numberFont, Color.BLACK, "User's Board", 240, 30);
        drawBoardName(g2d, numberFont, new Color(153, 0, 0), "Computer's Board", 780, 30);
    }

    private void drawBoardName(Graphics2D g2d, Font font, Color color, String name, int x, int y) {
        g2d.setFont(font);
        g2d.setColor(color);
        g2d.drawString(name, x, y);
    }

    private void drawButtons(Graphics2D g2d) {
        Font font = new Font("Franklin Gothic Heavy", Font.PLAIN, 25);
        int arcWidth = 30;
        int arcHeight = 30;

        RoundRectangle2D exitButton = new RoundRectangle2D.Double(100, 620, 100, 50, arcWidth, arcHeight);
        RoundRectangle2D restartButton = new RoundRectangle2D.Double(250, 620, 100, 50, arcWidth, arcHeight);
        RoundRectangle2D startButton = new RoundRectangle2D.Double(400, 620, 100, 50, arcWidth, arcHeight);

        drawButton(g2d, Color.RED, "Exit", 120, 655, font, exitButton);
        drawButton(g2d, new Color(189, 189, 0), "Restart", 260, 655, font, restartButton);
        drawButton(g2d, new Color(0, 102, 0), "Start", 420, 655, font, startButton);
    }

    private void drawButton(Graphics2D g2d, Color color, String label, int x, int y, Font font, RoundRectangle2D roundedRectangle) {
        g2d.setColor(color);
        g2d.draw(roundedRectangle);
        g2d.setFont(font);
        g2d.drawString(label, x, y);
    }

    public void drawBoards(Graphics2D g2d) {
        Cell[][] userBoard = model.getUserBoardArray();
        Cell[][] enemyBoard = model.getEnemyBoardArray();

        for (Cell[] row : userBoard) {
            for (Cell cell : row) {
                drawCell(g2d, cell, false);
            }
        }

        for (Cell[] row : enemyBoard) {
            for (Cell cell : row) {
                drawCell(g2d, cell, true);
            }
        }
    }

    private void drawCell(Graphics2D g2d, Cell cell, boolean isEnemyBoard) {
        if (cell.isVisible() && isEnemyBoard) {
            return;
        }

        if (cell.getValue() == 1 || cell.getValue() == 2 || cell.getValue() == 4) {
            if (cell.getShip().isHorizontal()) {
                g2d.rotate(Math.toRadians(270), cell.x + cell.width / 2, cell.y + cell.height / 2);
                g2d.drawImage(cell.getImage(), cell.x, cell.y, cell.width, cell.height, null);
                g2d.rotate(Math.toRadians(90), cell.x + cell.width / 2, cell.y + cell.height / 2);
            } else {
                g2d.drawImage(cell.getImage(), cell.x, cell.y, cell.width, cell.height, null);
            }
        } else if (!cell.isVisible()) {
            g2d.drawImage(cell.getImage(), cell.x, cell.y, cell.width, cell.height, null);
        }
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        throw new IOException("This class is NOT serializable.");
    }
}
