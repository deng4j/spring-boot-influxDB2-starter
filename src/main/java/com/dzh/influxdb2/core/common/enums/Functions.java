package com.dzh.influxdb2.core.common.enums;

public enum Functions {
    COUNT("count"),
    COUNT_P("count()"),
    ;

    private String name;

    Functions(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
