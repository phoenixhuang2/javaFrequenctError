package com.phoenix.streamdemo;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
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

    public void filesExample() throws IOException {
        try (Stream<Path> pathStream = Files.walk(Paths.get("."))) {
            pathStream.filter(Files::isRegularFile)
                    .filter(FileSystems.getDefault().getPathMatcher("glob:**/I.java")::matches)
                    .flatMap(ThrowingFunction.unchecked((Path path)->
                        Files.readAllLines(path).stream()
                                .filter(line-> Pattern.compile("public class").matcher(line).find())
                                .map(line->path.getFileName()+">>"+line)
                    ))
                    .forEach(System.out::println); //打印所有行

        }
    }


    /**
     *
     * @return
     */
    public static Object computeIfAbsent() {
        Long id = 0l;
        ConcurrentHashMap<Long, Object> cache = new ConcurrentHashMap<>();
        List<Product> data = new ArrayList<>();
        Object o = cache.computeIfAbsent(id, i->data.stream().filter(product -> product.getId().equals(i)).findFirst().orElse(null));
        return o;
    }

}
