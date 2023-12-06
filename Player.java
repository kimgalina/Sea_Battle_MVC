public abstract class Player implements Runnable {
    private Thread thread;
    private ShotsQueue shotsQueue;
    private boolean isRunning;

    public Player(ShotsQueue shotsQueue, boolean isRunning) {
        thread = new Thread(this);
        this.shotsQueue = shotsQueue;
        this.isRunning = isRunning;
    }

    public void run() {
        while (isRunning) {
            doAction();
        }
    }

    public void start() {
        thread.start();
    }


    public abstract void doAction();

    public ShotsQueue getShotsQueue() {
        return shotsQueue;
    }
}
