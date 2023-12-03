public class Ship {

    private int health;
    private int isHorizontal;
    private Cell[] cells;

    public Ship() {

    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getIsHorizontal() {
        return isHorizontal;
    }

    public void setIsHorizontal(int isHorizontal) {
        this.isHorizontal = isHorizontal;
    }

    public Cell[] getCells() {
        return cells;
    }

    public void setCells(Cell[] cells) {
        this.cells = cells;
    }
}
