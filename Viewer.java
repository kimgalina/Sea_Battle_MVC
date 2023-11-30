import javax.swing.JFrame;
import java.awt.CardLayout;

public class Viewer {
    private Controller controller;
    private Canvas canvas;
    private JFrame frame;
    private CardLayout cardLayout;

    public Viewer() {
        controller = new Controller(this);
        Model model = controller.getModel();
        canvas = new Canvas(model);

        cardLayout = new CardLayout();

        frame = new JFrame("Battleship");
        frame.setSize(1200, 720);
        frame.setLocation(100, 10);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(cardLayout);

    }

}
