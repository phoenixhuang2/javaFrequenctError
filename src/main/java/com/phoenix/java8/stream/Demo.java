package com.phoenix.java8.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 流是从支持数据处理操作的源生成的元素序列
 *
 * 元素序列
 * 数据处理操作
 * 流水线
 *
 *
 * 链式中的方法调用都在排队等待，直到调用collect
 */
public class Demo {
    public static void main(String[] args) {
        //三个高热量菜肴的名字
        List<String> threeHighCaloricDishNames = menu().stream().filter((Dish dish)->dish.getCalories()>300)
                .map(Dish::getName)
                .limit(3)
                .collect(Collectors.toList());

        System.out.println(threeHighCaloricDishNames);

        Stream<Dish> stream = menu().stream();
        stream.forEach(System.out::println);
        stream.forEach(System.out::println);

    }


    public static List<Dish> menu() {
        return Arrays.asList(
                new Dish("pork", false, 800, Type.MEAT),
                new Dish("beef", false, 700, Type.MEAT),
                new Dish("chicken", false, 400, Type.MEAT),

                new Dish("french fries", true, 530, Type.OTHER),
                new Dish("rice", true, 350, Type.OTHER),
                new Dish("season fruit", true, 120, Type.OTHER),
                new Dish("pizza", true, 550, Type.OTHER),

                new Dish("prawns", false, 300, Type.FISH),
                new Dish("salmon", false, 450, Type.FISH)
        );
    }
}
