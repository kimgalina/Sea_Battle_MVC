public class User extends Player {

    private Object lock;
    private Model model;

    public User(Model model, ShotsQueue shotsQueue) {
        super(shotsQueue);
        this.model = model;
        lock = model.getLock();
    }

    @Override
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
        Shot shot = new Shot(model.getX(), model.getY(), PlayerType.USER);
        ShotsQueue shots = getShotsQueue();
        try {
            shots.add(shot);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
