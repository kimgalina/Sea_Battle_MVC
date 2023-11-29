import javax.swing.JFrame;
import java.awt.CardLayout;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class Viewer {
    private Controller controller;
    private Canvas canvas;
    private Canvas player1Canvas;
    private Canvas player2Canvas;
    private JFrame frame;
    private CardLayout cardLayout;
    private SettingsPanel settings;
    private Menu menu;

    public Viewer() {
        Controller controller = new Controller(this);
        Model model = controller.getModel();
        canvas = new Canvas(model);
        settings = new SettingsPanel(this, model);
        menu = new Menu(this, model);
        player1Canvas = new Canvas(model);
        player2Canvas = new Canvas(model);

        cardLayout = new CardLayout();

        frame = new JFrame("Battleship");
        frame.setSize(1200, 720);
        frame.setLocation(100, 10);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(cardLayout);

        frame.add(menu, "menu");
        frame.add(settings, "settings");
        frame.add(canvas, "canvas");

        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);

        frame.add(player1Canvas, "player1");
        frame.add(separator, "separator");
        frame.add(player2Canvas, "player2");

        frame.setVisible(true);
        frame.setResizable(true);

        frame.addMouseListener(controller);
    }

    public void update() {
        canvas.repaint();
    }

    public void showSettings() {
        cardLayout.show(frame.getContentPane(), "settings");
    }

    public void showCanvas() {
        cardLayout.show(frame.getContentPane(), "canvas");
    }
}
