package com.phoenix.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.plaf.IconUIResource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * 并发限制
 */

@RestController
@RequestMapping("reptile")
@Slf4j
public class ReptileController {

    static CloseableHttpClient httpClient;
    static CloseableHttpClient httpClient2;
    static {
        httpClient = HttpClients.custom().setConnectionManager(new PoolingHttpClientConnectionManager()).build();
        httpClient2 = HttpClients.custom().setMaxConnPerRoute(10).setMaxConnTotal(20).build();
    }
    @GetMapping("server")
    public int server() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        return 1;
    }

    /**
     * httpclient有并发数量限制，默认为2
     * 耗时：5s+
     * @param count
     * @return
     * @throws InterruptedException
     */
    @GetMapping("wrong")
    public int wrong(@RequestParam(value = "count", defaultValue = "10") int count) throws InterruptedException {
        return sendRequest(count, ()->httpClient);
    }

    /**
     * 设置了并发数量
     * 耗时: 1s+
     * @param count
     * @return
     * @throws InterruptedException
     */
    @GetMapping("right")
    public int right(@RequestParam(value = "count", defaultValue = "10") int count) throws InterruptedException {
        return sendRequest(count, ()->httpClient2);
    }


    private int sendRequest(int count, Supplier<CloseableHttpClient> client) throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger();
        ExecutorService threadPool = Executors.newCachedThreadPool();
        long begin = System.currentTimeMillis();
        IntStream.rangeClosed(1, count).forEach(i->{
            threadPool.execute(()->{
                try (CloseableHttpResponse execute = client.get().execute(new HttpGet("http://localhost:9001/reptile/server"));) {
                    atomicInteger.addAndGet(Integer.parseInt(EntityUtils.toString(execute.getEntity())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
        threadPool.shutdown();
        threadPool.awaitTermination(1, TimeUnit.HOURS);
        log.info("发送 {} 次请求，耗时 {} ms", atomicInteger.get(), System.currentTimeMillis() - begin);
        return atomicInteger.get();
    }
}
