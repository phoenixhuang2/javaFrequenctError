package com.phoenix.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(name = "clientsdk")
public interface Client {
    @PostMapping("/feign/server")
    public void server();
}
