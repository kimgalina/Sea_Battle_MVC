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
        if (shots.size() == this.size) {
            wait();
        }

        shots.add(shot);
        notifyAll();
    }

    public synchronized Shot remove() throws InterruptedException {
        if (shots.isEmpty()) {
            wait();
        }
        Shot shot = shots.poll();

        notifyAll();
        return shot;
    }
}
