import java.awt.Rectangle;
import java.awt.Image;

public class Cell extends Rectangle {

    private Image image;
    private int value;
    private Ship ship;
    private boolean isVisible;

    public Cell(int x, int y, int width, int height, int value) {
        super(x, y, width, height);
        this.value = value;
        isVisible = true;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
