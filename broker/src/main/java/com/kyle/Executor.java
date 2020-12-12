import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Executor {
    public static void mains(String[] args) {
        Executor exe = new Executor();
        ExecutorService executori = Executors.newCachedThreadPool();
        executori.submit(exe.yTask());
    }

    public Future<void> yTask(){
        System.out.println("Triggered");
    }

    public Future<Integer> calculate(Integer input) {        
        return executor.submit(() -> {
            Thread.sleep(1000);
            return input * input;
        });
    }
}
