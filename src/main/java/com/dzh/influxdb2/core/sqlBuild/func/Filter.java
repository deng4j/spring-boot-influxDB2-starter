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
public class Filter<T> implements Compare<T,Filter<T>> {
    private List<Express> expressList = new ArrayList<>();
    private Express lastExpress;

    @Override
    public Filter<T> and() {
        addLogic(SqlKeyword.AND);
        return this;
    }

    @Override
    public Filter<T> or() {
        addLogic(SqlKeyword.OR);
        return this;
    }

    private void addLogic(SqlKeyword key) {
        Logic e = new Logic(key.getSql());
        if (lastExpress instanceof Logic){
            expressList.set(expressList.size()-1, e);
        }else if (lastExpress instanceof Entity){
            expressList.add(e);
        }
    }

    @Override
    public Filter<T> eq(SFunction<T> sf, Object val) {
        addEntity(sf,SqlKeyword.EQ,val);
        return this;
    }

    public Filter<T> eq(Boolean condition,SFunction<T> sf, Object val) {
        if (!condition) return this;
        eq(sf,val);
        return this;
    }

    @Override
    public Filter<T> ne(SFunction<T> sf, Object val) {
        addEntity(sf,SqlKeyword.NE,val);
        return this;
    }

    public Filter<T> ne(Boolean condition,SFunction<T> sf, Object val) {
        if (!condition) return this;
        ne(sf,val);
        return this;
    }

    @Override
    public Filter<T> lt(SFunction<T> sf, Object val) {
        addEntity(sf,SqlKeyword.LT,val);
        return this;
    }

    public Filter<T> lt(Boolean condition,SFunction<T> sf, Object val) {
        if (!condition) return this;
        lt(sf,val);
        return this;
    }

    @Override
    public Filter<T> le(SFunction<T> sf, Object val) {
        addEntity(sf,SqlKeyword.LE,val);
        return this;
    }

    public Filter<T> le(Boolean condition,SFunction<T> sf, Object val) {
        if (!condition) return this;
        le(sf,val);
        return this;
    }

    @Override
    public Filter<T> gt(SFunction<T> sf, Object val) {
        addEntity(sf,SqlKeyword.GT,val);
        return this;
    }

    public Filter<T> gt(Boolean condition,SFunction<T> sf, Object val) {
        if (!condition) return this;
        gt(sf,val);
        return this;
    }

    @Override
    public Filter<T> ge(SFunction<T> sf, Object val) {
        addEntity(sf,SqlKeyword.GE,val);
        return this;
    }

    public Filter<T> ge(Boolean condition,SFunction<T> sf, Object val) {
        if (!condition) return this;
        ge(sf,val);
        return this;
    }

    private void addEntity(SFunction<T> sf, SqlKeyword keyword,Object value){
        String fieldName = LambdaUtils.convertToFieldName(sf);
        expressList.add(new Entity(fieldName,keyword.getSql(),value.toString()));
        expressList.add(new Logic(SqlKeyword.AND.getSql()));
    }

    @Override
    public String getSql(){
        StringBuilder sql = new StringBuilder();
        for (int i = 0; i < expressList.size()-1; i++) {
            Express express = expressList.get(i);
            if (express instanceof Entity){
                Entity e = (Entity) express;
                e.setFiedName(Identifier.COLUMN+e.getFiedName());
            }
            sql.append(express.getSql());
        }
        return sql.toString();
    }
}
