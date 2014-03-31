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
}


