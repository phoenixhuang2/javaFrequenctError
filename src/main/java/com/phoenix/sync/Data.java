package com.phoenix.sync;

import java.util.stream.IntStream;

public class Data {
    private static int counter = 0;
    public static int reset() {
        counter = 0;
        return counter;
    }

    /**
     * counter是静态变量，所有实例共享的
     * wrong()方法是非静态方法；多个线程可能会执行不同的实例方法
     */
    public synchronized void wrong() {
        counter++;
    }


    public static void main(String[] args) {
        IntStream.rangeClosed(1, 1000000).parallel().forEach(i->new Data().wrong());
        System.out.println(Data.counter);
    }
}
