package com.phoenix.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.OptionalDouble;

@SpringBootTest
class PhoenixApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void optional() {
        System.out.println(Optional.of(1).get());
        System.out.println(Optional.ofNullable(null).orElse("A"));
        System.out.println(OptionalDouble.empty().isPresent());
        System.out.println(Optional.of(1).map(Math::incrementExact).get());
        System.out.println(Optional.of(1).filter(integer -> integer % 2 == 0).orElse(null));
        System.out.println(Optional.empty().orElseThrow(IllegalArgumentException::new));
    }
}
