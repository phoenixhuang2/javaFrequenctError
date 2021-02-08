package com.phoenix.java8;

import com.phoenix.utils.Apple;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import static java.util.Comparator.comparing;

import java.util.Comparator;
import java.util.List;
import java.util.function.*;

/**
 * lambda表达式：可传递匿名函数，没有方法名称，但是有参数列表，函数主体，返回类型
 */
public class LambdaDemo {
    public static void demo1() {
//        (String s) -> s.length();

//        (Apple a) -> a.getWeight() > 150;

//        (int x, int y) -> {
//            System.out.println("Result");
//            System.out.println(x+y);
//        };

//        ()->42;

//        (Apple a1, Apple a2)->a1.getWeight().compareTo(a2.getWeight());

//        (Integer i) -> {return "Alan" + i; };
    }

    public static void demo2() {
        Runnable r1 = () -> System.out.println("hello world1");
        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                System.out.println("hello world2");
            }
        };
        process(r1);
        process(r2);
        process(()-> {
            System.out.println("hello world3");
        });
    }

    public static void process(Runnable r) {
        r.run();
    }

    /**
     * java.util.function 函数式接口
     * @param t
     * @param consumer
     */
    public static void consumerDemo(Integer t, Consumer<Integer> consumer) {
        consumer.accept(t);
    }

    public static void demo3() {
        consumerDemo(10, (Integer t)->{
            System.out.println("integer i :"+t);
        });
    }


    public static void demo4() {
        process(()-> System.out.println("hello world"));
        process(()->{
            System.out.println("hello world2");
        });
    }

    public static void main(String[] args) {
        demo3();

//        try(FileInputStream fis = new FileInputStream(new File(""))) {
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 方法引用：lambda表达式的语法糖
     *
     * 如果一个Lambda代表的只是直接调用这个方法，那最好还是用名称来调用它，而不是去描述如何调用它。
     */
    public static void demo5() {
        process(System.out::println);

        List list = new ArrayList();
        //排序
        list.sort(comparing(Apple::getWeight));

        Function<Apple, Float> getWeight = Apple::getWeight;
        BiFunction<String, Integer, String> stringIntegerStringBiFunction = String::substring;
        Runnable runnable = System.out::println;

        Supplier<Apple> aNew = Apple::new;

    }


    /**
     * 谓词复合，函数复合
     */
    public static void demo6() {
        //谓词复合
        Predicate<Apple> predicate = (Apple apple) -> apple.getWeight()>140;
        Predicate<Apple> predicate1 = (Apple apple)-> Color.RED.equals(apple.getColor());
        predicate.negate();

        predicate.and(predicate1);
        predicate.or(predicate1);

        //函数复合
        Function<Apple, Float> function1 = Apple::getWeight;
        Function<Float, Float> function2 = (Float f) -> f % 2;
        Function<Apple, Float> function3 = function1.andThen(function2);
        function3.apply(new Apple());

        //比较器链
        List<Apple> inventory = new ArrayList<>();
        inventory.sort(Comparator.comparing(Apple::getWeight).reversed());
        inventory.sort(Comparator.comparing(Apple::getWeight).reversed().thenComparing(Apple::getColor));


    }
}
