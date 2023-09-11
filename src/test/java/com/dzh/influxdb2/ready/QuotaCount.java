package com.dzh.influxdb2.ready;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.Data;

@Data
@Measurement(name = "quota")
public class QuotaCount {

    @Column(name = "_value")
    private Long value;
}