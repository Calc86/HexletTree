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

    public IBTreeForkJoinFinder<T> search(T value);

    public void printAll();

    //функциональный интерфейс, пределяем что будем делать с деревом
    public interface Process<V extends Comparable<V>> {
        public void process(V value);
    }

    public class IBTreeForkJoinFinder<T extends Comparable<T>> extends RecursiveTask<IBTree<T>> {
        private final T findValue;
        private final IBTree<T> node;

        IBTreeForkJoinFinder(IBTree<T> node, T findValue) {
            this.findValue = findValue;
            this.node = node;
        }

        @Override
        protected IBTree<T> compute() {
            if(node.getValue().equals(findValue))
                return node;

            IBTreeForkJoinFinder<T> fLeft = null;
            if(node.getLeft() != null){
                fLeft = new IBTreeForkJoinFinder<T>(node.getLeft(),  findValue);
                fLeft.fork();
            }

            IBTreeForkJoinFinder<T> fRight = null;
            if(node.getRight() != null){
                fRight = new IBTreeForkJoinFinder<T>(node.getRight(),  findValue);
                fRight.fork();
            }

            IBTree<T> found;
            if(fLeft != null && (found = fLeft.join()) != null) return found;
            if(fRight != null && (found = fRight.join()) != null) return found;

            return null;
        }
    }
}


