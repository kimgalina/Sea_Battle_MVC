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
            if (model.isUserTurn()) {
                printComputerBoard();
                produceShot();
            }
        }
    }

    private void printComputerBoard() {
        System.out.println("\nCOMPUTER");
        Cell[][] board = model.getEnemyBoardArray();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j].getValue());
            }
            System.out.println();
        }
    }

    private void waitForClick() {
        try {
            lock.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void produceShot() {
        Shot shot = parseToShot(model.getX(), model.getY());
        ShotsQueue shots = getShotsQueue();
        try {
            shots.add(shot);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Shot parseToShot(int x, int y) {
        int i = (y - 100) / 50;
        int j = (x - 650) / 50;
        return new Shot(j, i, PlayerType.USER);
    }
}
