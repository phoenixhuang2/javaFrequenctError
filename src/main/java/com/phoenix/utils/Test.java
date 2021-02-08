package com.phoenix.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Predicate;


/**
 * 行为参数化
 * 一个方法接受多个不同行为作为参数，并在内部使用它们， 完成不同行为的能力。
 */
public class Test {
    public static void main(String[] args) {
        //接口实现类的实例
        List<Apple> appleList = new ArrayList<>();
        Filter filter  = new Filter();
        List<Apple> list = filter.filterApple(appleList, new AppleRedAndHeavyPredicate());

        PrintApple printApple = new PrintApple();
        printApple.prettyPrintApple(appleList, new AppleFancyFormatter());

        //匿名类
        printApple.prettyPrintApple(appleList, new AppleInfoPredicate() {
            @Override
            public String appleInfo(Apple apple) {
                return "a apple color:" + apple.getColor() + " weight:"+apple.getWeight();
            }
        });

        //Lambda表达式
        filter.filterApple(appleList, (Apple apple) -> Color.RED.equals(apple.getColor()));

        //方法引用版本
        Predicate<Apple> s = (Apple apple)->apple.getWeight()>140;
        filter.filterDemo(appleList, (Predicate) filter.filterDemo(appleList,  s));
//        filter.filterUtil(Arrays.asList(1,2,4,5,6,7), (com.phoenix.utils.Predicate) s);
        filter.filterDemo(appleList,  s);

        filter.filterSup(Apple::new);

        filter.consumer(new Apple(), System.out::println);


    }

    /**
     * lambda comparator
     */
    public static void testComparator() {
        List<Apple> list = new ArrayList<>();
        list.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
    }


    public static void testRunnable() {
        new Thread(() -> {
            System.out.println("hello world");
        }).start();
    }

    /**
     * Callable lambda
     */
    public static void testCallable() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> threadName = executorService.submit(() -> Thread.currentThread().getName());
    }


}
