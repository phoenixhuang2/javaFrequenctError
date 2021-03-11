package com.phoenix.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "SmsClient")
public interface SmsClient {
    @GetMapping("/ribbonserver/sms")
    void sendSmsWrong(@RequestParam("mobile") String mobile, @RequestParam("message") String message);
}
