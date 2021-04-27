package concurrency.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁实现缓存
 * 读到则返回
 * 读不到则加写锁进行写入，写入时需要阻塞其他读线程
 * 在写入缓存的同时，为了避免其他线程同时获取这个缓存中不存在的数据，需要阻塞其他读线程。
 */
public class MyCacheWithReetrantReadWriteLock {
    public static void main(String[] args) {
        Map<String, String> cacheMap = new HashMap<>(4);
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        for (int i = 0; i < 20; i++) {
            String key = String.valueOf(i % 4);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        lock.readLock().lock();
                        String value = cacheMap.get(key);
                        if (value == null) {
                            lock.readLock().unlock();
                            try {
                                lock.writeLock().lock();
                                String value1 = cacheMap.get(key);
                                if (value1 == null) {
                                    cacheMap.put(key, "value");
                                    System.out.println(Thread.currentThread().getName() + "put value");
                                }
                                lock.readLock().lock();
                                System.out.println(Thread.currentThread().getName() + "get new value" + cacheMap.get(key));
                            } finally {
                                lock.writeLock().unlock();
                            }
                        } else {
                            System.out.println(Thread.currentThread().getName() + "get  value" + cacheMap.get(key));
                        }
                    } finally {
                        lock.readLock().unlock();
                    }
                }
            },String.valueOf(i));
            thread.start();
        }
    }
}
