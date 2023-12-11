import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

import java.awt.Image;
import java.io.File;

public class Model {

    private Viewer viewer;
    private FieldGenerator fieldGenerator;
    private volatile boolean isUserTurn;
    private volatile boolean isGameStopped;
    private User user;
    private Computer computer;
    private GameLogic gameLogic;
    private final Object lock;
    private int x;
    private int y;
    private Music shotSound;
    private Music successShotSound;
    private Music waterShotSound;
    private Music killedShipSound;
    private Music backgroundMusic;
    private Cell[][] userBoardArray;
    private Cell[][] enemyBoardArray;
    private final Cell enemyBoard;
    private final Cell exitButton;
    private final Cell restartButton;
    private final Cell startButton;
    private final Cell stopButton;
    private final Cell soundButton;
    private final Image soundOnBtnImage;
    private final Image soundOffBtnImage;

    public Model(Viewer viewer) {
        this.viewer = viewer;
        x = -1;
        y = -1;

        fieldGenerator = new FieldGenerator();
        userBoardArray = fieldGenerator.getGeneratedField(50, 100);
        enemyBoardArray = fieldGenerator.getGeneratedField(650, 100);

        enemyBoard = new Cell(650, 100, 10 * 50, 10 * 50, 0);
        exitButton = new Cell(100, 620, 100, 50, 0);
        restartButton = new Cell(250, 620, 100, 50, 0);
        stopButton = new Cell(400, 620, 100, 50, 0);
        startButton = new Cell(500, 300, 200, 100, 0);
        startButton.setVisible(false);
        soundButton = new Cell(1080, 600, 70, 80, 0);
        soundOnBtnImage = new ImageIcon("images/soundOn.png").getImage();
        soundOffBtnImage = new ImageIcon("images/soundOff.png").getImage();
        soundButton.setImage(soundOnBtnImage);

        shotSound = new Music(new File("music/shotSound.wav"));
        shotSound.setVolume(0.8f);
        successShotSound = new Music(new File("music/succesShot.wav"));
        successShotSound.setVolume(0.8f);
        waterShotSound = new Music(new File("music/waterShot.wav"));
        waterShotSound.setVolume(0.8f);
        killedShipSound = new Music(new File("music/KilledShipSound.wav"));
        killedShipSound.setVolume(0.8f);
        backgroundMusic = new Music(new File("music/backgroundMusic2.wav"));
        backgroundMusic.setVolume(0.8f);
        backgroundMusic.playLoop();

        lock = new Object();
        startThreads();
    }

    public Music getShotSound() {
        return shotSound;
    }

    public Music getSuccessShotSound() {
        return successShotSound;
    }

    public Music getWaterShotSound() {
        return waterShotSound;
    }

    public Music getKilledShipSound() {
        return killedShipSound;
    }

    public Cell getSoundButton() {
        return soundButton;
    }

    private void startThreads() {
        ShotsQueue shotsQueue = new ShotsQueue(1);
        user = new User(this, shotsQueue);
        computer = new Computer(this, shotsQueue);
        gameLogic = new GameLogic(this, shotsQueue);

        user.start();
        computer.start();
        gameLogic.start();
    }

    private void stopThreads() {
        user.stop();
        computer.stop();
        gameLogic.stop();
    }

    public void doAction(int x, int y) {
        this.x = x;
        this.y = y;

        int userShipsNumber = gameLogic.getUserShipsNumber();
        int computerShipsNumber = gameLogic.getComputerShipsNumber();

        if (enemyBoard.contains(x, y) && startButton.isVisible()) {
            if (userShipsNumber > 0 && computerShipsNumber > 0 && isUserTurn) {
                if (!isShotValid()) {
                    return;
                }
                makeUserShot();
            }
        }

        if (startButton.contains(x, y)) {
            startGame();
        } else if (restartButton.contains(x, y) && startButton.isVisible()) {
            restartGame();
        } else if (stopButton.contains(x, y)) {
            stopGame();
        } else if (exitButton.contains(x, y)) {
            stopThreads();
            System.exit(0);
        } else if (soundButton.contains(x, y)) {
            if (soundButton.getImage().equals(soundOnBtnImage)) {
                soundButton.setImage(soundOffBtnImage);
                viewer.update();
                backgroundMusic.stop();
            } else {
                soundButton.setImage(soundOnBtnImage);
                viewer.update();
                backgroundMusic.playLoop();
            }
        }
    }

    private void startGame() {
        startButton.setVisible(true);
        isUserTurn = true;
        viewer.update();
    }

    private void restartGame() {
        userBoardArray = fieldGenerator.getGeneratedField(50, 100);
        enemyBoardArray = fieldGenerator.getGeneratedField(650, 100);
        gameLogic.updateShipsNumber();
        isUserTurn = true;
        computer.reset();
        viewer.update();
    }

    private void stopGame() {
        isGameStopped = !isGameStopped;
        viewer.update();
        if (!isGameStopped && !isUserTurn) {
            synchronized (lock) {
                lock.notifyAll();
            }
        }
    }

    private void makeUserShot() {
        synchronized (lock) {
            if (isShotValid() && !isGameStopped) {
                lock.notify();
            }
        }
    }

    private boolean isShotValid() {
        int indexY = (y - 100) / 50;
        int indexX = (x - 650) / 50;
        Cell shottedCell = enemyBoardArray[indexY][indexX];
        return shottedCell.getValue() < 2;
    }

    public boolean isGameStopped() {
        return isGameStopped;
    }

    public boolean isUserTurn() {
        return isUserTurn;
    }

    public void setUserTurn(boolean userTurn) {
        isUserTurn = userTurn;
    }

    public Object getLock() {
        return lock;
    }

    public Cell[][] getUserBoardArray() {
        return userBoardArray;
    }

    public Cell[][] getEnemyBoardArray() {
        return enemyBoardArray;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void playRocketAnimation(int x, int y) {
        ImageIcon rocketIcon = new ImageIcon("images/missile.png");
        JLabel rocketLabel = new JLabel(rocketIcon);
        viewer.addRocket(rocketIcon, x, y);
        Timer timer = new Timer(1000000, e -> {
            viewer.removeRocket(rocketLabel);
            viewer.update();
        });

        timer.setRepeats(false);
        timer.start();
    }

    public void viewerUpdate() {
        viewer.update();
    }

    public Cell getStartButton() {
        return startButton;
    }

    public int getUserShipsNumber() {
        return gameLogic.getUserShipsNumber();
    }

    public int getComputerShipsNumber() {
        return gameLogic.getComputerShipsNumber();
    }

}
