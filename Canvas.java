import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Image;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.io.ObjectOutputStream;

@SuppressWarnings("serial")
public class Canvas extends JPanel {

    private final Model model;
    private final Image seaBattleImage;
    private final Font font;
    private final Font numberFont;
    private final RoundRectangle2D exitButton;
    private final RoundRectangle2D restartButton;
    private final RoundRectangle2D stopButton;
    private final RoundRectangle2D largeStartButton;

    public Canvas(Model model, Controller controller) {
        Image backgroundImage = new ImageIcon("images/background.jpg").getImage();
        seaBattleImage = new ImageIcon("images/sea-battle-cell.png").getImage();

        font = new Font("Franklin Gothic Heavy", Font.PLAIN, 25);
        numberFont = new Font("Franklin Gothic Heavy", Font.PLAIN, 30);
        int arcWidth = 30;
        int arcHeight = 30;

        JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
        backgroundLabel.setSize(1200, 720);
        add(backgroundLabel);

        exitButton = new RoundRectangle2D.Double(100, 620, 100, 50, arcWidth, arcHeight);
        RoundRectangle2D soundButton = new RoundRectangle2D.Double(900, 620, 100, 100, arcWidth, arcHeight);
        restartButton = new RoundRectangle2D.Double(250, 620, 100, 50, arcWidth, arcHeight);
        stopButton = new RoundRectangle2D.Double(400, 620, 100, 50, arcWidth, arcHeight);
        largeStartButton = new RoundRectangle2D.Double(500, 300, 200, 100, arcWidth, arcHeight);

        this.model = model;
        addMouseListener(controller);
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        if (model.getUserShipsNumber() == 0) {
            drawFinish(g2d, true);
        } else if (model.getComputerShipsNumber() == 0) {
            drawFinish(g2d, false);
        }

        drawGrid(g2d);

        if (!model.getStartButton().isVisible()) {
            setComposite(g2d, 0.2f);
        }

        drawBoards(g2d);
    }

    private void drawFinish(Graphics2D g2d, boolean isUser) {
        Font finishFont = new Font("Rockwell Extra Bold", Font.BOLD, 30);
        g2d.setFont(finishFont);
        if (isUser) {
            g2d.setColor(Color.RED);
            g2d.drawString("Computer WIN!", 650, 640);
            return;
        }
        g2d.setColor(new Color(22, 73, 1));
        g2d.drawString("YOU WIN!", 650, 640);
    }


    private void setComposite(Graphics2D g2d, float alpha) {
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    }

    private void drawGrid(Graphics2D g2d) {

        if (!model.getStartButton().isVisible()) {
            setComposite(g2d, 0.5f);
        }

        drawSeaBattle(g2d);
        drawBoardNames(g2d);

        setComposite(g2d, 1.0f);
        drawButtons(g2d);
    }

    private void drawSeaBattle(Graphics2D g2d) {
        g2d.drawImage(seaBattleImage, 0, 50, 600, 600, this);
        g2d.drawImage(seaBattleImage, 600, 50, 600, 600, this);
    }

    private void drawBoardNames(Graphics2D g2d) {
        drawBoardName(g2d, numberFont, Color.BLACK, "User's Board", 240, 30);
        drawBoardName(g2d, numberFont, new Color(153, 0, 0), "Computer's Board", 780, 30);
    }

    private void drawBoardName(Graphics2D g2d, Font font, Color color, String name, int x, int y) {
        g2d.setFont(font);
        g2d.setColor(color);
        g2d.drawString(name, x, y);
    }

    private void drawButtons(Graphics2D g2d) {
        drawButton(g2d, Color.RED, "Exit", 120, 655, font, exitButton);
        drawButton(g2d, new Color(128, 128, 0), "Restart", 260, 655, font, restartButton);
        drawButton(g2d, new Color(199, 52, 0), "Stop", 420, 655, font, stopButton);
        drawSoundButton(g2d, model.getSoundButton());

        if (!model.getStartButton().isVisible()) {
            drawButton(g2d, new Color(1, 84, 6), "Start", 530, 370, new Font("Franklin Gothic Heavy", Font.PLAIN, 55), largeStartButton);
        }
    }

    private void drawButton(Graphics2D g2d, Color color, String label, int x, int y, Font font, RoundRectangle2D roundedRectangle) {
        g2d.setColor(color);
        g2d.draw(roundedRectangle);

        if (!model.getStartButton().isVisible()) {
            g2d.fill(roundedRectangle);
            g2d.setColor(Color.BLACK);
        }
        g2d.setFont(font);
        g2d.drawString(label, x, y);

    }

    private void drawSoundButton(Graphics2D g2d, Cell cell) {
        g2d.drawImage(cell.getImage(), cell.x, cell.y, cell.width, cell.height, null);
    }

//    private void drawBoardBorder(Graphics2D g2d) {
//        Cell border = model.getComputerBoardBorder();
//        if (model.isUserTurn()) {
//            g2d.setColor(Color.GREEN);
//        }
//        g2d.setStroke(new BasicStroke(5f));
//        g2d.drawRoundRect(border.x, border.y, border.width, border.height, 20, 20);
//    }

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
