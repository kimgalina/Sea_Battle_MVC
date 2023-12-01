import java.awt.Rectangle;

public class Cell extends Rectangle {

    private String name;


    public Cell(String name, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.name = name;
    }
}
