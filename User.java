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

            int x = model.getX();
            int y = model.getY();
            Shot shot = parseToShot(x, y);

            if (isShotValid(shot)) {
                produceShot(shot);
                lock.notify();
            }
        }
    }

    private void waitForClick() {
        try {
            lock.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Shot parseToShot(int x, int y) {
        int i = (y - 100) / 50;
        int j = (x - 650) / 50;
        return new Shot(j, i, PlayerType.USER);
    }

    private boolean isShotValid(Shot shot) {
        Cell[][] computerBoard = model.getEnemyBoardArray();
        Cell shottedCell = computerBoard[shot.getY()][shot.getX()];

        return shottedCell.getValue() < 2;
    }

    private void produceShot(Shot shot) {
        ShotsQueue shots = getShotsQueue();
        try {
            shots.add(shot);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
