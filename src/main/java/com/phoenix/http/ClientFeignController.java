package com.phoenix.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * feign和ribbon 超时参数校验
 */
@RestController
@RequestMapping("feign")
@Slf4j
public class ClientFeignController {
    @Resource
    private Client client;

    @GetMapping("client")
    public void timeout() {
        long begin = System.currentTimeMillis();

        try {
            client.server();
        } catch (Exception e) {
            log.warn("执行耗时：{}ms 错误：{}", System.currentTimeMillis() - begin, e.getMessage());
        }

    }
}
