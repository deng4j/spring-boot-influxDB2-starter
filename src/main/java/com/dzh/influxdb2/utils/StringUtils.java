package com.dzh.influxdb2.utils;

import cn.hutool.core.util.StrUtil;
import com.dzh.influxdb2.core.common.domain.express.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StringUtils {
    private static String doubleQuotes = "\"%s\"";

    /**
     * 添加双引号 "aaa"
     * @param val
     * @return
     */
    public static String addDoubleQuotes(String val){
        return String.format(doubleQuotes,val);
    }

    /**
     * 转化为 "column1","columns2",...,"column6"
     *
     * @param columns
     * @return
     */
    @NotNull
    public static StringBuilder columnsToString(List<String> columns) {
        StringBuilder s = new StringBuilder();
        for (String column : columns) {
            s.append(addDoubleQuotes(column));
            s.append(",");
        }
        return s;
    }

    /**
     * a = 1,b = 2
     * @param entities
     * @return
     */
    public static String paramsToStr(List<Entity> entities) {
        StringBuilder sql = new StringBuilder();
        for (Entity entity : entities) {
            if (StrUtil.isEmpty(entity.getFiedName())) continue;
            sql.append(entity.getSql());
            sql.append(",");
        }
        return sql.toString();
    }

    /**
     * 如果val为空，则返回一个默认值
     * @return
     */
    public static String getDefault(String val,String defaultVal){
        return StrUtil.isEmpty(val)?defaultVal:val;
    }

    public static void main(String[] args) {
        System.out.println(getDefault("1","a"));
    }
}
