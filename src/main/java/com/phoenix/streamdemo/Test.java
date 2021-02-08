package com.phoenix.streamdemo;

import javax.swing.text.html.Option;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * stream demo
 */
public class Test {
    //流对象的创建
    public static<T> void empty() {
        //创建空的流对象
        StreamSupport.stream(Spliterators.<T>emptySpliterator(), false);
    }

    public void example1() {
        List<Integer> ints = Arrays.asList(1,2,4,5,6,7,8,9);
        ints.stream().map(i -> new Point2D.Double((double)i%3, (double)i/3))
        .filter(point -> point.getY() > 1)
        .mapToDouble(point -> point.distance(0, 0))
        .average()
        .orElse(0);
    }

    public static void main(String[] args) {
        int a = 10;
        System.out.println(Optional.of(a).isPresent());

        System.out.println(Optional.of(a).orElse(0));

    }

//    public void filesExample() throws IOException {
//        try (Stream<Path> pathStream = Files.walk(Paths.get("."))) {
//            pathStream.filter(Files::isRegularFile)
//                    .filter(FileSystems.getDefault().getPathMatcher("glob:**/I.java")::matches)
////                    .flatMap()
//        }
//    }



}
