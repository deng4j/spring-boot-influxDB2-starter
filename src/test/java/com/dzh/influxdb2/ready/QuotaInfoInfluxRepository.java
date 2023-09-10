package com.dzh.influxdb2.ready;

import com.dzh.influxdb2.core.BaseInfluxdb;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class QuotaInfoInfluxRepository extends BaseInfluxdb<QuotaInfo> {

}