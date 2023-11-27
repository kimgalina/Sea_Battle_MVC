public class Viewer {
    private Controller controller;
    private Canvas canvas;
    public Viewer() {
        controller = new Controller(this);
        canvas = new Canvas(controller.getModel());
    }
}
