import java.awt.Rectangle;

public class Cell extends Rectangle {

    private String name;
    private Ship ship;
    private int value;
    private boolean state;


    public Cell(int x, int y, int width, int height, int value, Ship ship) {
        super(x, y, width, height);
        this.ship = ship;
        this.value = value;
        state = true;
    }

    public int getValue() {
        return value;
    }

    public Ship getShip() {
        return ship;
    }

    public boolean isState() {
        return state;
    }

    public void setState() {
        state = !state;
    }
}
