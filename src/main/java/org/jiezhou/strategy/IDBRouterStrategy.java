package org.jiezhou.strategy;

public interface IDBRouterStrategy {
    /**
     * 路由计算
     */
    void doRouter(String dbKeyAttr);


    /**
     * 手动设置分库路由
     */
    void setDBKey(String dbKey);

    /**
     * 手动设置分表路由
     */
    void setTBKey(String tableKey);

    /**
     * 获取分库数
     */
    int getDBCount();

    /**
     * 获取分表数
     */
    int getTBCount();

    /**
     * 清除路由
     */
    void clear();
}
