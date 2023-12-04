public class Ship {

    private int health;
    private boolean isHorizontal;
    private Cell[] cells;

    public Ship(int health) {
        this.health = health;
    }
    public Ship(int health, boolean isHorizontal) {
        this(health);
        this.isHorizontal = isHorizontal;
    }


    public Ship(int health, boolean isHorizontal, Cell[] cells) {
        this(health, isHorizontal);
        this.cells = cells;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }


    public Cell[] getCells() {
        return cells;
    }

    public void setCells(Cell[] cells) {
        this.cells = cells;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public void setHorizontal(boolean horizontal) {
        isHorizontal = horizontal;
    }
}
