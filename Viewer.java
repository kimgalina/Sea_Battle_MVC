import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JComponent;

public class Viewer {
    private Controller controller;
    private Canvas canvas;
    private JFrame frame;

    public Viewer() {
        new SplashScreen();

        controller = new Controller(this);
        Model model = controller.getModel();
        canvas = new Canvas(model, controller);
        frame = new JFrame("Battleship");
        frame.setSize(1200, 720);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(new ImageIcon("images/game-icon.png").getImage());

        frame.add(canvas);
        frame.setVisible(true);
        frame.setResizable(true);
    }

    public void update() {
        canvas.repaint();
    }

    public void addRocket(ImageIcon rocketIcon, int x, int y) {
        JLabel rocketLabel = new JLabel(rocketIcon);
        rocketLabel.setBounds(x, y, rocketIcon.getIconWidth(), rocketIcon.getIconHeight());
        frame.add(rocketLabel);
        frame.repaint();
    }

    public void removeRocket(JLabel rocketLabel) {
        frame.remove(rocketLabel);
        frame.repaint();
    }

    public void add(JComponent component, int x, int y) {
        component.setBounds(x, y, component.getWidth(), component.getHeight());
        frame.add(component);
        frame.repaint();
    }


}
