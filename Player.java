public abstract class Player implements Runnable {
    private Thread thread;
    private ShotsQueue shotsQueue;
    public Player(ShotsQueue shotsQueue) {
        thread = new Thread(this);
        this.shotsQueue = shotsQueue;
    }

    @Override
    public void run() {
        while (true) {
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
