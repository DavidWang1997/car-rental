package com.exam.service.lock.impl;

import com.exam.service.lock.Lock;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 汽车锁
 *
 * @author wangpeng
 */
@Service
public class LockImpl implements Lock {
    private volatile Map<Integer, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    @Override
    public boolean lock(int carId) {
        if (!lockMap.containsKey(carId)) {
            lockMap.put(carId, new ReentrantLock());
        }
        boolean res;
        try {
            res = lockMap.get(carId).tryLock(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            return false;
        }
        return res;
    }

    @Override
    public void unlock(int carId) {
        if (!lockMap.containsKey(carId)) {
            return;
        }
        lockMap.get(carId).unlock();
    }
}
