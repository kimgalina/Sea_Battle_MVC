public class Shot {

    private int x;
    private int y;
    private PlayerType playerType;

    public Shot(int x, int y, PlayerType playerType) {
        this.x = x;
        this.y = y;
        this.playerType = playerType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
