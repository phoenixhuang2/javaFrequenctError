package com.phoenix.java8.stream;

import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.*;

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
//        distinct();
//        map();
//        flatMap();
//        match();
//        find();
//        optional();
//        reduce();
//        intStream();
//        optional2();
//        demo2();
//        collect();
        groupingby();
    }

    public static void three() {
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
        return asList(
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

    /**
     * 筛选，切片和映射
     * 查找，匹配和归约
     * 数值范围等值流
     * 多个源创建流
     * 无限流
     */

    public static void distinct() {
        List<Integer> list = asList(1,2,4,6,74,7,8,4,83,64,8,9,03,2,4);
        list.stream().filter((Integer i) -> 0 == i%2).distinct().forEach(System.out::println);

        //
        List<Dish> collect = menu().stream().filter(dish -> dish.getType() == Type.MEAT).limit(2).collect(Collectors.toList());
        System.out.println(collect);

    }

    /**
     * 映射
     */
    public static void map() {
        List<String> list = asList("morden", "lili", "java", "In", "Action");
        List<Integer> collect = list.stream().map(String::length).collect(Collectors.toList());
        System.out.println(collect);

        List<Integer> collect1 = menu().stream().map(Dish::getName).map(String::length).collect(Collectors.toList());
        System.out.println(collect1);
    }

    /**
     * 流的扁平化
     */
    public static void flatMap() {
        //<1>
        List<String> list = asList("morden", "lili", "java", "In", "Action");
        //此处返回的是List<String[]>类型，希望返回List<String>
        List<String[]> collect = list.stream().map((String name) -> name.split("")).distinct().collect(Collectors.toList());
        collect.stream().forEach(s->System.out.print(Arrays.toString(s)));
        System.out.println();

        //<2>
        //flatMap
        List<String> collect1 = list.stream().map((String name) -> name.split("")).flatMap(Arrays::stream).distinct().collect(Collectors.toList());
        System.out.println(collect1);

        //<3>
        List<Integer> numbers1 = asList(1,2,3);
        List<Integer> numbers2 = asList(3,4);
        List<int[]> pairs = numbers1.stream().flatMap(i->numbers2.stream().map(j->new int[]{i, j})).collect(Collectors.toList());
        pairs.stream().forEach(i->System.out.print(Arrays.toString(i)));
        System.out.println();


        List<int[]> collect2 = numbers1.stream().flatMap(i -> numbers2.stream().filter(j -> (i + j) % 3 == 0).map(j -> new int[]{i, j})).collect(Collectors.toList());
        collect2.stream().forEach(i-> System.out.print(Arrays.toString(i)));
    }

    /**
     * allMatch anyMatch noneMatch用到了短路
     * 不需要处理整个流就能得到结果
     */
    public static void match() {
        //至少匹配一个
        if (menu().stream().anyMatch(Dish::isVegetarian)) {
            System.out.println("The menu is (somewhat) vegetatian friendly !!");
        }

        //一个也不匹配
        boolean isHealthy = menu().stream().noneMatch(dish->dish.getCalories()>=1000);
        System.out.println(isHealthy);

    }

    /**
     * 查找元素
     */
    public static void find() {
        Optional<Dish> any = menu().stream().filter(Dish::isVegetarian).findAny();
        System.out.println(any.orElse(null));

//        List<Integer> someNumbers =
    }

    /**
     * optional
     */
    public static void optional() {
        menu().stream().findAny().ifPresent(System.out::println);
    }

    /**
     * 归约
     */
    public static void reduce() {
       List<Integer> list = asList(1,2,3,4,5,6,7,8,9);
        Integer reduce = list.stream().reduce(0, (a, b) -> a + b);
        System.out.println(reduce);

        Integer reduce1 = list.stream().reduce(1, (a, b) -> a * b);
        System.out.println(reduce1);

        Optional<Integer> reduce2 = list.stream().reduce(Integer::max);
        System.out.println(reduce2.get());

        Optional<Integer> reduce3 = list.stream().reduce((a, b)->a>b?b:a);
        System.out.println(reduce3.get());

        Optional<Integer> reduce4 = menu().stream().map(Dish::getCalories).reduce(Integer::max);
        System.out.println(reduce4.get());

        Integer reduce5 = menu().stream().map(Dish::getCalories).reduce(0, Integer::sum);
        System.out.println(reduce5);

        Optional<Dish> min = menu().stream().min(Comparator.comparing(Dish::getCalories));
        System.out.println(min.get());
    }

    /**
     * 原始类型流：IntStream DoubleStream LongStream
     * 将流中的元素特化为int, double, long类型，从而避免流暗含的装箱成本。
     */
    public static void intStream() {
        //问题：存在自动装箱
        Integer reduce5 = menu().stream().map(Dish::getCalories).reduce(0, Integer::sum);
        System.out.println(reduce5);

        //原始类型流
        System.out.println(menu().stream().mapToInt(Dish::getCalories).sum());

        OptionalInt max = menu().stream().mapToInt(Dish::getCalories).max();
        System.out.println(max);

    }

    /**
     * 转换回对象流
     */
    public static void intStreamToStream() {
        IntStream intStream = menu().stream().mapToInt(Dish::getCalories);
        Stream<Integer> boxed = intStream.boxed();
    }

    /**
     * 没有值的情况下，可以设置默认值
     */
    public static void optional2() {
        OptionalInt max = menu().stream().mapToInt(Dish::getCalories).max();
        System.out.println(max.orElse(0));
    }

    /**
     * 数值范围
     */
    public static void demo2() {
        //rangeClosed:包含结束值
        IntStream intStream = IntStream.rangeClosed(1, 100)
                .filter(n -> n % 2 == 0);
        System.out.println(intStream.count());

        //range:不包含结束值
        IntStream intStream1 = IntStream.range(1, 100).filter(n -> n % 2 == 0);
        System.out.println(intStream1.count());
    }

    public static void collect() {
//        Map<Currency, List<Transaction>> transactionsByCurrencies = transactions.stream().collect(Collectors.groupingBy(Transaction::getCurrency))
        Long collect = menu().stream().collect(counting());
        System.out.println(collect);

//        long count = menu().stream().count();
//        System.out.println(count);

        Optional<Dish> collect1 = menu().stream().collect(maxBy(Comparator.comparing(Dish::getCalories)));
        System.out.println(collect1.orElse(null).getCalories());


        IntSummaryStatistics collect2 = menu().stream().collect(summarizingInt(Dish::getCalories));
        System.out.println(collect2);

        Integer collect3 = menu().stream().collect(summingInt(Dish::getCalories));
        System.out.println(collect3);

        String collect4 = Arrays.stream(new String[]{"ab", "cd", "ef", "ac"}).collect(joining());
        System.out.println(collect4);

        String collect5 = menu().stream().map(Dish::getName).collect(joining(", "));
        System.out.println(collect5);


        Integer collect6 = menu().stream().collect(reducing(0, Dish::getCalories, (a, b) -> a + b));
        System.out.println(collect6);
    }


    public static void groupingby() {
        //方法引用分类
        Map<Type, List<Dish>> collect = menu().stream().collect(groupingBy(Dish::getType));
        System.out.println(collect);

        //自定义的分类
        Map<CaloricLevel, List<Dish>> collect1 = menu().stream().collect(groupingBy(dish -> {
            if (dish.getCalories() <= 400) return CaloricLevel.DIET;
            else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
            else return CaloricLevel.FAT;
        }));
        System.out.println(collect1);

        //被过滤掉的类别不会出现在结果里
        Map<Type, List<Dish>> collect2 = menu().stream().filter(dish -> dish.getCalories() > 500).collect(groupingBy(Dish::getType));
        System.out.println(collect2);

        //java9 的特性;会把没有元素的类别也保留，返回空集合
//        menu().stream().collect(groupingBy(Dish::getType, filtering(dish->dish.getCalories() > 500, toList())));

        //返回自定义的值，mapping
        Map<Type, List<String>> collect3 = menu().stream().collect(groupingBy(Dish::getType, mapping(Dish::getName, toList())));
        System.out.println(collect3);


        Map<String, List<String>> dishTags = new HashMap<>();
        dishTags.put("pork", asList("greasy", "salty"));
        dishTags.put("beef", asList("salty", "roasted"));
        dishTags.put("chicken", asList("fried", "crisp"));
        dishTags.put("french fries", asList("greasy", "fried"));
        dishTags.put("rice", asList("light", "natural"));
        dishTags.put("season fruit", asList("fresh", "natural"));
        dishTags.put("pizza", asList("tasty", "salty"));
        dishTags.put("prawns", asList("tasty", "roasted"));
        dishTags.put("salmon", asList("delicious", "fresh"));


        //提取每组菜肴对应的标签
//        menu().stream().collect(groupingBy(Dish::getType, flatMapping(dish->dishTags.get(dish.getName()).stream(), toSet())));

        //多级分组
        Map<Type, Map<CaloricLevel, List<String>>> collect4 = menu().stream().collect(groupingBy(Dish::getType,
                groupingBy(dish -> {
                    if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                    else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                    else return CaloricLevel.FAT;
                }, mapping(Dish::getName, toList()))));
        System.out.println(collect4);

    }



}
