package com.dzh.influxdb2.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    /**
     * 北京时间转国际标准格式
     * 2020-01-14 00:00:00对应的国际标准时间格式为：2020-01-13T16:00:00.000Z
     * @return
     */
    public static String internationalStandard(Date date){
        DateTime dateTime = DateUtil.offsetHour(date, -8);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return format.format(dateTime);
    }

    /**
     * 北京时间转国际标准格式
     * 2020-01-14 00:00:00对应的国际标准时间格式为：2020-01-13T16:00:00.000Z
     * @return
     */
    public static String internationalStandard(String date){
        DateTime time = DateUtil.parse(date);
        DateTime dateTime = DateUtil.offsetHour(time, -8);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return format.format(dateTime);
    }

    public static void main(String[] args) {
        System.out.println(DateUtils.internationalStandard(new Date()));
        System.out.println(DateUtils.internationalStandard("2023-09-05 20:40:55"));
    }
}