package com.dzh.influxdb2.core;

import com.dzh.influxdb2.core.common.instance.Identifier;
import com.dzh.influxdb2.core.sqlBuild.LambdaQuery;
import com.dzh.influxdb2.core.sqlBuild.Query;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApi;
import com.influxdb.client.WriteOptions;
import com.influxdb.client.domain.WritePrecision;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseInfluxdb<T> {
    @Autowired
    private InfluxdbConfig influxdbConfig;
    @Autowired
    private InfluxDBClient influxDBClient;

    private final Type type;
    private Class<T> t;
    private String measurement;
    private Map<String, Column> entityInfoMap = new HashMap<>();

    public BaseInfluxdb() {
        Type type = this.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        this.type = parameterizedType.getActualTypeArguments()[0];

        String classFullName = this.type.getTypeName();
        Class<?> c = null;
        try {
            c = Class.forName(classFullName);
            this.t = (Class<T>) c;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void init(){
        this.measurement = getMeasurement();
        this.entityInfoMap = getColumnMap();
    }

    private String getMeasurement() {
        Measurement measurement = this.t.getAnnotation(Measurement.class);
        return measurement.name();
    }

    private Map<String,Column> getColumnMap() {
        Field[] fields = t.getDeclaredFields();
        Map<String,Column> columnMap=new HashMap<>();
        for (Field field : fields) {
            String name = field.getName();
            Column column = field.getAnnotation(Column.class);
            if (column==null)break;
            columnMap.put(Identifier.COLUMN +name,column);
        }
        return columnMap;
    }

    private void initData(Query<T> query){
        query.setEntityInfoMap(this.entityInfoMap);
        query.setMeasurement(this.measurement);
        query.setBucket(this.influxdbConfig.getBucket());
        System.out.println(query.getSql());
    }

    //---------------------------------------------------------------------------------------

    public boolean write(Object o){
        WriteOptions writeOptions = WriteOptions.builder()
                .batchSize(5000)
                .flushInterval(1000)
                .bufferLimit(10000)
                .jitterInterval(1000)
                .retryInterval(5000)
                .build();
        try (WriteApi writeApi = influxDBClient.getWriteApi(writeOptions)) {
            writeApi.writeMeasurement(influxdbConfig.getBucket(), influxdbConfig.getOrg(), WritePrecision.NS, o);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 查询数据方法
     */
    public <M> List<M> query(String sql, Class<M> t){
        return influxDBClient.getQueryApi().query(sql, influxdbConfig.getOrg(), t);
    }

    /**
     * 查询数据方法
     */
    public List<T> query(Query<T> query){
        initData(query);
        String sql = query.getSql();
        return influxDBClient.getQueryApi().query(sql, influxdbConfig.getOrg(), t);
    }

    /**
     * 查询数据方法
     */
    public <M> List<M> query(Query<T> query,Class<M> c){
        initData(query);
        String sql = query.getSql();
        return influxDBClient.getQueryApi().query(sql, influxdbConfig.getOrg(), c);
    }

    /**
     * 查询全部
     */
    public List<T> queryAll(){
        LambdaQuery<T> query = new LambdaQuery<>();
        query.range("-5000",null);
        initData(query);
        return influxDBClient.getQueryApi().query(query.getSql(), influxdbConfig.getOrg(), t);
    }
}
