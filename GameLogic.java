public class GameLogic implements Runnable {

    private Model model;
    private Thread thread;
    private ShotsQueue shotsQueue;
    private boolean isRunning;
    private final int SHIP = 1;
    private final int HIT_SHOT = 2;
    private final int MISS_SHOT = 3;

    public GameLogic(Model model, ShotsQueue shotsQueue) {
        this.model = model;
        this.shotsQueue = shotsQueue;
        thread = new Thread(this);
    }

    public void start() {
        thread.start();
    }

    public void stop() {
        isRunning = false;
    }

    public void run() {
        isRunning = true;
        while (isRunning) {
            consumeShot();
        }
    }

    private void consumeShot() {
        try {
            Shot shot = shotsQueue.remove();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean handleShot(Cell[][] field, Shot shot) {
        Cell shottedCell = field[shot.getY()][shot.getX()];

        if (shottedCell.getValue() == SHIP) {
            shottedCell.setValue(HIT_SHOT);
            return true;
        } else {
            shottedCell.setValue(MISS_SHOT);
            return false;
        }
    }
}
