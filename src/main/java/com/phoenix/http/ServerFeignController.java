package com.phoenix.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("feign")
@Slf4j
public class ServerFeignController {

    @PostMapping("/server")
    public void server() throws InterruptedException {
        TimeUnit.MINUTES.sleep(10);
    }
}
