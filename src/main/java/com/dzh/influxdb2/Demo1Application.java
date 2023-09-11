package com.dzh.influxdb2;

import com.dzh.influxdb2.core.EnableInfluxDB;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableInfluxDB
public class Demo1Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo1Application.class, args);
    }
}
