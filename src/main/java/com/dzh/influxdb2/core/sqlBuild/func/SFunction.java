package com.dzh.influxdb2.core.sqlBuild.func;

import java.io.Serializable;

@FunctionalInterface
public interface SFunction<T> extends Serializable {
    Object get(T source);
}