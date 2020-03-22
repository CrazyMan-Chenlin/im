package com.roy.common.sdk.exception;

/**
 * @author chenlin
 */
public class ZkLockException extends Exception {

    public ZkLockException() {
    }

    public ZkLockException(String message) {
        super(message);
    }

    public ZkLockException(String message, Throwable cause) {
        super(message, cause);
    }
}
