import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Color;

public class SplashScreen {

    JFrame frame;
    JProgressBar progressBar;

    public SplashScreen() {
        frame = new JFrame();
        frame.getContentPane().setLayout(null);
        frame.setUndecorated(true);
        frame.setSize(1200, 720);
        frame.setLocation(300, 100);
        frame.setLocationRelativeTo(null);
        JLabel backgroundImage = new JLabel(new ImageIcon("images/SplashBackground.png"));
        backgroundImage.setSize(1200, 720);

        frame.add(backgroundImage);
        frame.setVisible(true);

        addProgressBar();
        runningProgressBar();
    }

    private void addProgressBar() {
        progressBar = new JProgressBar();
        progressBar.setBounds(100, 620, 1000, 50);
        progressBar.setBorderPainted(true);
        progressBar.setStringPainted(true);
        progressBar.setBackground(Color.WHITE);
        progressBar.setForeground(Color.BLACK);
        progressBar.setValue(0);
        frame.add(progressBar);
    }

    public void runningProgressBar() {
        int i = 0;

        while (i <= 100) {
            try {
                Thread.sleep(50);
                progressBar.setValue(i);
                i += 2.5;
                if (i >= 100) {
                    frame.dispose();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}

