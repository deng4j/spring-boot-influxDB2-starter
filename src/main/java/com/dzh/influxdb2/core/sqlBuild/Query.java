package com.dzh.influxdb2.core.sqlBuild;

import com.influxdb.annotations.Column;
import java.util.Map;

public abstract class Query<T> implements Isql{
    protected Map<String, Column> entityInfoMap;
    protected String bucket;
    protected String measurement;

    public void setEntityInfoMap(Map<String, Column> entityInfoMap){
        this.entityInfoMap = entityInfoMap;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }
}
