import javax.swing.JFrame;

public class Viewer {
    private Controller controller;
    private Canvas canvas;
    private JFrame frame;

    public Viewer() {
        controller = new Controller(this);
        Model model = controller.getModel();
        canvas = new Canvas(model);

        frame = new JFrame("Battleship");
        frame.setSize(1200, 720);
        frame.setLocation(100, 10);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addMouseListener(controller);

        frame.add(canvas);

        frame.setVisible(true);
        frame.setResizable(true);
    }

    public void update() {
        canvas.repaint();
    }
}
