package com.dzh.influxdb2.core.sqlBuild.domain.express;

/**
 * r._measurement == "cpu" and
 */
public class Entity implements Express {
    private String fiedName;
    private String operator;
    private String value;

    public Entity(String fiedName, String operator, String value) {
        this.fiedName = fiedName;
        this.operator = operator;
        this.value = value;
    }

    public String getFiedName() {
        return fiedName;
    }

    public void setFiedName(String fiedName) {
        this.fiedName = fiedName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getSql() {
        return String.format("r.%s %s \"%s\"",fiedName,operator,value);
    }
}
