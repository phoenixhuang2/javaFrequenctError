package com.phoenix.utils;

public class AppleFancyFormatter implements AppleInfoPredicate{
    @Override
    public String appleInfo(Apple apple) {
        String characteristic = apple.getWeight() > 150 ? "heavy" : "light";
        return "A" + characteristic + " " + apple.getColor() + " a apple";
    }
}
