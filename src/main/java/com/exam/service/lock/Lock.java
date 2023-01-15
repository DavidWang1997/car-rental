package com.exam.service.lock;

/**
 * 汽车锁
 *
 * @author wangpeng
 */
public interface Lock {
    boolean lock(int carId);

    void unlock(int carId);
}
