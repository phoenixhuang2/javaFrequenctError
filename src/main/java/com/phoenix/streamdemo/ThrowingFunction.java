package com.phoenix.streamdemo;

import java.io.IOException;
import java.util.function.Function;

/**
 * 用 ThrowingFunction 包装方法，把受检异常转换为运行时异常，让代码更清晰：
 * @param <T>
 * @param <R>
 * @param <E>
 */
@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Throwable> {
    static <T, R, E extends Throwable> Function<T, R> unchecked(ThrowingFunction<T, R, E> f) {
        return t -> {
            try {
                return f.apply(t);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }

    R apply(T t) throws E, IOException;
}
