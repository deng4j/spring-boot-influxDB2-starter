package com.dzh.influxdb2.core.sqlBuild;

import com.dzh.influxdb2.core.common.enums.TimeUnit;

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

    Children window(Integer every, TimeUnit everyUnit,
                    Integer period, TimeUnit periodUnit,
                    Integer offset, TimeUnit offsetUnit,
                    String timeColumn, String startColumn,
                    String stopColumn, Boolean createEmpty);

    Children aggregateWindow(Integer every, TimeUnit everyUnit,
                    Integer period, TimeUnit periodUnit,
                    Integer offset, TimeUnit offsetUnit,
                    String fn, String column,
                    String timeSrc, String timeDst, Boolean createEmpty);

    Children sort(Boolean desc, List<String> columns);

    Children limit(Number n,Number offset);

    Children count(String sql);

    Children lastSql(String sql);
}
