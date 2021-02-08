package com.phoenix.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Filter<T> {

    /**
     * 行为参数化
     * @param appleList
     * @param predicate 接口
     * @return
     */
    public List<Apple> filterApple(List<Apple> appleList, ApplePredicate predicate) {
        List<Apple> list = new ArrayList<>();
        for (Apple apple: appleList) {
            if (predicate.test(apple)) {
                list.add(apple);
            }
        }
        return list;
    }

    //将List抽象化
    public List<T> filterUtil(List<T> list, Predicate<T> predicate) {
        List<T> resList = new ArrayList<>();
        for (T t: list) {
            if (predicate.test(t)) {
                resList.add(t);
            }
        }
        return resList;
    }

    /**
     * 利用已有的function包下的函数式编程接口
     * @param appleList
     * @param predicate
     * @return
     */
    public List<T> filterDemo(List<T> appleList, java.util.function.Predicate<T> predicate) {
        List<T> list = new ArrayList<>();
        for (T t: appleList) {
            if (predicate.test(t)) {
                list.add(t);
            }
        }
        return list;
    }

    public T filterSup(Supplier<T> supplier) {
        return supplier.get();
    }

    public void consumer(T t, Consumer<T> consumer) {
        consumer.accept(t);
    }
}
