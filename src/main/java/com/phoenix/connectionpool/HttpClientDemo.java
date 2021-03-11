package com.phoenix.connectionpool;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("httpclient")
public class HttpClientDemo {

    /**
     * HttpClient内含了连接池，客户端无需获取连接，直接使用即可，此处如果每次都创建一个CloseableHttpClient，那么会出现大量连接，
     * @return
     */
    @GetMapping("wrong1")
    public String wrong1() {
        CloseableHttpClient client = HttpClients.custom()
                .setConnectionManager(new PoolingHttpClientConnectionManager())
                .evictIdleConnections(60, TimeUnit.SECONDS)
                .build();
        try (CloseableHttpResponse execute = client.execute(new HttpGet("http://github.com"))) {
            return EntityUtils.toString(execute.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 定义为静态变量
     */
    private static CloseableHttpClient httpClient;

    static {
        httpClient = HttpClients.custom()
                .setMaxConnPerRoute(1)
                .setMaxConnTotal(1)
                .evictIdleConnections(60, TimeUnit.SECONDS)
                .build();
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    @GetMapping("right")
    public String right() {
        try (CloseableHttpResponse execute = httpClient.execute(new HttpGet("http://www.baidu.com"));) {
            return EntityUtils.toString(execute.getEntity());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
