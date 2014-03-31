package com.tree;

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

    public IBTree<T> search(T searchValue);

    //функциональный интерфейс, пределяем что будем делать с деревом
    public interface Process<V extends Comparable<V>> {
        public void process(V value);
    }

    //функциональный интерфейс в котором у нас есть нода
    public interface ProcessEx<T extends Comparable<T>> {
        public int process(IBTree<T> node);
    }

    public int forEachEx(final ProcessEx<T> processEx);

    public int getChildCount();
    public int getTreeCounts();
}


