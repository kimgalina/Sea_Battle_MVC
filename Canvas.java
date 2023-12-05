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


    public Canvas(Model model, Controller controller) {

        this.model = model;
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
        Image backgroundImage = new ImageIcon("images/background.jpg").getImage();
        Image seaBattleImage = new ImageIcon("images/sea-battle-cell.png").getImage();

        g2d.drawImage(backgroundImage,0,0, getWidth(), getHeight(), this);
        g2d.drawImage(seaBattleImage,0,50, 600, 600, this);
        g2d.drawImage(seaBattleImage,600,50, 600, 600, this);

        Font numberFont = new Font("Arial", Font.PLAIN, 30);
        g2d.setFont(numberFont);
        String boardName = "User Board";
        g2d.drawString(boardName, 240, 30);
        boardName = "Computer Board";
        g2d.drawString(boardName, 780, 30);

        Cell exitCell = model.getExitButton();
        Cell restartCell = model.getRestartButton();
        Cell startCell = model.getStartButton();

        g2d.setFont(font);
        String exit = "Exit";
        String restart = "Restart";
        String start = "Start";

        g2d.setColor(Color.RED);
        g2d.drawRect(exitCell.x, exitCell.y, exitCell.width, exitCell.height);
        g2d.drawString(exit, 135, 650);
        g2d.drawRect(restartCell.x, restartCell.y, restartCell.width, restartCell.height);
        g2d.drawString(restart, 270, 650);
        g2d.drawRect(startCell.x, startCell.y, startCell.width, startCell.height);
        g2d.drawString(start, 430, 650);

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
                } else {
                    g2d.drawImage(cell.getImage(), cell.x, cell.y, cell.width, cell.height, null);
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
                } else {
                    g2d.drawImage(cell.getImage(), cell.x, cell.y, cell.width, cell.height, null);
                }
            }
        }
    }
}
