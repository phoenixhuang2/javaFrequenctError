package com.phoenix.threadlocal;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/threadlocal")
public class Demo {
    //用ThreadLocal存储当前用户信息
    private static final ThreadLocal<Integer> currentUser = ThreadLocal.withInitial(() -> null);

    /**
     * 问题：
     * 容易出现用户A获取到用户B的信息；
     * 原因：
     * 程序运行在tomcat中，执行程序的线程是tomcat的工作线程，tomcat的工作线程是基于线程池的。重用了线程
     * ThreadLocal使用完后不删除数据，就有可能出现上述情况。
     * <p>
     * 方案：
     * 使用完后显示的清除设置的数据
     *
     * @param userId
     * @return
     */
    @GetMapping("/wrong")
    public Map wrong(@RequestParam("userId") Integer userId) {
        //存之前，先查一次
        String before = Thread.currentThread().getName() + " : " + currentUser.get();
        //设置当前用户信息
        currentUser.set(userId);
        //设置完之后再查一次
        String after = Thread.currentThread().getName() + " : " + currentUser.get();
        Map<String, String> map = new HashMap();
        map.put("bebore", before);
        map.put("after", after);
        return map;
    }

    @GetMapping("right")
    public Map right(@RequestParam("userId") Integer userId) {
        //存之前，先查一次
        String before = Thread.currentThread().getName() + " : " + currentUser.get();
        try {
            //设置当前用户信息
            currentUser.set(userId);
            //设置完之后再查一次
            String after = Thread.currentThread().getName() + " : " + currentUser.get();
            Map<String, String> map = new HashMap();
            map.put("bebore", before);
            map.put("after", after);
            return map;
        } finally {
            //不要忘记清除数据
            currentUser.remove();
        }
    }
}
