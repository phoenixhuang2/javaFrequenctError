package com.phoenix.sync;

import java.util.stream.IntStream;

public class Data2 {
    private static int counter = 0;
    private static Object lock = new Object();
    public static int reset() {
        counter = 0;
        return counter;
    }

    /**
     * 给静态变量加锁，所有实例共享
     */
    public void wrong() {
        synchronized (lock) {
            counter++;
        }
    }


    public static void main(String[] args) {
        IntStream.rangeClosed(1, 1000000).parallel().forEach(i->new Data2().wrong());
        System.out.println(Data2.counter);
    }
}
