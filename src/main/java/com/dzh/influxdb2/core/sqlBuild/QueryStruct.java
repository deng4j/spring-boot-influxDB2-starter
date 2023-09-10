package com.dzh.influxdb2.core.sqlBuild;

import java.util.List;

/**
 * 主体结构
 * @param <Children>
 */
public interface QueryStruct<Children>{

    Children from(String bucket);

    Children range(String start,String end);

    Children filter(String sql);

    Children group(List<String> columns);

    Children sort(List<String> columns, Boolean desc);

    Children limit(Number n,Number offset);

    Children count(String sql);

    Children lastSql(String sql);
}
