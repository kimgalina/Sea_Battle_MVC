import javax.swing.JFrame;
import java.awt.CardLayout;

public class Viewer {
    private Controller controller;
    private Canvas canvas;
    private JFrame frame;
    private CardLayout cardLayout;

    public Viewer() {

        cardLayout = new CardLayout();

        controller = new Controller(this);
        Model model = controller.getModel();
        canvas = new Canvas(model);
        Menu menu = new Menu(this, model);

        frame = new JFrame("Battleship");
        frame.setSize(1200,720);
        frame.setLocation(100,15);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(cardLayout);

        frame.add(menu, "menu");
        frame.setVisible(true);
        frame.setResizable(true);

    }
}
