package com.acanx.meta.component.schedule.util;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * ExecutionTimeUtil - 任务调度时间计算工具类
 *
 * @author ACANX
 * @since 20260506
 */
public class ExecutionTimeUtil {
    @Deprecated
    public static final String FIXED_RATE = "FIXED_RATE";
    @Deprecated
    public static final String CRON = "CRON";

    /**
     * 判断是否在时间窗口内至少执行一次（固定间隔）
     *
     * @param windowStart      窗口起始时间点
     * @param windowEnd        窗口结束时间点
     * @param lastFireDateTime 上次执行时间点
     * @param fixedInterval    固定时间间隔（秒）
     * @return 是否应该执行
     */
    public static boolean shouldExecuteAtLeastOnceInWindow(ZonedDateTime windowStart, ZonedDateTime windowEnd,
                                                           ZonedDateTime lastFireDateTime, Integer fixedInterval) {
        return lastFireDateTime.isBefore(windowStart) && lastFireDateTime.plus(fixedInterval, ChronoUnit.SECONDS).isBefore(windowEnd);
    }

    /**
     * 判断是否在时间窗口内至少执行一次（CRON表达式）
     *
     * @param windowStart      窗口起始时间点
     * @param windowEnd        窗口结束时间点
     * @param lastFireDateTime 上次执行时间点
     * @param cronExpression   cron表达式
     * @return 是否应该执行
     */
    public static boolean shouldExecuteAtLeastOnceInWindow(
            ZonedDateTime windowStart,
            ZonedDateTime windowEnd,
            ZonedDateTime lastFireDateTime,
            String cronExpression) {
        CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.SPRING53));
        Cron cron = parser.parse(cronExpression);
        ExecutionTime executionTime = ExecutionTime.forCron(cron);
        return executionTime.nextExecution(lastFireDateTime)
                .filter(nextFire -> !nextFire.isAfter(windowStart) && windowStart.isBefore(windowEnd))
                .isPresent();
    }


    /**
     * 计算下一次执行时间（CRON表达式）
     *
     * @param now              当前时间
     * @param lastFireDateTime 上次执行时间
     * @param cronExpression   cron表达式
     * @return 下一次执行时间
     */
    public static ZonedDateTime nextFire(ZonedDateTime now, ZonedDateTime lastFireDateTime, String cronExpression) {
        CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.SPRING53));
        Cron cron = parser.parse(cronExpression);
        ExecutionTime executionTime = ExecutionTime.forCron(cron);

        Optional<ZonedDateTime> nextExecution = executionTime.nextExecution(now);

        if (nextExecution.isPresent()) {
            ZonedDateTime nextFire = nextExecution.get();
            System.out.println("下次触发时间: " + nextFire.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        } else {
            System.out.println("无法确定后续触发时间。");
        }
        return nextExecution.orElse(null);
    }

    /**
     * 计算下一次执行时间（固定间隔）
     *
     * @param now              当前时间
     * @param lastFireDateTime 上次执行时间
     * @param fixedInterval    固定时间间隔（秒）
     * @return 下一次执行时间
     */
    public static ZonedDateTime nextFire(ZonedDateTime now, ZonedDateTime lastFireDateTime, Integer fixedInterval) {
        long seconds = fixedInterval;
        if (seconds <= 0) {
            throw new IllegalArgumentException("fixedInterval 必须为正数，实际为：" + seconds);
        }
        Duration duration = Duration.ofSeconds(seconds);
        ZonedDateTime next = now.plus(duration);
        while (!next.isAfter(lastFireDateTime)) {
            next = next.plus(duration);
        }
        return next;
    }



    /**
     * 判断是否在时间窗口内至少执行一次（固定间隔）
     *
     * @param windowStart      窗口起始时间点
     * @param windowEnd        窗口结束时间点
     * @param lastFireDateTime 上次执行时间点
     * @param fixedInterval    固定时间间隔（秒）
     * @return 是否应该执行
     */
    @Deprecated
    public static boolean shouldExecuteAtLeastOnceInWindow(LocalDateTime windowStart, LocalDateTime windowEnd,
                                                            LocalDateTime lastFireDateTime, Integer fixedInterval) {
        return lastFireDateTime.isBefore(windowStart) && lastFireDateTime.plus(fixedInterval, ChronoUnit.SECONDS).isBefore(windowEnd);
    }



    /**
     * 判断是否在时间窗口内至少执行一次（CRON表达式）
     *
     * @param windowStart      窗口起始时间点
     * @param windowEnd        窗口结束时间点
     * @param tz               时区
     * @param lastFireDateTime 上次执行时间点
     * @param cronExpression   cron表达式
     * @return 是否应该执行
     */
    @Deprecated
    public static boolean shouldExecuteAtLeastOnceInWindow(
            LocalDateTime windowStart,
            LocalDateTime windowEnd,
            String tz,
            LocalDateTime lastFireDateTime,
            String cronExpression) {
        CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.SPRING53));
        Cron cron = parser.parse(cronExpression);
        ZoneId zoneId = ZoneId.of(tz);
        ExecutionTime executionTime = ExecutionTime.forCron(cron);
        return executionTime.nextExecution(lastFireDateTime.atZone(zoneId))
                .filter(nextFire -> !nextFire.isAfter(windowStart.atZone(zoneId)) && windowStart.isBefore(windowEnd))
                .isPresent();
    }



}
