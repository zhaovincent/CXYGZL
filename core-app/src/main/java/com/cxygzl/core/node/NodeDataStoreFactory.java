package com.cxygzl.core.node;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.core.env.Environment;

public class NodeDataStoreFactory {

    public static IDataStoreHandler getInstance() {
        Environment environment = SpringUtil.getBean(Environment.class);
        String mapNodeDataStore = environment.getProperty("node.data.store", "mapNodeDataStore");
        IDataStoreHandler bean = SpringUtil.getBean(mapNodeDataStore,
                IDataStoreHandler.class);
        return bean;
    }

}
