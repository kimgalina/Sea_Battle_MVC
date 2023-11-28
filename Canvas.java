import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;

public class Canvas extends JPanel {
    private Model model;
    private Font font;

    public Canvas(Model model) {
        this.model = model;
        setBackground(Color.BLUE); // Цвет фона поля
        font = new Font("Impact", Font.PLAIN, 50);
    }

    public void paint(Graphics g) {
        super.paint(g);

        int cellSize = 50; // Размер клетки поля

        // Отрисовка игрового поля
        for (int row = 0; row < model.getBoardSize(); row++) {
            for (int col = 0; col < model.getBoardSize(); col++) {
                int x = col * cellSize;
                int y = row * cellSize;

                // Отрисовка клетки
                g.setColor(Color.WHITE);
                g.fillRect(x, y, cellSize, cellSize);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, cellSize, cellSize);

                // Пример отображения содержимого клетки (здесь можно добавить корабли и т.д.)
                g.setColor(Color.RED);
                if (model.isCellOccupied(row, col)) {
                    g.fillOval(x + cellSize / 4, y + cellSize / 4, cellSize / 2, cellSize / 2);
                }
            }
        }

        // Отрисовка других элементов (например, координаты)
        int x = model.getX();
        int y = model.getY();
        g.setColor(Color.YELLOW);
        g.setFont(font);
        g.drawString("" + x + " " + y, x - 10, y - 35);

        Cell cell1 = model.getCell1();
        g.setColor(Color.RED);
        g.drawRect(cell1.getXCell(), cell1.getYCell(), cell1.getWidthCell(), cell1.getHeightCell());

        Cell cell2 = model.getCell2();
        g.setColor(Color.YELLOW);
        g.drawRect(cell2.getXCell(), cell2.getYCell(), cell2.getWidthCell(), cell2.getHeightCell());

        g.drawString("Rectangle1 " + cell1.getX() + " " + cell1.getY() + " " + cell1.getWidth() + " " + cell1.getHeight(), 300, 300);
    }
}
