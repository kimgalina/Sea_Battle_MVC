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
    setBackground(Color.BLACK);
    font = new Font("Impact", Font.PLAIN, 50);
  }

  public void paint(Graphics g) {
    super.paint(g);
    Graphics2D g2d = (Graphics2D)g;

    int x = model.getX();
    int y = model.getY();
    g2d.setColor(Color.YELLOW);
    g2d.setFont(font);
    g2d.drawString("" + x + " " + y, x - 10, y - 35);

    Cell cell1 = model.getCell1();
    g2d.setColor(Color.RED);
    g2d.draw(cell1);
    Cell cell2 = model.getCell2();
    g2d.setColor(Color.YELLOW);
    g2d.draw(cell2);

  }
}
