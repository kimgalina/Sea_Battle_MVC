public class Model {
  private Viewer viewer;
  private int x;
  private int y;

  private Cell cell1;
  private Cell cell2;

  public Model(Viewer viewer) {
    this.viewer = viewer;
    x = -1;
    y = -1;

    cell1 = new Cell("RED", 100, 100, 100, 100);
    cell2 = new Cell("YELLOW", 250, 100, 100, 100);
  }

  public void doAction(int x, int y) {
    this.x = x;
    this.y = y;
    viewer.update();
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public Cell getCell1() {
    return cell1;
  }

  public Cell getCell2() {
    return cell2;
  }
}
