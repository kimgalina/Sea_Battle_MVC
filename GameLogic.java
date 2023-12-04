public class GameLogic implements Runnable {
  
    private Thread thread;
    private ShotsQueue shotsQueue;

    public GameLogic(ShotsQueue shotsQueue) {
        this.shotsQueue = shotsQueue;
        thread = new Thread(this);
    }

    public void start() {
        thread.start();
    }

    @Override
    public void run() {
        processShot();
    }

    private void processShot() {
        while (true) {
            try {
                Shot shot = shotsQueue.remove();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
