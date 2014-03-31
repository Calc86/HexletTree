import com.tree.BTree;
import com.tree.IBTree;
import com.tree.IBTreeForkJoinFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by calc on 31.03.14.
 * hexlet
 */
public class Main {

    public static final int MAX_VALUE = 300;

    private static final ForkJoinPool FORK_JOIN_POOL = new ForkJoinPool();
    public static final int TIMEOUT = 5;
    public static final int N_THREADS = 100;

    private static List<String> profiler = new ArrayList<>();

    private static void fillTreeByArray(IBTree<Integer> bTree){
        int[] values = {1,5,6,7,88,4,3,5,7,9};
        for (int value : values) {
            bTree.add(value);
        }
    }

    private static void fillTreeByRandom(final IBTree<Integer> bTree){
        ExecutorService executor = Executors.newFixedThreadPool(N_THREADS);

        for (int i = 0; i < MAX_VALUE; i++) {
            Thread t = new Thread(){
                @Override
                public void run() {
                    bTree.add((int) (Math.random() * MAX_VALUE));
                }
            };
            executor.submit(t);
        }
        executor.shutdown();
        try {
            executor.awaitTermination(TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }
    }

    private static void outExecTime(long time, String message){
        String out = message + ": " + ((System.nanoTime() - time) / 1000000000f) + "s";
        profiler.add(out);
        System.out.println(out);
    }

    public static void main(String[] args) throws InterruptedException{

        long time;

        final IBTree<Integer> bTree = new BTree<>(MAX_VALUE/2);
        //final IBTree<Integer> bTree = new BTree<>(1);

        System.out.println("start fill com");
        time = System.nanoTime();
        //fillTreeByRandom(bTree);
        fillTreeByArray(bTree);
        outExecTime(time, "stop fill com");

        /*IBTree.Process<Integer> process = new IBTree.Process<Integer>() {
            @Override
            public void process(Integer value) {
                System.out.println(value);
            }
        };*/
        //find

        System.out.println("start find");
        time = System.nanoTime();
        IBTreeForkJoinFinder<Integer> rt = bTree.search(88);
        FORK_JOIN_POOL.submit(rt);
        FORK_JOIN_POOL.shutdown();
        FORK_JOIN_POOL.awaitTermination(TIMEOUT, TimeUnit.SECONDS);
        outExecTime(time, "stop find");

        IBTree<Integer> foundNode = (IBTree<Integer>) rt.join();

        System.out.println("print found tree start");
        time = System.nanoTime();
        //bTree.forEach(process);
        if(foundNode != null)
            foundNode.printAll();
        else System.out.println("Ничавошеньки не нашли");
        outExecTime(time, "stop print found tree");
        System.out.println("print AllTree");
        bTree.printAll();
        System.out.println("end");

        for(String s : profiler)
            System.out.println(s);
    }
}
