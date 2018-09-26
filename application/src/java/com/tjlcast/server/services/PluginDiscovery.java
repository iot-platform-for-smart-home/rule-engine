package com.tjlcast.server.services;


import com.tjlcast.server.common.CollectionUtil;
import com.tjlcast.server.common.Constant;
import com.tjlcast.server.data.Plugin;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * 基于 ZooKeeper 的服务发现接口实现
 *
 * @author huangyong
 * @since 1.0.0
 */
public class PluginDiscovery {

    private static final Logger LOGGER = LoggerFactory.getLogger(PluginDiscovery.class);

    private String zkAddress;

    public PluginDiscovery(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public List<Plugin> discover() {
        // 创建 ZooKeeper 客户端
        List<Plugin> plugin = new LinkedList<>();

        ZkClient zkClient = new ZkClient(zkAddress, Constant.ZK_SESSION_TIMEOUT, Constant.ZK_CONNECTION_TIMEOUT);
        Stat stat = new Stat();
        LOGGER.debug("connect zookeeper");
        try {
            // 获取 service 节点
            String servicePath = Constant.ZK_REGISTRY_PATH;
            if (!zkClient.exists(servicePath)) {

                zkClient.createPersistent(servicePath);
                return plugin;
            }

            List<String> addressList = zkClient.getChildren(servicePath);
            if (CollectionUtil.isEmpty(addressList)) {
                throw new RuntimeException(String.format("can not find any address node on path: %s", servicePath));
            }

            for(String childNode:addressList)
            {
                String data = zkClient.readData(servicePath+"/"+childNode,stat);
                String[] splitData=data.split("\\|");

                plugin.add(new Plugin(childNode, splitData[0], splitData[1]));
            }
        } finally {
            zkClient.close();
        }
        return plugin ;
    }
}