package org.jiezhou.strategy.Impl;


import org.jiezhou.DBContextHolder;
import org.jiezhou.DBRouterConfig;
import org.jiezhou.strategy.IDBRouterStrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 路由策略
 */
public class DBRouterStrategyHashCode implements IDBRouterStrategy {
    private Logger logger = LoggerFactory.getLogger(DBRouterStrategyHashCode.class);

    private DBRouterConfig dbRouterConfig;

    public DBRouterStrategyHashCode(DBRouterConfig dbRouterConfig) {
        this.dbRouterConfig = dbRouterConfig;
    }


    /**
     * 计算方式：
     * size = 库*表
     * idx = 散列刀哪张表
     *
     * @param dbKeyAttr
     */
    @Override
    public void doRouter(String dbKeyAttr) {
        //获取所有表
        int size = dbRouterConfig.getDbCount() * dbRouterConfig.getTbCount();

        //扰动函数 jdk 的 hashmap
        //借鉴
        int idx = (size - 1) & (dbKeyAttr.hashCode() ^ (dbKeyAttr.hashCode() >>> 16));

        //库表索引
        //获取对应的库，库是从1开始算的，因此要在基础上+1
        int dbIndex = (idx / dbRouterConfig.getTbCount()) + 1;

        int tbIndex = (idx - dbRouterConfig.getTbCount() * (dbIndex - 1));

        //设置库表信息，string.format("%02d",dbIndex) 保证dbIndex是两位数，不足补0
        //例：库名test_01 就写 %02的 表名user_001 就写 %03d
        DBContextHolder.setDBKey(String.format("%02d", dbIndex));
        DBContextHolder.setTBKey(String.format("%03d", tbIndex));
        logger.debug("数据库路由 dbIdx：{} tbIdx：{}", dbIndex, tbIndex);
    }

    @Override
    public void setDBKey(String dbKey) {
        DBContextHolder.setDBKey(dbKey);
    }

    @Override
    public void setTBKey(String tableKey) {
        DBContextHolder.setTBKey(tableKey);
    }

    @Override
    public int getDBCount() {
        return dbRouterConfig.getDbCount();
    }

    @Override
    public int getTBCount() {
        return dbRouterConfig.getTbCount();
    }

    @Override
    public void clear() {
        DBContextHolder.clear();
    }
}

