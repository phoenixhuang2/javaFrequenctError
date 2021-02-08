package com.phoenix.map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@RestController
@RequestMapping("map")
public class ConcurrentHashMapDemo {
    //总元素数量
    private static int ITEM_COUNT = 1000;
    //线程数量
    private static int THREAD_COUNT = 10;

    /**
     * 获取一个含有指定数量数据的concurrentHashMap
     * @param count
     * @return
     */
    private ConcurrentHashMap<String, Long> getData(int count) {
        return LongStream.rangeClosed(1, count)
                .boxed()
                .collect(
                        Collectors.toConcurrentMap(
                                i-> UUID.randomUUID().toString(),
                                Function.identity(),
                                (o1, o2)-> o1,
                                ConcurrentHashMap::new)
                );
    }

    /**
     * 错误的方式：
     * concurrentHashMap虽然是线程安全的，但不代表多个操作之间是一致的；如果多个线程操作它，是需要显示的加锁
     * 诸如 size、isEmpty 和 containsValue 等聚合方法，在并发情况下可能会反映 ConcurrentHashMap 的中间状态。因此在并发情况下，这些方法的返回值只能用作参考，而不能用于流程控制。显然，利用 size 方法计算差异值，是一个流程控制。
     * 诸如 putAll 这样的聚合方法也不能确保原子性，在 putAll 的过程中去获取数据可能会获取到部分数据。
     * @return
     * @throws InterruptedException
     */
    @GetMapping("wrong")
    public String wrong() throws InterruptedException {
        //先初始化900个元素
        ConcurrentHashMap<String, Long> concurrentHashMap = getData(ITEM_COUNT-100);
        System.out.println("init size:"+concurrentHashMap.size());

        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);
        forkJoinPool.execute(()->
            IntStream.rangeClosed(1, 10).parallel().forEach(
                    i -> {
                        int gap = ITEM_COUNT - concurrentHashMap.size();
                        System.out.println("gap size:"+gap+" "+Thread.currentThread().getName());
                        concurrentHashMap.putAll(getData(gap));
                    }
            ));
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        System.out.println("finish size:"+concurrentHashMap.size());
        return "OK";
    }

    /**
     *
     * @return
     * @throws InterruptedException
     */
    @GetMapping("right")
    public String right() throws InterruptedException {
        //先初始化900个元素
        ConcurrentHashMap<String, Long> concurrentHashMap = getData(ITEM_COUNT-100);
        System.out.println("init size:"+concurrentHashMap.size());
        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);
        forkJoinPool.execute(()->
            IntStream.rangeClosed(1, 10).parallel().forEach(
                i -> {
                    synchronized (concurrentHashMap) {
                        int gap = ITEM_COUNT - concurrentHashMap.size();
                        System.out.println("gap size:" + gap + " " + Thread.currentThread().getName());
                        concurrentHashMap.putAll(getData(gap));
                    }
                }
            )
        );
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        System.out.println("finish size:"+concurrentHashMap.size());
        return "OK";
    }

}
