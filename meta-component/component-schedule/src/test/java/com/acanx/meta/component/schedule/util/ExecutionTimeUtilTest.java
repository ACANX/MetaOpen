package com.acanx.meta.component.schedule.util;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

class ExecutionTimeUtilTest {

    @Test
    void nextFire() {
        ZonedDateTime nextFire = ExecutionTimeUtil.nextFire(ZonedDateTime.now(), ZonedDateTime.now().minusSeconds(1400), "0 3 20 * * ?");
        System.out.println("下次触发时间: " + nextFire.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
    }

    @Test
    void nextFire2() {
        ZonedDateTime nextFire = ExecutionTimeUtil.nextFire(ZonedDateTime.now(), ZonedDateTime.now().minusSeconds(1400), 20*3600);
        System.out.println("下次触发时间: " + nextFire.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
    }
}