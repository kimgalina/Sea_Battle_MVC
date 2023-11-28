import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.ObjectOutputStream;

@SuppressWarnings("serial")
public class Menu extends JPanel {

    private JLabel label;
    private MenuController menuController;
    private JButton playButton;
    private JButton settingsButton;
    private JButton exitButton;

    int middleWidth;
    int height;

    public Menu(Viewer viewer, Model model) {

        menuController = new MenuController(viewer , model);
        label = new JLabel("Sea Battle");

        middleWidth= 0;
        height = 0;
        init();
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image backgroundImage = new ImageIcon("images/menuBackground.jpg").getImage();
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        middleWidth= (int) (getWidth() / 2.5);
        height = getHeight() / 20;

        label.setBounds( middleWidth , height , 500, 100);
        label.setForeground(Color.BLACK);
        playButton.setBounds(15, getHeight() - 200, 200,40);
        settingsButton.setBounds(15, getHeight() - 140, 200, 40);
        exitButton.setBounds(15, getHeight() - 80, 200, 40);

    }

    private void init() {
        setLayout(null);

        label.setBounds( middleWidth , height , 500, 100);
        label.setForeground(Color.BLACK);

        Font labelFont = new Font("Bookman Old Style", Font.BOLD, 50);
        label.setFont(labelFont);

        Font menuFont = new Font("Bookman Old Style", Font.BOLD, 20);

        playButton = createButton("Play", menuFont, "Play", 10, 480, 300, 40);
        settingsButton = createButton("Settings", menuFont, "Settings", 10, 540, 300, 40);
        exitButton = createButton("Exit", menuFont, "Exit", 10, 600, 300, 40);

        add(playButton);
        add(settingsButton);
        add(exitButton);
        add(label);

    }

    private JButton createButton(String text, Font font, String command, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x,y,200,40);
        button.setFocusable(false);
        button.setFont(font);
        button.setEnabled(true);
        button.setActionCommand(command);
        button.addActionListener(menuController);
        return button;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        throw new IOException("This class is NOT serializable.");
    }
}
