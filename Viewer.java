import javax.swing.JFrame;

public class Viewer {
  private Canvas canvas;

  public Viewer() {
    Controller controller = new Controller(this);
    Model model = controller.getModel();
    canvas = new Canvas(model);

    JFrame frame = new JFrame("Sea Battle Application");
    frame.setSize(800, 800);
    frame.setLocation(300, 100);
    frame.add(canvas);
    frame.setVisible(true);
    frame.addMouseListener(controller);
    Cell cell1 = model.getCell1();
    frame.addMouseListener(cell1);
    Cell cell2 = model.getCell2();
    frame.addMouseListener(cell2);

  }

  public void update() {
    canvas.repaint();

  }
}
