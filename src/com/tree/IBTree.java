package com.tree;

import java.util.concurrent.RecursiveTask;

/**
 * Created by calc on 25.03.14.
 * hexlet
 */
public interface IBTree<T extends Comparable<T>> {

    public IBTree getLeft();

    public IBTree getRight();

    public T getValue();

    public int getCount();

    public void add(T value);

    public void forEach(Process<T> process);

    public RecursiveTask<Integer> forEachFJ(Process<T> process);

    public void printAll();

    //функциональный интерфейс, пределяем что будем делать с деревом
    public interface Process<T extends Comparable<T>> {
        public void process(T value);
    }
}
