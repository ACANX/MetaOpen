package com.acanx.meta.component.schedule.util;

import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class ExecutionTimeUtilTest {

    @Test
    void nextFire() {
        ZonedDateTime fixedNow = ZonedDateTime.of(2026, 6, 17, 10, 0, 0, 0, ZoneId.systemDefault());
        ZonedDateTime lastFire = fixedNow.minusSeconds(1400);
        ZonedDateTime nextFire = ExecutionTimeUtil.nextFire(fixedNow, lastFire, "0 3 20 * * ?");
        assertNotNull(nextFire, "下次触发时间不应为空");
        assertEquals(20, nextFire.getHour(), "触发时间应为20时");
        assertEquals(3, nextFire.getMinute(), "触发时间应为20:03");
        assertTrue(nextFire.isAfter(fixedNow), "触发时间应在当前时间之后");
        System.out.println("下次触发时间: " + nextFire.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
    }

    @Test
    void nextFire2() {
        ZonedDateTime fixedNow = ZonedDateTime.of(2026, 6, 17, 10, 0, 0, 0, ZoneId.systemDefault());
        ZonedDateTime lastFire = fixedNow.minusSeconds(1400);
        ZonedDateTime nextFire = ExecutionTimeUtil.nextFire(fixedNow, lastFire, 20 * 3600);
        assertNotNull(nextFire, "下次触发时间不应为空");
        assertEquals(fixedNow.plusSeconds(20 * 3600L), nextFire, "固定间隔触发时间应为当前时间 + 20小时");
        assertTrue(nextFire.isAfter(lastFire), "触发时间应在上次执行时间之后");
        System.out.println("下次触发时间: " + nextFire.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
    }
}
