import com.tree.BTree;
import com.tree.IBTree;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by calc on 31.03.14.
 * hexlet
 */
public class Main {

    private static final int MAX_VALUE = 3000000;
    private static final int TEST_FIND_VALUE = MAX_VALUE/2-2;

    private static final int TIMEOUT = 30;
    private static final int N_THREADS = 4; // 4 - оптимально

    private static final List<String> profiler = new ArrayList<>();

    private static void fillTreeByArray(IBTree<Integer> bTree){
        int[] values = { 1, 5, 6, 7, 88, 4, 3, 5, 7, 9};
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

        System.out.println("start fill com");
        time = System.nanoTime();
        fillTreeByRandom(bTree);        //многомиллионное дерево
        //fillTreeByArray(bTree);       //предопределенный массив
        outExecTime(time, "stop fill com");

        System.out.println("start find");
        time = System.nanoTime();

        IBTree<Integer> foundNode = bTree.search(TEST_FIND_VALUE);
        outExecTime(time, "stop find");

        /*System.out.println("print found tree start");
        time = System.nanoTime();

        if(foundNode != null)
            foundNode.printAll();
        else System.out.println("Ничавошеньки не нашли");

        outExecTime(time, "stop print found tree");
        System.out.println("print AllTree");

        bTree.printAll();*/

        System.out.println("end");

        //статистика
        System.out.println("Мы искази значение " + TEST_FIND_VALUE);
        if(foundNode != null) System.out.println("нод был найден " + foundNode + " value: " + foundNode.getValue());
        else System.out.println("Random нам не улыбнулся");
        for(String s : profiler)
            System.out.println(s);
    }
}
