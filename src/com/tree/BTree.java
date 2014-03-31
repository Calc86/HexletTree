package com.tree;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by calc on 25.03.14.
 * hexlet
 */
public class BTree<T extends Comparable<T>> implements IBTree<T> {

    private IBTree<T> left = null;
    private IBTree<T> right = null;
    private final T value;
    private AtomicInteger count = new AtomicInteger(1);

    public BTree(T value) {
        this.value = value;
    }

    @Override
    public IBTree getLeft() {
        return left;
    }

    @Override
    public IBTree getRight() {
        return right;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public int getCount() {
        return count.get();
    }

    @Override
    public void add(T value) {
        if(value.equals(getValue()))
            count.incrementAndGet();
        else if(value.compareTo(getValue()) < 0)
            addLeft(value);
        else
            addRight(value);
    }

    private void addRight(T value) {
        if(getRight() == null)
            setRight(new BTree<>(value));
        else
            getRight().add(value);
    }


    private void addLeft(T value){
        if (getLeft() == null)
            setLeft(new BTree<>(value));
        else
            getLeft().add(value);
    }

    private void setLeft(IBTree<T> left) {
        this.left = left;
    }

    private void setRight(IBTree<T> right) {
        this.right = right;
    }

    @Override
    public void forEach(final Process<T> process) {
        // запускаем процесс для нашего нода
        new Thread() {
            @Override
            public void run() {
                process.process(getValue());
            }
        }.start();
        //запускаем процесс для дочерних нодов
        if(getLeft() != null)
            getLeft().forEach(process);
        if(getRight() != null)
            getRight().forEach(process);
    }

    @Override
    public RecursiveTask<Integer> forEachFJ(final Process<T> process) {
        RecursiveTask<Integer> rt = new RecursiveTask<Integer>() {
            @Override
            protected Integer compute() {
                process.process(getValue());

                return 0;
            }
        };

        // создаем 2 форка
        RecursiveTask<Integer> rt_left = null;
        RecursiveTask<Integer> rt_right = null ;
        if(getLeft() != null){
            rt_left = getLeft().forEachFJ(process);
            rt_left.fork();
        }
        if(getRight() != null){
            rt_right = getRight().forEachFJ(process);
            rt_right.fork();
        }

        if(rt_left != null) rt_left.join();
        if(rt_right != null) rt_right.join();

        return rt;
    }

    @Override
    public void printAll() {
        System.out.println(value + ":" + count);
        if(getLeft() != null)
            getLeft().printAll();
        if(getRight() != null)
            getRight().printAll();
    }
}