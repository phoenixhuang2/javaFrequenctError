package com.phoenix.sync;

/**
 * add 和compare都需要加锁
 * add 和 compare 会交错执行，a<b这种是非原子操作的
 */
public class Interesting2 {
    private volatile int a = 1;
    private volatile int b = 1;

    public synchronized void add() {
        System.out.println("add start");
        for (int i=0; i<10000; i++) {
            a++;
            b++;
        }
        System.out.println("add done");
    }

    public synchronized void compare() {
        System.out.println("compare start");
        for (int i=0; i<10000; i++) {
            if (a<b) {
                System.out.println("a:"+a+" b:"+b);
                System.out.println(a>b);
            }
        }
        System.out.println("compare done");
    }


    public static void main(String[] args) {
        Interesting2 interesting = new Interesting2();
        new Thread(() -> interesting.add()).start();
        new Thread(() -> interesting.compare()).start();
    }
}
