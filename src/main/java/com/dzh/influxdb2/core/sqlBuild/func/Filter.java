package com.dzh.influxdb2.core.sqlBuild.func;

import com.dzh.influxdb2.core.common.Identifier;
import com.dzh.influxdb2.core.common.LambdaUtils;
import com.dzh.influxdb2.core.common.SqlKeyword;
import com.dzh.influxdb2.core.sqlBuild.domain.express.Entity;
import com.dzh.influxdb2.core.sqlBuild.domain.express.Express;
import com.dzh.influxdb2.core.sqlBuild.domain.express.Logic;

import java.util.ArrayList;
import java.util.List;


/**
 *  |> filter(fn: (r) => r._measurement == "cpu" and r._field == "usage_system" and r.cpu == "cpu-total")
 */
public class Filter<T> implements Compare<T> {
    private List<Express> expressList = new ArrayList<>();

    @Override
    public Compare<T> and() {
        expressList.add(new Logic(SqlKeyword.AND.getSql()));
        return this;
    }

    @Override
    public Compare<T> or() {
        expressList.add(new Logic(SqlKeyword.OR.getSql()));
        return this;
    }

    @Override
    public Compare<T> eq(SFunction<T> sf, Object val) {
        expressList.add(addColumn(sf,SqlKeyword.EQ,val));
        return this;
    }

    @Override
    public Compare<T> ne(SFunction<T> sf, Object val) {
        expressList.add(addColumn(sf,SqlKeyword.NE,val));
        return this;
    }

    @Override
    public Compare<T> lt(SFunction<T> sf, Object val) {
        expressList.add(addColumn(sf,SqlKeyword.LT,val));
        return this;
    }

    @Override
    public Compare<T> le(SFunction<T> sf, Object val) {
        expressList.add(addColumn(sf,SqlKeyword.LE,val));
        return this;
    }

    @Override
    public Compare<T> gt(SFunction<T> sf, Object val) {
        expressList.add(addColumn(sf,SqlKeyword.GT,val));
        return this;
    }

    @Override
    public Compare<T> ge(SFunction<T> sf, Object val) {
        expressList.add(addColumn(sf,SqlKeyword.GE,val));
        return this;
    }

    private Express addColumn(SFunction<T> sf, SqlKeyword keyword,Object value){
        String fieldName = LambdaUtils.convertToFieldName(sf);
        return new Entity(fieldName,keyword.getSql(),value.toString());
    }

    @Override
    public String getSql(){
        StringBuilder sql = new StringBuilder();
        for (Express express : expressList) {
            if (express instanceof Entity){
                Entity e = (Entity) express;
                e.setFiedName(Identifier.COLUMN+e.getFiedName());
            }
            sql.append(express.getSql());
        }
        return sql.toString();
    }
}
