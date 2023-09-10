package com.dzh.influxdb2.core.sqlBuild.domain;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.dzh.influxdb2.core.sqlBuild.Isql;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * flux查询语句通过管道符号传递数据，主题分from，range是必须有的，其他的自由搭配
 */
@Data
public class SqlSegment implements Isql {

    private String fromSql;
    private String rangeSql;
    private List<String> lastSql=new ArrayList<>();

    @Override
    public String getSql(){
        StringBuilder sql = new StringBuilder();
        sql.append(fromSql);
        sql.append("\n|> ");
        sql.append(rangeSql);

        if (CollUtil.isNotEmpty(lastSql)){
            sql.append("\n|> ");
            for (int i = 0; i < lastSql.size(); i++) {
                sql.append(lastSql.get(i));
                if (i<lastSql.size()-1){
                    sql.append("\n|> ");
                }
            }
        }
        return sql.toString();
    }

}
