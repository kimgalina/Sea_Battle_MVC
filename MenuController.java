import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuController implements ActionListener {
    private Model model;
    private Viewer viewer;

    public MenuController(Viewer viewer, Model model) {
        this.viewer =viewer;
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        System.out.println(" In menu Controller command: " + command);
    }
}
