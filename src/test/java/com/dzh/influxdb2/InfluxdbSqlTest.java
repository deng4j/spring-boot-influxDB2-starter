package com.dzh.influxdb2;

import cn.hutool.core.util.StrUtil;
import com.dzh.influxdb2.core.common.enums.TimeUnit;
import com.dzh.influxdb2.core.sqlBuild.func.Filter;
import com.dzh.influxdb2.core.sqlBuild.LambdaQuery;
import com.dzh.influxdb2.ready.QuotaAllInfo;
import com.dzh.influxdb2.ready.QuotaCount;
import com.dzh.influxdb2.ready.QuotaInfo;
import com.dzh.influxdb2.ready.QuotaInfoInfluxRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
class InfluxdbSqlTest {
    @Autowired
    private QuotaInfoInfluxRepository quotaInfoInfluxRepository;

    @Test
    public void testPost() throws InterruptedException {
        for (int i = 0; i < 20; i++) {
            QuotaInfo quotaInfo=new QuotaInfo();
            quotaInfo.setDeviceId("123456");
            quotaInfo.setQuotaId("1");
            quotaInfo.setQuotaName("温度");
            quotaInfo.setAlarm("1");
            quotaInfo.setAlarmName("温度过低"+i);
            quotaInfo.setValue(-1.8);
            quotaInfoInfluxRepository.write(quotaInfo);
            Thread.sleep(1000);
        }
    }

    @Test
    void testSQL(){
        LambdaQuery<QuotaInfo> lambdaQuery = new LambdaQuery<>();
        lambdaQuery
                .from("ykk")
                .range("2023-09-09","2023-09-10")
                .filter(new Filter<QuotaInfo>()
                        .eq(QuotaInfo::getAlarmName,"温度过低")
                        .eq(QuotaInfo::getDeviceId,"123456")
                        .and()
                        .and()
                        .eq(QuotaInfo::getAlarm,"1")
                )
                .window(10, TimeUnit.D)
                .sort(true,QuotaInfo::getDeviceId)
                .limit(10,0)
                .count(QuotaInfo::getValue)
                .aggregateWindow(3,TimeUnit.D,"count")
                ;
        System.out.println(lambdaQuery.getSql());
    }

    @Test
    void testsql1(){
        LambdaQuery<QuotaInfo> lambdaQueryByPage = new LambdaQuery<>();
        lambdaQueryByPage
                .from("ykk")
                .range(null,null)
                .filter(new Filter<QuotaInfo>()
                        .eq(StrUtil.isNotEmpty("123456"),QuotaInfo::getQuotaId,"123456")
                )
                .group(QuotaInfo::getDeviceId,QuotaInfo::getQuotaId)
                .sort(true,QuotaInfo::getDeviceId)
                .limit(10,0);
        System.out.println(lambdaQueryByPage.getSql());
    }

    @Test
    void findAll(){
        System.out.println(quotaInfoInfluxRepository.queryAll());
    }

    @Test
    void find1(){
        LambdaQuery<QuotaInfo> lambdaQuery = new LambdaQuery<>();
        lambdaQuery
                .from("ykk")
                .range("","")
                .filter(new Filter<QuotaInfo>()
                        .eq(QuotaInfo::getAlarmName,"温度过低")
                        .and()
                        .eq(QuotaInfo::getDeviceId,"123456")
                        .and()
                        .eq(QuotaInfo::getAlarm,"1")
                )
                .group(QuotaInfo::getDeviceId,QuotaInfo::getValue)
                .sort(true,QuotaInfo::getDeviceId)
                .limit(10,0)
        ;
        System.out.println(quotaInfoInfluxRepository.query(lambdaQuery, QuotaAllInfo.class));
    }

    @Test
    void find2(){
        LambdaQuery<QuotaInfo> lambdaQuery = new LambdaQuery<>();
        lambdaQuery
                .from("ykk")
                .range("","")
                .filter(new Filter<QuotaInfo>()
                        .eq(QuotaInfo::getAlarmName,"温度过低")
                        .and()
                        .eq(QuotaInfo::getDeviceId,"123456")
                        .and()
                        .eq(QuotaInfo::getAlarm,"1")
                )
                .count(QuotaInfo::getValue)
        ;
        System.out.println(quotaInfoInfluxRepository.query(lambdaQuery, QuotaCount.class));
    }

}
