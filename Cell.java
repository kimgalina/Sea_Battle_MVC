import java.awt.Rectangle;

public class Cell extends Rectangle {

    private String name;
    private Ship ship;
    private int value;


    public Cell(int x, int y, int width, int height, int value) {
        super(x, y, width, height);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Ship getShip() {
        return ship;
    }
}
