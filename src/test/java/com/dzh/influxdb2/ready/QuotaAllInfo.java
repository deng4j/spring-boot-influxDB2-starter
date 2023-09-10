package com.dzh.influxdb2.ready;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.Data;
import lombok.ToString;

import java.time.Instant;

/**
 * 包括时间字段的指标对象
 */
@Data
@ToString(callSuper = true)
@Measurement(name = "quota")
public class QuotaAllInfo extends QuotaInfo{
    @Column(name = "time")
    private Instant time;
}