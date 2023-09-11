package com.dzh.influxdb2;

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
    public void testPost(){
        for (int i = 0; i < 20; i++) {
            QuotaInfo quotaInfo=new QuotaInfo();
            quotaInfo.setDeviceId("123456");
            quotaInfo.setQuotaId("1");
            quotaInfo.setQuotaName("温度");
            quotaInfo.setAlarm("1");
            quotaInfo.setAlarmName("温度过低");
            quotaInfo.setValue(-1D);
            quotaInfoInfluxRepository.write(quotaInfo);
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
                .sort(true,QuotaInfo::getDeviceId)
                .limit(10,0)
                .count(QuotaInfo::getValue)
                ;
        System.out.println(lambdaQuery.getSql());
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
