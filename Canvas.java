import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
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
    int x = model.getX();
    int y = model.getY();
    g.setColor(Color.YELLOW);
    g.setFont(font);
    g.drawString("" + x + " " + y, x - 10, y - 35);

    Cell cell1 = model.getCell1();
    g.setColor(Color.RED);
    g.drawRect(cell1.getXCell(), cell1.getYCell(), cell1.getWidthCell(), cell1.getHeightCell());

    Cell cell2 = model.getCell2();
    g.setColor(Color.YELLOW);
    g.drawRect(cell2.getXCell(), cell2.getYCell(), cell2.getWidthCell(), cell2.getHeightCell());

    g.drawString("Rectangle1 " + cell1.getX() + " " + cell1.getY() + " " + cell1.getWidth() + " " + cell1.getHeight(), 300, 300);

  }
}
