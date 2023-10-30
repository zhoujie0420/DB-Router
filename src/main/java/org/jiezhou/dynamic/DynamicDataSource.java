package org.jiezhou.dynamic;

import org.jiezhou.DBContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


/**
 * 动态获取数据源
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return "db" + DBContextHolder.getDBKey();
    }
}
