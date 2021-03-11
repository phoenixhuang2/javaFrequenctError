package com.phoenix.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("ribbonclient")
@Slf4j
public class RibbonRetryIssueClientController {
    @Autowired
    private SmsClient smsClient;

    @GetMapping("wrong")
    public String wrong() {
        log.info("client is called");
        try {
            smsClient.sendSmsWrong("1360000000", UUID.randomUUID().toString());
        } catch (Exception e) {
            log.error("send sms failed: {}", e.getMessage());
        }
        return "done";
    }
}
