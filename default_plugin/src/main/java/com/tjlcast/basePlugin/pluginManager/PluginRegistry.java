package com.tjlcast.basePlugin.pluginManager;

import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tangjialiang on 2018/5/4.
 */
public class PluginRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(PluginRegistry.class);

    private final ZkClient zkClient;

    public PluginRegistry(String zkAddress) {
        // 创建 ZooKeeper 客户端
        zkClient = new ZkClient(zkAddress, Constant.ZK_SESSION_TIMEOUT, Constant.ZK_CONNECTION_TIMEOUT);
        LOGGER.debug("connect zookeeper");
    }

    public void register(String serviceName, String detailInfo) {
        if (detailInfo == null || detailInfo.equals("")) detailInfo = "None" ;

        // 创建 registry 节点（持久）
        String registryPath = Constant.ZK_REGISTRY_PATH;
        if (!zkClient.exists(registryPath)) {
            zkClient.createPersistent(registryPath);
            LOGGER.debug("create registry node: {}", registryPath);
        }

        // 创建 servicePath 节点（临时）
        String servicePath = registryPath + "/" + serviceName;
        if (!zkClient.exists(servicePath)) {
            zkClient.createEphemeral(servicePath);
        }

        // 向临时节点写入信息
        zkClient.writeData(servicePath, detailInfo);
    }
}