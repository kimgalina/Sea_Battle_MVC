import javax.swing.ImageIcon;

public class GameLogic implements Runnable {

    private Model model;
    private Thread thread;
    private ShotsQueue shotsQueue;
    private boolean isRunning;
    private final int EMPTY = 0;
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
            handleAction();
        }
    }

    private void handleAction() {
        Shot shot = consumeShot();
        if (shot != null) {
            if (shot.getPlayerType() == PlayerType.USER) {
                boolean isShipHit = processUserShot(shot);
                if (isShipHit) {
                    // user's turn
                }
            } else {
                boolean isShipHit = processComputerShot(shot);
                if (isShipHit) {
                    // computer's turn
                    model.viewerUpdate();
                }
            }
        }
    }

    private Shot consumeShot() {
        try {
            return shotsQueue.remove();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean processUserShot(Shot shot) {
        return processShot(model.getEnemyBoardArray(), shot);
    }

    private boolean processComputerShot(Shot shot) {
        return processShot(model.getUserBoardArray(), shot);
    }

    private boolean processShot(Cell[][] board, Shot shot) {
        Cell shottedCell = board[shot.getY()][shot.getX()];

        if (shottedCell.getValue() == SHIP) {
            shottedCell.setValue(HIT_SHOT);
            String imagePath = shottedCell.getImagePath();
            String sharpedImagePath = imagePath.substring(0, imagePath.length() - 4) + "-sharped.png";
            shottedCell.setImage(new ImageIcon(sharpedImagePath).getImage());

            if (shottedCell.isVisible()) {
                shottedCell.setVisible(false);
            }
            return true;
        } else {
            shottedCell.setValue(MISS_SHOT);
            if (shottedCell.isVisible()) {
                shottedCell.setVisible(false);
            }
            return false;
        }
    }
}
