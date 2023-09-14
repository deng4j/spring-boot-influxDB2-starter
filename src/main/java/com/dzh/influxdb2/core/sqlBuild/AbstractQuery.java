package com.dzh.influxdb2.core.sqlBuild;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.dzh.influxdb2.core.common.instance.Identifier;
import com.dzh.influxdb2.core.common.domain.SqlSegment;
import com.dzh.influxdb2.core.common.domain.express.Entity;
import com.dzh.influxdb2.utils.CollectUtils;
import com.dzh.influxdb2.utils.DateUtils;
import com.dzh.influxdb2.utils.StringUtils;
import com.dzh.influxdb2.core.common.enums.TimeUnit;
import com.influxdb.annotations.Column;

import java.util.ArrayList;
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
            columns.set(i, addIentifierCOLUMN(columns.get(i)));
        }
        StringBuilder s = StringUtils.columnsToString(columns);
        sqlSegment.getLastSql().add(String.format("group(columns: [%s])", s));
        return this.typedThis;
    }

    @Override
    public Children window(Integer every, TimeUnit everyUnit,
                           Integer period, TimeUnit periodUnit,
                           Integer offset, TimeUnit offsetUnit,
                           String timeColumn, String startColumn,
                           String stopColumn, Boolean createEmpty) {
        if (every == null) return this.typedThis;
        if (createEmpty == null) createEmpty = Boolean.FALSE;

        List<Entity> entityList = new ArrayList<>();

        entityList.add(new Entity("every", ":", every + everyUnit.getUnit()));
        if (period != null && periodUnit != null) {
            entityList.add(new Entity("period", ":", period + periodUnit.getUnit()));
        }
        if (offset != null && offsetUnit != null) {
            entityList.add(new Entity("offset", ":", offset + offsetUnit.getUnit()));
        }

        if (StrUtil.isNotEmpty(timeColumn)) {
            entityList.add(new Entity("timeColumn", ":", StringUtils.addDoubleQuotes(timeColumn)));
        }
        if (StrUtil.isNotEmpty(startColumn)) {
            entityList.add(new Entity("startColumn", ":", StringUtils.addDoubleQuotes(startColumn)));
        }
        if (StrUtil.isNotEmpty(stopColumn)) {
            entityList.add(new Entity("stopColumn", ":", StringUtils.addDoubleQuotes(stopColumn)));
        }
        if (Boolean.TRUE.equals(createEmpty)) {
            entityList.add(new Entity("createEmpty", ":", createEmpty.toString()));
        }
        sqlSegment.getLastSql().add(String.format("window(%s)", StringUtils.paramsToStr(entityList)));
        return this.typedThis;
    }

    public Children window(Integer every, TimeUnit everyUnit) {
        this.window(every, everyUnit, null, TimeUnit.S, null, TimeUnit.S, null, null, null, false);
        return this.typedThis;
    }

    @Override
    public Children aggregateWindow(Integer every, TimeUnit everyUnit,
                                    Integer period, TimeUnit periodUnit,
                                    Integer offset, TimeUnit offsetUnit,
                                    String fn, String column,
                                    String timeSrc, String timeDst, Boolean createEmpty) {
        if (every == null || StrUtil.isEmpty(fn)) return this.typedThis;
        if (createEmpty == null) createEmpty = Boolean.FALSE;

        List<Entity> entityList = new ArrayList<>();

        entityList.add(new Entity("every", ":", every + everyUnit.getUnit()));
        if (period != null && periodUnit != null) {
            entityList.add(new Entity("period", ":", period + periodUnit.getUnit()));
        }
        if (offset != null && offsetUnit != null) {
            entityList.add(new Entity("offset", ":", offset + offsetUnit.getUnit()));
        }

        if (StrUtil.isNotEmpty(fn)) {
            entityList.add(new Entity("fn", ":", fn));
        }
        if (StrUtil.isNotEmpty(column)) {
            entityList.add(new Entity("column", ":", StringUtils.addDoubleQuotes(addIentifierCOLUMN(column))));
        }
        if (StrUtil.isNotEmpty(timeSrc)) {
            entityList.add(new Entity("timeSrc", ":", StringUtils.addDoubleQuotes(timeSrc)));
        }
        if (StrUtil.isNotEmpty(timeDst)) {
            entityList.add(new Entity("timeDst", ":", StringUtils.addDoubleQuotes(timeDst)));
        }
        entityList.add(new Entity("createEmpty", ":", createEmpty.toString()));

        sqlSegment.getLastSql().add(String.format("aggregateWindow(%s)", StringUtils.paramsToStr(entityList)));
        return this.typedThis;
    }

    public Children aggregateWindow(Integer every, TimeUnit everyUnit, String fn) {
        aggregateWindow(every, everyUnit, null, null, null, null, fn, null, null, null, null);
        return this.typedThis;
    }

    @Override
    public Children lastSql(String sql) {
        sqlSegment.getLastSql().add(sql);
        return this.typedThis;
    }

    @Override
    public Children sort(Boolean desc, List<String> columns) {
        if (CollUtil.isEmpty(columns)) return this.typedThis;
        if (null==desc) desc = Boolean.FALSE;
        columns.forEach(c -> c = addIentifierCOLUMN(c));
        StringBuilder s = StringUtils.columnsToString(columns);
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
            column = addIentifierCOLUMN(column);
            sql = String.format("column: \"%s\"", column);
        }
        sqlSegment.getLastSql().add(String.format("count(%s)", sql));
        return this.typedThis;
    }

    public Children count() {
        count("");
        return this.typedThis;
    }

    private String addIentifierCOLUMN(String column) {
        return Identifier.COLUMN + column;
    }

    @Override
    public String getSql() {
        if (StrUtil.isNotEmpty(this.bucket)) {
            this.from(this.bucket);
        }
        if (StrUtil.isNotEmpty(this.measurement)) {
            this.sqlSegment.getLastSql().add(0, String.format("filter(fn: (r) => r._measurement == \"%s\")", this.measurement));
        }
        String sql = sqlSegment.getSql();
        if (this.entityInfoMap != null) {
            for (Map.Entry<String, Column> entry : this.entityInfoMap.entrySet()) {
                sql = sql.replace(entry.getKey(), entry.getValue().name());
            }
        } else {
            sql = sql.replace(Identifier.COLUMN, "");
        }
        return sql;
    }
}
