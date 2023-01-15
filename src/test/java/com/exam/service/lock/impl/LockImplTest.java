package com.exam.service.lock.impl;

import mockit.Deencapsulation;
import mockit.Tested;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;

public class LockImplTest {
    @Tested
    private LockImpl lock;

    /**
     * 验证：锁功能
     *
     * @Given: 锁已被占
     * @When: 执行lock方法
     * @Then: 其他线程无法上锁，返回false
     */
    @Test
    public void testLock() throws InterruptedException {
        Map<Integer, ReentrantLock> lockMap = new ConcurrentHashMap<>();
        lockMap.put(3, new ReentrantLock());
        Deencapsulation.setField(lock, "lockMap", lockMap);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock(3);
            }
        });
        thread.start();

        Thread.sleep(20);

        Assert.assertFalse(lock.lock(3));
    }

    /**
     * 验证：释放锁功能
     *
     * @Given: 锁已被占
     * @When: 执行unlock方法
     * @Then: 锁被释放
     */
    @Test
    public void testUnlock() {
        Map<Integer, ReentrantLock> lockMap = new ConcurrentHashMap<>();
        lockMap.put(3, new ReentrantLock());
        Deencapsulation.setField(lock, "lockMap", lockMap);
        lock.lock(3);
        Assert.assertTrue(lockMap.get(3).isLocked());

        lock.unlock(3);
        Assert.assertFalse(lockMap.get(3).isLocked());
    }
}