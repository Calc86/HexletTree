package com.tree;

import java.util.concurrent.ForkJoinPool;
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
    private final AtomicInteger count = new AtomicInteger(1);

    public BTree(T value) {
        this.value = value;
    }

    @Override
    public IBTree<T> getLeft() {
        return left;
    }

    @Override
    public IBTree<T> getRight() {
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
    public void printAll() {
        System.out.println(value + ":" + count);
        if(getLeft() != null)
            getLeft().printAll();
        if(getRight() != null)
            getRight().printAll();
    }

    private class Flag{
        public boolean isFound = false;
    }

    private class ForkJoinFinder<T extends Comparable<T>> extends RecursiveTask<IBTree<T>> {
        private final T findValue;
        private final IBTree<T> node;

        // тащим полотенцем
        private final Flag flag;

        ForkJoinFinder(IBTree<T> node, T findValue, Flag flag) {
            this.findValue = findValue;
            this.node = node;
            this.flag = flag;
        }

        private  ForkJoinFinder<T> startNodeFind(IBTree<T> node, T findValue){
            if(node == null) return null;

            ForkJoinFinder<T> fjf = new ForkJoinFinder<>(node,  findValue, flag);
            fjf.fork();

            return fjf;
        }

        @Override
        protected IBTree<T> compute() {
            if(node.getValue().equals(findValue)){
                flag.isFound = true;
                return node;
            }

            if(flag.isFound) return null; //кто то уже нашел до нас

            IBTree<T> found = null;

            ForkJoinFinder<T> f1 = startNodeFind(node.getLeft(), findValue);
            ForkJoinFinder<T> f2 = startNodeFind(node.getRight(), findValue);
            if(f1 != null) found = f1.join();
            if(found != null) return found;
            if(f2 != null) found = f2.join();

            return found;
        }
    }

    @Override
    public IBTree<T> search(T searchValue){
        final ForkJoinPool fjp = new ForkJoinPool();

        IBTree<T> found;

        Flag flag = new Flag();

        ForkJoinFinder<T> fjf = new ForkJoinFinder<>(this, searchValue, flag);
        fjp.submit(fjf);
        found = fjf.join();
        fjp.shutdown();
        //fjp.awaitTermination(secTimeout, TimeUnit.SECONDS);

        return found;
    }
}