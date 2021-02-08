package com.phoenix.map;

/**
 * 充分了解并发工具的特性
 *
 * 使用 ConcurrentHashMap 来统计，Key 的范围是 10。
 * 使用最多 10 个并发，循环操作 1000 万次，每次操作累加随机的 Key。
 * 如果 Key 不存在的话，首次设置值为 1。
 *
 */
public class SynchronizedAndCas {


}
