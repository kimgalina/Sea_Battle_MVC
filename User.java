public class User extends Player {

    private Model model;

    public User(Model model, ShotsQueue shotsQueue) {
        super(shotsQueue);
        this.model = model;
    }

    @Override
    public void makeShot() {
        Shot shot = new Shot(model.getX(), model.getY(), PlayerType.USER);
        try {
            getShotsQueue().add(shot);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
