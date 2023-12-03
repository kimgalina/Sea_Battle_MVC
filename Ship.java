import javax.swing.ImageIcon;
import java.awt.Image;

public class Ship {

    private int health;
    private Image image;
    private Image imageSharped;

    public Ship(int health) {
        this.health = health;
        image = new ImageIcon("images/1ship-1.png").getImage();
        imageSharped = new ImageIcon("images/1ship-1-sharped.png").getImage();
    }

    public void minusHealth() {

        if(health == 0 ){
            //call some method to do

        }
    }

    public Image getImage() {
        return image;
    }

    public Image getImageSharped() {
        return imageSharped;
    }
}
