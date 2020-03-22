package com.roy.common.sdk.zookeeper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 描述：zookeeper增删改查
 * 重点：删除、修改、查询所用的version版本号，分布式事务锁的实现方式，乐观锁。ACL权限：所有人、限定人、限定IP等
 * @author chenlin
 */
@Slf4j
@Component
public class MyZkConnect{

    /**
     * 集群节点
     */
    @Value("${zookeeper.cluster.address}")
    public String zkServerClusterConnect;

    /**
     * 超时毫秒数
     */
    @Value("${zookeeper.timeout}")
    public int timeout = 3000;

    /**
     * 描述：建立连接
     */
    public ZooKeeper connect() throws IOException, InterruptedException{
        CountDownLatch cdl = new CountDownLatch(1);
        log.info("准备建立zk服务");
        ZooKeeper zk = new ZooKeeper(zkServerClusterConnect, timeout, new MyZkWatcher(cdl,"建立连接"));
        log.info("完成建立zk服务");
        //这里为了等待wather监听事件结束
        cdl.await();
        return zk;
    }
    
    /**
     * 描述：重新连接服务
     * @param sessionId 现有会话ID
     * @param sessionPasswd 现有会话密码
     * 重点：关闭后的会话连接，不支持重连。重连后，前会话连接将会失效。
     */
    public  ZooKeeper reconnect(long sessionId, byte[] sessionPasswd) throws IOException, InterruptedException{
        CountDownLatch cdl = new CountDownLatch(1);
        log.info("准备重新连接zk服务");
        ZooKeeper zk = new ZooKeeper(zkServerClusterConnect, timeout, new MyZkWatcher(cdl,"重新连接"), sessionId, sessionPasswd);
        log.info("完成重新连接zk服务");
        cdl.await();//这里为了等待wather监听事件结束
        return zk;
    }
    
    /**
     * 描述：创建节点
     */
    public  void create(ZooKeeper zk,String nodePath,String nodeData) throws KeeperException, InterruptedException{
        log.info("开始创建节点：{}， 数据：{}",nodePath,nodeData);
        List<ACL> acl = Ids.OPEN_ACL_UNSAFE;
        //持久性节点
        CreateMode createMode = CreateMode.PERSISTENT;
        String result = zk.create(nodePath, nodeData.getBytes(), acl, createMode);
        //创建节点有两种，上面是第一种，还有一种可以使用回调函数及参数传递，与上面方法名称相同。
        log.info("创建节点返回结果：{}",result);
        log.info("完成创建节点：{}， 数据：{}",nodePath,nodeData);
    }
    
    /**
     * 描述：查询节点结构信息
     */
    public static Stat queryStat(ZooKeeper zk,String nodePath) throws KeeperException, InterruptedException{
        log.info("准备查询节点Stat，path：{}", nodePath);
        Stat stat = zk.exists(nodePath, false);
        log.info("结束查询节点Stat，path：{}，version：{}", nodePath, stat.getVersion());
        return stat;
    }
    
    /**
     * 描述：查询节点Data值信息
     */
    public static String queryData(ZooKeeper zk,String nodePath) throws KeeperException, InterruptedException{
        log.info("准备查询节点Data,path：{}", nodePath);
        String data = new String(zk.getData(nodePath, false, queryStat(zk, nodePath)));
        log.info("结束查询节点Data,path：{}，Data：{}", nodePath, data);
        return data;
    }
    
    
    /**
     * 描述：修改节点
     * 重点：每次修改节点的version版本号都会变更，所以每次修改都需要传递节点原版本号，以确保数据的安全性。
     */
    public static Stat update(ZooKeeper zk,String nodePath,String nodeData) throws KeeperException, InterruptedException{
        //修改节点前先查询该节点信息
        Stat stat = queryStat(zk, nodePath);
        log.info("准备修改节点，path：{}，data：{}，原version：{}", nodePath, nodeData, stat.getVersion());
        Stat newStat = zk.setData(nodePath, nodeData.getBytes(), stat.getVersion());
        //修改节点值有两种方法，上面是第一种，还有一种可以使用回调函数及参数传递，与上面方法名称相同。
        //zk.setData(path, data, version, cb, ctx);
        log.info("完成修改节点，path：{}，data：{}，现version：{}", nodePath, nodeData, newStat.getVersion());
        return stat;
    }
    
    /**
     * 描述：删除节点
     */
    public static void delete(ZooKeeper zk,String nodePath) throws InterruptedException, KeeperException{
        //删除节点前先查询该节点信息
        Stat stat = queryStat(zk, nodePath);
        log.info("准备删除节点，path：{}，原version：{}", nodePath, stat.getVersion());
        zk.delete(nodePath, stat.getVersion());
        //修改节点值有两种方法，上面是第一种，还有一种可以使用回调函数及参数传递，与上面方法名称相同。
        //zk.delete(path, version, cb, ctx);
        log.info("完成删除节点，path：{}", nodePath);
    }
}