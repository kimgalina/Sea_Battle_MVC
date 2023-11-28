import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Color;
import java.awt.Image;
import java.awt.Graphics;

public class SettingsPanel extends JPanel {

    JLabel settingsLabel;

    public SettingsPanel(Viewer viewer, Model model) {
        SettingsController controller = new SettingsController(viewer, model);
        setLayout(null);

        settingsLabel = new JLabel("Settings");
        settingsLabel.setBounds(500,50,300,60);
        settingsLabel.setForeground(Color.WHITE);
        settingsLabel.setFont(new Font("Bookman Old Style", Font.BOLD, 50));
        add(settingsLabel);
    }

    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        Image backgroundImage = new ImageIcon("images/settingsBackground.jpg").getImage();
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);

    }
}
