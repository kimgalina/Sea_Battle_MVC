public class Computer extends Player {

    private Model model;
    
    public Computer(Model model, ShotsQueue shotsQueue) {
        super(shotsQueue);
        this.model = model;
    }

    @Override
    public void makeShot() {
        Shot shot = generateShot();
        try {
            getShotsQueue().add(shot);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Shot generateShot() {
        return new Shot(0, 0, PlayerType.COMPUTER); //logic of computer's shot generation
    }
}
