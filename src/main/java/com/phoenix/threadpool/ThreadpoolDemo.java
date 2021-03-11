package com.phoenix.threadpool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class ThreadpoolDemo {
    public void oom1() throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(1);
        printStats(threadPoolExecutor);
        for (int i=0; i< 1000000; i++) {
            threadPoolExecutor.execute(()->{
                String payload = IntStream.rangeClosed(1, 1000000).mapToObj(__->"a").collect(Collectors.joining(""))+
                        UUID.randomUUID().toString();
                try {
                    TimeUnit.HOURS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info(payload);
            });
        }
        threadPoolExecutor.shutdown();
        threadPoolExecutor.awaitTermination(1, TimeUnit.HOURS);
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadpoolDemo threadpoolDemo = new ThreadpoolDemo();
//        threadpoolDemo.oom1();
        threadpoolDemo.right();
    }



    private void printStats(ThreadPoolExecutor threadPool) {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            log.info("=========================");
            log.info("Pool Size: {}", threadPool.getPoolSize());
            log.info("Active Threads: {}", threadPool.getActiveCount());
            log.info("Number of Tasks Completed: {}", threadPool.getCompletedTaskCount());
            log.info("Number of Tasks in Queue: {}", threadPool.getQueue().size());

            log.info("=========================");
        }, 0, 1, TimeUnit.SECONDS);
    }


    /**
     * 通过日志可以看到
     * 我们可以总结出线程池默认的工作行为：
     * 1、不会初始化 corePoolSize 个线程，有任务来了才创建工作线程；
     * 2、当核心线程满了之后不会立即扩容线程池，而是把任务堆积到工作队列中；
     * 3、当工作队列满了后扩容线程池，一直到线程个数达到 maximumPoolSize 为止；（新创建的线程是不入队列直接执行）
     * 4、如果队列已满且达到了最大线程后还有任务进来，
     * 5、按照拒绝策略处理；当线程数大于核心线程数时，线程等待 keepAliveTime 后还是没有任务需要处理的话，收缩线程到核心线程数。
     *
     * 声明线程池后立即调用 prestartAllCoreThreads 方法，来启动所有核心线程；传入 true 给 allowCoreThreadTimeOut 方法，来让线程池在空闲的时候同样回收核心线程。
     * @return
     * @throws InterruptedException
     */
    public int right() throws InterruptedException {
        AtomicInteger integer = new AtomicInteger();
        //自定义线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 5, 5,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(10),
                new ThreadFactoryBuilder().setNameFormat("demo-threadpool-%d").build(),
                new ThreadPoolExecutor.AbortPolicy());

        //启动所有的核心线程
//        threadPoolExecutor.prestartAllCoreThreads();
//        //允许收回核心线程
//        threadPoolExecutor.allowCoreThreadTimeOut(true);
        //每秒钟打印线程池的情况
        printStats(threadPoolExecutor);

        //每秒钟提交一次，共提交20次
        IntStream.rangeClosed(1, 20).forEach(i->{
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int id = integer.incrementAndGet();
            //每次提交耗时10秒钟
            try {
                threadPoolExecutor.submit(() -> {
                    log.info("{} started", id);
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.info("{} finished", id);
                });
            } catch (Exception ex) {
                log.error("error submitting task{}", id, ex);
                integer.decrementAndGet();
            }
        });

        TimeUnit.SECONDS.sleep(60);
        return integer.intValue();
    }
}


