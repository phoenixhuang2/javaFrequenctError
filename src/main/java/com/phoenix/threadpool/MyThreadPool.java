package com.phoenix.threadpool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * 调整了入队列和扩容的顺序，实现先扩容再入队列
 */
@Slf4j
public class MyThreadPool {
    public int threadpool() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger();
        MyQueue myQueue = new MyQueue(10);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                2, 5, 1, TimeUnit.SECONDS,
                myQueue,
                new ThreadFactoryBuilder().setNameFormat("demo-threadpool-%d").build(),
                new MyPolicy());

        printStats(threadPoolExecutor);

        IntStream.rangeClosed(1, 20).forEach(i->{
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int id = atomicInteger.incrementAndGet();
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
            } catch (Exception e) {
                log.error("error submitting task{}", id, e);
                atomicInteger.decrementAndGet();
            }
         });

        TimeUnit.SECONDS.sleep(60);
        return atomicInteger.intValue();
    }

    private void printStats(ThreadPoolExecutor threadPool) {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            log.info("=========================");
            log.info("Pool Size: {}", threadPool.getPoolSize());
            log.info("Active Threads: {}", threadPool.getActiveCount());
            log.info("Number of Tasks Completed: {}", threadPool.getCompletedTaskCount());
            log.info("Number of Tasks in Queue: {}", threadPool.getQueue().size());
            log.info("Rejected Handler: {}", threadPool.getRejectedExecutionHandler());
            log.info("=========================");
        }, 0, 1, TimeUnit.SECONDS);
    }

    public static void main(String[] args) throws InterruptedException {
        MyThreadPool myThreadPool = new MyThreadPool();
        myThreadPool.threadpool();
    }
}

@Slf4j
class MyQueue<E> extends ArrayBlockingQueue<E> {
    public MyQueue(int capacity) {
        super(capacity);
    }

    @Override
    public boolean offer(E e) {
        log.info("offer......");
        return false;
    }

    //也可以直接用put
    public boolean offer2(E e) {
        return super.offer(e);
    }
}


class MyPolicy implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
       if (!((MyQueue)executor.getQueue()).offer2(r)) {
           throw new RejectedExecutionException("Task " + r.toString() +
                   " rejected from " +
                   executor.toString());
       }
    }
}


class MyPolicy2 implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        try {
            executor.getQueue().put(r);
            if (executor.isShutdown()) {
                throw new RejectedExecutionException("Task " + r.toString() +
                        " rejected from " +
                        executor.toString());
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
