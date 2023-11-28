import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Rectangle;

public class Cell extends Rectangle implements MouseListener {

  // private int x;
  // private int y;
  // private int width;
  // private int height;
  private String name;

  public Cell (String name, int x, int y, int width, int height) {
    super(x, y, width, height);

    this.name = name;
    // this.x = x;
    // this.y = y;
    // this.width = width;
    // this.height = height;
  }

  public int getXCell() {
    return x;
  }

  public int getYCell() {
    return y;
  }

  public int getWidthCell() {
    return width;
  }

  public int getHeightCell() {
    return height;
  }

  public void mouseClicked(MouseEvent event) {
  }

  public void mousePressed(MouseEvent event) {
    int x1 = event.getX();
    int y1 = event.getY();
    if(contains(x1, y1)) {
      System.out.println(name + " " + x + " " + y);
    }

  }

  public void mouseReleased(MouseEvent event) {
  }

  public void mouseEntered(MouseEvent event) {
  }

  public void mouseExited(MouseEvent event) {
  }

  public boolean contains(int row, int col) {
        return row >= y && row <= y + height && col >= x && col <= x + width;
    }
}
