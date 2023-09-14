# 一.介绍

本框架是基于spring对influx查询语句的初步封装，致力于对flux查询语句的快速构建、便捷使用、易读。

influx官网：[Query InfluxDB with Flux | InfluxDB OSS 2.7 Documentation (influxdata.com)](https://docs.influxdata.com/influxdb/v2.7/query-data/get-started/query-influxdb/)

# 二.使用

**普通使用**：

```java
LambdaQuery<QuotaInfo> lambdaQuery = new LambdaQuery<>();
lambdaQuery
    .from("ykk")
    .range("2023-09-09","2023-09-10")
    .filter(new Filter<QuotaInfo>()
            .eq(QuotaInfo::getAlarmName,"温度过低")
            .and()
            .eq(QuotaInfo::getDeviceId,"123456")
            .and()
            .eq(QuotaInfo::getAlarm,"1")
           )
    .sort(true,QuotaInfo::getDeviceId)
    .limit(10,0)
    .count(QuotaInfo::getValue)
    ;
System.out.println(lambdaQuery.getSql());
```

```java
from(bucket: "ykk")
|> range(start: 2023-09-08T16:00:00Z, stop: 2023-09-09T16:00:00Z)
|> filter(fn: (r) => r.alarmName == "温度过低" and r.deviceId == "123456" and r.alarm == "1")
|> sort(columns: ["deviceId",],desc:true)
|> limit(n: 10,offset: 0)
|> count(column: "value")
```

**基于spring IOC使用**：

```yaml
spring:
  influx:
    org: ykk
    bucket: ykk
    url: http://192.168.136.10:8086
    token: iPbdsWbXOXnY5YLNK0VTYXSfcGfk-AdBnbJAIkPU4w30mjCajvuZ0CJI7yvW05J8TFB6XmeLCkxw1b1FmMj8bQ==
```

```java
@Component
@Slf4j
public class QuotaInfoInfluxRepository extends BaseInfluxdb<QuotaInfo> {

}
```

```java
@Test
void find1(){
    LambdaQuery<QuotaInfo> lambdaQuery = new LambdaQuery<>();
    lambdaQuery
        .range("2023-09-09","2023-09-10")
        .filter(new Filter<QuotaInfo>()
                .eq(QuotaInfo::getAlarmName,"温度过低")
                .eq(QuotaInfo::getDeviceId,"123456")
                .or()
                .eq(QuotaInfo::getAlarm,"1")
               )
        .group(QuotaInfo::getDeviceId,QuotaInfo::getValue)
        .sort(true,QuotaInfo::getDeviceId)
        .limit(10,0)
        ;
    System.out.println(quotaInfoInfluxRepository.query(lambdaQuery));
}
```