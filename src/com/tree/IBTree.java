package com.tree;

import java.util.concurrent.RecursiveTask;

/**
 * Created by calc on 25.03.14.
 * hexlet
 */
public interface IBTree<T extends Comparable<T>> {

    public IBTree<T> getLeft();

    public IBTree<T> getRight();

    public T getValue();

    public int getCount();

    public void add(T value);

    public void forEach(Process<T> process);

    public void printAll();

    public ForkJoinFinder<T> getFinder(T findValue);

    public IBTree<T> search(T searchValue);

    //функциональный интерфейс, пределяем что будем делать с деревом
    public interface Process<V extends Comparable<V>> {
        public void process(V value);
    }

    public class ForkJoinFinder<T extends Comparable<T>> extends RecursiveTask<IBTree<T>> {
        private final T findValue;
        private final IBTree<T> node;

        //надо подумать по поводу нескольких поисков за раз...
        private static boolean found = false;   //флаг, ставится только вверх, вниз не может, по этому не thread safe

        ForkJoinFinder(IBTree<T> node, T findValue) {
            this.findValue = findValue;
            this.node = node;
        }

        @Override
        protected IBTree<T> compute() {
            if(node.getValue().equals(findValue)){
                found = true;
                return node;
            }

            if(found) return null; //кто то уже нашел до нас

            ForkJoinFinder<T> fLeft = null;
            if(node.getLeft() != null){
                fLeft = new ForkJoinFinder<T>(node.getLeft(),  findValue);
                fLeft.fork();
            }

            ForkJoinFinder<T> fRight = null;
            if(node.getRight() != null){
                fRight = new ForkJoinFinder<T>(node.getRight(),  findValue);
                fRight.fork();
            }

            IBTree<T> found;
            if(fLeft != null && (found = fLeft.join()) != null) return found;
            if(fRight != null && (found = fRight.join()) != null) return found;

            return null;
        }
    }
}


