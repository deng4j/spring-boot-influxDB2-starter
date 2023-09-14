package com.dzh.influxdb2.core.common.enums;

public enum TimeUnit {
    NS("ns"), // 纳秒
    US("us"), // 微妙
    MS("ms"), // 毫秒
    S("s"), // 秒
    M("m"), // 分
    H("h"), // 时
    D("d"), // 天
    W("w"), // 星期
    MO("mo"), // 月
    Y("y"), // 年
    ;


    private String unit;

    TimeUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }
}
