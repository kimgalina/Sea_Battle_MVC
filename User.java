public class User extends Player {

    private final Object lock;
    private Model model;

    public User(Model model, ShotsQueue shotsQueue) {
        super(shotsQueue);
        this.model = model;
        lock = model.getLock();
    }

    public void doAction() {
        synchronized (lock) {
            waitForClick();
            produceShot();
            lock.notify();
        }
    }

    public void waitForClick() {
        try {
            lock.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void produceShot() {
        int y = (model.getY() - 100) / 50;
        int x = (model.getX() - 650) / 50;
        Shot shot = new Shot(x, y, PlayerType.USER);
        ShotsQueue shots = getShotsQueue();
        try {
            shots.add(shot);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
