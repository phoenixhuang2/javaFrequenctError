package com.phoenix.utils;

public class AppleRedAndHeavyPredicate implements ApplePredicate{
    @Override
    public boolean test(Apple apple) {
        if (Color.RED.equals(apple.getColor()) && apple.getWeight() > 150.0) {
            return true;
        }
        return false;
    }
}
