import java.util.LinkedList;
import java.util.Queue;

public class ShotsQueue {
    private Queue<Shot> shots;
    private final int size;

    public ShotsQueue(int size) {
        this.size = size;
        shots = new LinkedList<>();
    }

    public synchronized void add(Shot shot) throws InterruptedException {
        if (shots.size() == this.size){
            System.out.println("Buffer is full: waiting for consumption");
            wait();
        }
        System.out.println(shot.getPlayerType() + " inserted: " + shot);

        shots.add(shot);
        notifyAll();
    }

    public synchronized Shot remove() throws InterruptedException {
        if (shots.isEmpty()){
            System.out.println("Buffer is empty: waiting for production");
            wait();
        }
        Shot shot = shots.poll();
        System.out.println("RECEIVED: " + shot);

        notifyAll();
        return shot;
    }
}
