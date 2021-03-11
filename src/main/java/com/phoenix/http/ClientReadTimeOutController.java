package com.phoenix.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequestMapping("clientreadtimeout")
public class ClientReadTimeOutController {
    private String getResponse(String url, int connectionTimeout, int readTimeout) throws IOException {
        return Request.Get("http://localhost:9001/clientreadtimeout"+url)
                .connectTimeout(connectionTimeout)
                .socketTimeout(readTimeout)
                .execute()
                .returnContent()
                .asString();
    }

    @GetMapping("client")
    public String client() throws IOException {
        log.info("client1 called");
        return getResponse("/server?timeout=5000", 1000, 2000);
    }

    @GetMapping("server")
    public void server(@RequestParam("timeout") int timeout) throws InterruptedException {
        log.info("server caled");
        TimeUnit.MILLISECONDS.sleep(timeout);
        log.info("Done");
    }
}
