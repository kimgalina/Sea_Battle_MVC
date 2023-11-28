import javax.swing.JFrame;
import java.awt.CardLayout;

public class Viewer {
    private Controller controller;
    private Canvas canvas;
    private JFrame frame;
    private CardLayout cardLayout;
    private SettingsPanel settings;
    public Viewer() {

        controller = new Controller(this);
        Model model = controller.getModel();
        canvas = new Canvas(model);
        settings = new SettingsPanel(this, model);
        Menu menu = new Menu(this, model);

        cardLayout = new CardLayout();

        frame = new JFrame("Battleship");
        frame.setSize(1200,720);
        frame.setLocation(100,10);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(cardLayout);

        frame.add(menu, "menu");
        frame.add(settings, "settings");
        frame.setVisible(true);
        frame.setResizable(true);

    }

    public void showSettings() {
        cardLayout.show(frame.getContentPane(), "settings");
    }

}
