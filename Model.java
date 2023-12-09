import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

import java.awt.Image;
import java.io.File;

public class Model {

    private Viewer viewer;
    private FieldGenerator fieldGenerator;
    private volatile boolean isUserTurn;
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

        lock = new Object();
        enemyBoard = new Cell(650, 100, 10 * 50, 10 * 50, 0);
        exitButton = new Cell(100, 620, 100, 50, 0);
        restartButton = new Cell(250, 620, 100, 50, 0);
        stopButton = new Cell(400, 620, 100, 50, 0);
        startButton = new Cell(500, 300, 200, 100, 0);
        startButton.setVisible(false);
        soundButton = new Cell(1080,600,70,80,0);
        soundOnBtnImage = new ImageIcon("images/soundOn.png").getImage();
        soundOffBtnImage = new ImageIcon("images/soundOff.png").getImage();
        soundButton.setImage(soundOnBtnImage);

        shotSound = new Music(new File("music/shotSound.wav"));
        successShotSound = new Music(new File("music/succesShot.wav"));
        waterShotSound = new Music(new File("music/waterShot.wav"));
        killedShipSound = new Music(new File("music/KilledShipSound.wav"));
        backgroundMusic = new Music(new File("music/backgroundMusic.wav"));
        backgroundMusic.playLoop();
        isUserTurn = true;
        startGame();
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
    private void startGame() {
        ShotsQueue shotsQueue = new ShotsQueue(1);
        user = new User(this, shotsQueue);
        computer = new Computer(this, shotsQueue);
        gameLogic = new GameLogic(this, shotsQueue);

        user.start();
        computer.start();
        gameLogic.start();
    }

    public void doAction(int x, int y) {
        this.x = x;
        this.y = y;

        int userShipsNumber = gameLogic.getUserShipsNumber();
        int computerShipsNumber = gameLogic.getComputerShipsNumber();
        System.out.println("user ship number " + userShipsNumber + "computer ship number " + computerShipsNumber);

        if (enemyBoard.contains(x, y) && startButton.isVisible()) {
             if (userShipsNumber > 0 && computerShipsNumber > 0) {
                 if (!isShotValid()) {
                     System.out.println("Invalid shot!");
                     return;
                 }
                 makeUserShot();
             } else {
                 System.out.println("The game is OVER");
             }
        }

        if (startButton.contains(x, y)) {
            startButton.setVisible(true);
            viewer.update();
        } else if (restartButton.contains(x, y) && startButton.isVisible()) {
            userBoardArray = fieldGenerator.getGeneratedField(50, 100);
            enemyBoardArray = fieldGenerator.getGeneratedField(650, 100);
            gameLogic.updateShipsNumber();
            computer.reset();
            viewer.update();
        } else if (stopButton.contains(x,y)) {
            System.out.println("Something do for stop or pause game");
        } else if (exitButton.contains(x, y)) {
            user.stop();
            computer.stop();
            gameLogic.stop();
            System.exit(0);
        } else if (soundButton.contains(x,y)) {
            if(soundButton.getImage().equals(soundOnBtnImage)) {
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

    private void makeUserShot() {
        synchronized (lock) {
            if (isShotValid() && isUserTurn) {
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

    public boolean validateBattlefield(Cell[][] field) {
        if (field.length != 10 || field[0].length != 10) {
            return false;
        }

        int[] shipsCount = {4, 3, 2, 1};
        boolean[][] visited = new boolean[10][10];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int cellValue = field[i][j].getValue();

                if (cellValue != 0 && cellValue != 1) {
                    return false;
                }

                if (cellValue == 1 && !visited[i][j]) {
                    int shipSizeHorizontal = getShipSize(field, visited, i, j, true);
                    int shipSizeVertical = getShipSize(field, visited, i, j, false);
                    int shipSize = Math.max(shipSizeHorizontal, shipSizeVertical);

                    if (shipSize > 4 || (shipSizeHorizontal != 1 && shipSizeVertical != 1)) {
                        return false;
                    }

                    if (!isValidPosition(field, i, j, shipSizeHorizontal, shipSizeVertical)) {
                        return false;
                    }

                    shipsCount[shipSize - 1]--;
                }
            }
        }

        for (int count : shipsCount) {
            if (count != 0) {
                return false;
            }
        }

        return true;
    }

    private int getShipSize(Cell[][] field, boolean[][] visited, int i, int j, boolean isHorizontal) {
        int size = 1;

        if (isHorizontal) {
            while (j + size < 10 && field[i][j + size].getValue() == 1) {
                visited[i][j + size] = true;
                size++;
            }
        } else {
            while (i + size < 10 && field[i + size][j].getValue() == 1) {
                visited[i + size][j] = true;
                size++;
            }
        }

        return size;
    }

    private boolean isValidPosition(Cell[][] field, int i, int j, int shipSizeHorizontal, int shipSizeVertical) {
        if (i - 1 >= 0) {
            if (j - 1 >= 0 && field[i - 1][j - 1].getValue() == 1) {
                return false;
            } else if (j + shipSizeHorizontal < 10 && field[i - 1][j + shipSizeHorizontal].getValue() == 1) {
                return false;
            }
        } else if (i + shipSizeVertical < 10) {
            if (j - 1 >= 10 && field[i + shipSizeVertical][j - 1].getValue() == 1) {
                return false;
            } else if (j + shipSizeHorizontal < 10 && field[i + shipSizeVertical][j + shipSizeHorizontal].getValue() == 1) {
                return false;
            }
        }
        return true;
    }


    public void playRocketAnimation(int x, int y) {
        ImageIcon rocketIcon = new ImageIcon("images/missile.png");
        JLabel rocketLabel = new JLabel(rocketIcon);
        viewer.addRocket(rocketIcon, x, y); // добавляем ракету на панель с указанными координатами

        // Для задержки перед удалением ракеты, чтобы она успела отобразиться
        Timer timer = new Timer(1000000, e -> {
            viewer.removeRocket(rocketLabel); // удаляем ракету с панели
            viewer.update(); // обновляем представление для отображения изменений
        });

        timer.setRepeats(false); // Устанавливаем повторение таймера только один раз
        timer.start(); // запускаем таймер для удаления ракеты через 1 секунду
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
