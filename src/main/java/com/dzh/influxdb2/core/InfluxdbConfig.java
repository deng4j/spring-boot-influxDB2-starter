package com.dzh.influxdb2.core;

import com.influxdb.LogLevel;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@Data
public class InfluxdbConfig {
 
    @Value("${spring.influx.url:''}")
    private String influxDBUrl;
    @Value("${spring.influx.token:''}")
    private String token;

    @Value("${spring.influx.org:''}")
    private String org;

    @Value("${spring.influx.bucket:''}")
    private String bucket;
 
    @Bean
    public InfluxDBClient influxDBClient() {
        InfluxDBClient influxDBClient = InfluxDBClientFactory.create(influxDBUrl, token.toCharArray());
        influxDBClient.setLogLevel(LogLevel.BASIC);
        return influxDBClient;
    }
}