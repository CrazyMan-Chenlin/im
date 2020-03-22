package com.roy.common.sdk.zookeeper;

/**
 * @author chenlin
 */
@FunctionalInterface
public interface ZkLockCallback<T> {

    T doInLock();
}