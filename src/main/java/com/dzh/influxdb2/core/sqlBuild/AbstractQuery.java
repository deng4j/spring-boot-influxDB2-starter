package com.dzh.influxdb2.core.sqlBuild;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.dzh.influxdb2.core.common.Identifier;
import com.dzh.influxdb2.core.sqlBuild.domain.SqlSegment;
import com.dzh.influxdb2.utils.DateUtils;
import com.influxdb.annotations.Column;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class AbstractQuery<T, Children extends AbstractQuery<T, Children>> extends Query<T> implements QueryStruct<AbstractQuery<T, Children>> {
    protected SqlSegment sqlSegment = new SqlSegment();

    protected final Children typedThis = (Children) this;

    @Override
    public Children from(String bucket) {
        sqlSegment.setFromSql(String.format("from(bucket: \"%s\")", bucket));
        return this.typedThis;
    }

    @Override
    public Children range(String start, String end) {
        if (StrUtil.isEmpty(start)) {
            start = "-2";
        } else {
            try {
                start = DateUtils.internationalStandard(start);
            } catch (Exception e) {
                // 解析异常，不做处理
            }
        }
        if (StrUtil.isEmpty(end)) {
            end = DateUtils.internationalStandard(new Date());
        } else {
            end = DateUtils.internationalStandard(end);
        }
        sqlSegment.setRangeSql(String.format("range(start: %s, stop: %s)", start, end));
        return this.typedThis;
    }

    @Override
    public Children filter(String sql) {
        if (StrUtil.isEmpty(sql)) return this.typedThis;
        sqlSegment.getLastSql().add(String.format("filter(fn: (r) => %s)", sql));
        return this.typedThis;
    }

    @Override
    public Children group(List<String> columns) {
        if (CollUtil.isEmpty(columns)) return this.typedThis;
        for (int i = 0; i < columns.size(); i++) {
            columns.set(i,Identifier.COLUMN + columns.get(i));
        }
        StringBuilder s = ListToString(columns);
        sqlSegment.getLastSql().add(String.format("group(columns: [%s])", s));
        return this.typedThis;
    }


    /**
     * 转化为 "column1","columns2",...,"column6"
     *
     * @param columns
     * @return
     */
    @NotNull
    private StringBuilder ListToString(List<String> columns) {
        StringBuilder s = new StringBuilder();
        for (String column : columns) {
            s.append("\"");
            s.append(column);
            s.append("\",");
        }
        return s;
    }

    @Override
    public Children lastSql(String sql) {
        sqlSegment.getLastSql().add(sql);
        return this.typedThis;
    }

    @Override
    public Children sort(List<String> columns, Boolean desc) {
        if (CollUtil.isEmpty(columns)) return this.typedThis;
        columns.forEach(c -> c = Identifier.COLUMN + c);
        StringBuilder s = ListToString(columns);
        sqlSegment.getLastSql().add(String.format("sort(columns: [%s],desc:%s)", s, desc.toString()));
        return this.typedThis;
    }

    @Override
    public Children limit(Number n, Number offset) {
        sqlSegment.getLastSql().add(String.format("limit(n: %s,offset: %s)", n, offset));
        return this.typedThis;
    }

    @Override
    public Children count(String column) {
        String sql;
        if (StrUtil.isEmpty(column)) {
            sql = "";
        } else {
            column = Identifier.COLUMN+column;
            sql = String.format("column: \"%s\"", column);
        }
        sqlSegment.getLastSql().add(String.format("count(%s)", sql));
        return this.typedThis;
    }

    public Children count() {
        count("");
        return this.typedThis;
    }

    @Override
    public String getSql() {
        if (StrUtil.isNotEmpty(this.bucket)){
            this.from(this.bucket);
        }
        if (StrUtil.isNotEmpty(this.measurement)){
            this.sqlSegment.getLastSql().add(0,String.format("filter(fn: (r) => r._measurement == \"%s\")", this.measurement));
        }
        String sql = sqlSegment.getSql();
        if (this.entityInfoMap!=null){
            for (Map.Entry<String, Column> entry : this.entityInfoMap.entrySet()) {
                sql = sql.replace(entry.getKey(),entry.getValue().name());
            }
        }else {
            sql = sql.replace(Identifier.COLUMN,"");
        }
        return sql;
    }
}
