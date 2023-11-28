import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsController implements ActionListener {

    public SettingsController(Viewer viewer, Model model) {

    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        System.out.println("In Settings Controller " + command);
    }
}
