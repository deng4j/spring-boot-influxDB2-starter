package com.dzh.influxdb2.core.common.enums;

import com.dzh.influxdb2.core.sqlBuild.Isql;

public enum SqlKeyword implements Isql {
    AND("and"),
    OR("or"),
    NE("!="),
    EQ("=="),
    GT(">"),
    GE(">="),
    LT("<"),
    LE("<="),
    ;

    private final String keyword;

    private SqlKeyword(final String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String getSql() {
        return this.keyword;
    }
}