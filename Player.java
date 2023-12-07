public abstract class Player implements Runnable {
    private Thread thread;
    private ShotsQueue shotsQueue;
    private boolean isRunning;

    public Player(ShotsQueue shotsQueue) {
        thread = new Thread(this);
        this.shotsQueue = shotsQueue;
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
            doAction();
        }
    }

    public abstract void doAction();

    public ShotsQueue getShotsQueue() {
        return shotsQueue;
    }
}
