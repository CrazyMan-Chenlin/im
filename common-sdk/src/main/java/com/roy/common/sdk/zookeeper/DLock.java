package com.roy.common.sdk.zookeeper;

import com.roy.common.sdk.exception.ZkLockException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.ErrorListenerPathable;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.TimeUnit;

/**
 * @author chenlin
 */
@Slf4j
public class DLock {

    private static final long TIMEOUT_D = 100L;
    private static final String ROOT_PATH_D = "/dLock";
    private String lockRootPath;
    private CuratorFramework client;

    public DLock(CuratorFramework client) {
        this(ROOT_PATH_D, client);
    }

    public DLock(String lockRootPath, CuratorFramework client) {
        this.lockRootPath = lockRootPath;
        this.client = client;
    }

    public InterProcessMutex mutex(String path) {
        if (!StringUtils.startsWith(path, "/")) {
            path = "/" + path;
        }

        return new InterProcessMutex(this.client, this.lockRootPath + path);
    }

    public <T> T mutex(String path, ZkLockCallback<T> zkLockCallback) throws ZkLockException {
        return this.mutex(path, zkLockCallback, 100L, TimeUnit.MILLISECONDS);
    }

    public <T> T mutex(String path, ZkLockCallback<T> zkLockCallback, long time, TimeUnit timeUnit) throws ZkLockException {
        String finalPath = this.getLockPath(path);
        InterProcessMutex mutex = new InterProcessMutex(this.client, finalPath);

        try {
            if (!mutex.acquire(time, timeUnit)) {
                throw new ZkLockException("acquire zk lock return false");
            }
        } catch (Exception var13) {
            throw new ZkLockException("acquire zk lock failed.", var13);
        }

        T var8;
        try {
            var8 = zkLockCallback.doInLock();
        } finally {
            this.releaseLock(finalPath, mutex);
        }

        return var8;
    }

    private void releaseLock(String finalPath, InterProcessMutex mutex) {
        try {
            mutex.release();
            log.info("delete zk node path:{}", finalPath);
            this.deleteInternal(finalPath);
        } catch (Exception var4) {
            log.error( "release lock failed, path:{}", finalPath, var4);
//            LogUtil.error(this.logger, "dlock", "release lock failed, path:{}", new Object[]{finalPath, var4});
        }

    }

    public void mutex(String path, ZkVoidCallBack zkLockCallback, long time, TimeUnit timeUnit) throws ZkLockException {
        String finalPath = this.getLockPath(path);
        InterProcessMutex mutex = new InterProcessMutex(this.client, finalPath);

        try {
            if (!mutex.acquire(time, timeUnit)) {
                throw new ZkLockException("acquire zk lock return false");
            }
        } catch (Exception var13) {
            throw new ZkLockException("acquire zk lock failed.", var13);
        }

        try {
            zkLockCallback.response();
        } finally {
            this.releaseLock(finalPath, mutex);
        }

    }

    public String getLockPath(String customPath) {
        if (!StringUtils.startsWith(customPath, "/")) {
            customPath = "/" + customPath;
        }

       return this.lockRootPath + customPath;
    }

    private void deleteInternal(String finalPath) {
        try {
            this.client.delete().inBackground().forPath(finalPath);
        } catch (Exception var3) {
            log.info("delete zk node path:{} failed", finalPath);
        }

    }

    public void del(String customPath) {
        String lockPath = null;
        try {
            lockPath = this.getLockPath(customPath);
            this.client.delete().inBackground().forPath(lockPath);
        } catch (Exception var4) {
            log.info("delete zk node path:{} failed", lockPath);
        }

    }
}