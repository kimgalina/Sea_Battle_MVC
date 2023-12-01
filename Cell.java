import java.awt.Rectangle;

public class Cell extends Rectangle {

    private String name;
    private int value;


    public Cell(String name, int x, int y, int width, int height, int value) {
        super(x, y, width, height);
        this.name = name;
        this.value = value;
    }
}
