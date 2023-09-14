package com.dzh.influxdb2.utils;

import java.util.List;

public class CollectUtils {

    /**
     * 条件不为空才添加
     * @param list
     * @param e
     * @param <T>
     */
    public static <T> void addCondition(Boolean condition,List<T> list,T e){
        if (!condition) return;
        list.add(e);
    }
}
