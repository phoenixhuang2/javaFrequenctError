package com.phoenix.sync;

public class Interesting {
    private volatile int a = 1;
    private volatile int b = 1;

    public void add() {
        System.out.println("add start");
        for (int i=0; i<10000; i++) {
            a++;
            b++;
        }
        System.out.println("add done");
    }

    public void compare() {
        System.out.println("compare start");
        for (int i=0; i<10000; i++) {
            //会出现a=b的情况，此时a>b 还输出false
            if (a<b) {
                System.out.println("a:"+a+" b:"+b);
                System.out.println(a>b);
            }
        }
        System.out.println("compare done");
    }


    public static void main(String[] args) {
        //加锁版本在interesting2中
        Interesting interesting = new Interesting();
        new Thread(() -> interesting.add()).start();
        new Thread(() -> interesting.compare()).start();
    }
}
