package com.phoenix.utils;

import java.util.List;

public class PrintApple {
    public void prettyPrintApple(List<Apple> appleList, AppleInfoPredicate appleInfoPredicate) {
        for (Apple apple: appleList) {
            String info = appleInfoPredicate.appleInfo(apple);
            System.out.println(info);
        }
    }
}
