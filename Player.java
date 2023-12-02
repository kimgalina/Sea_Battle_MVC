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
            waitForTurn();
            makeShot();
        }
    }

    public void start() {
        thread.start();
    }

    public abstract void makeShot();

    public synchronized void waitForTurn() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void notifyTurn() {
        notify();
    }

    public ShotsQueue getShotsQueue() {
        return shotsQueue;
    }
}
