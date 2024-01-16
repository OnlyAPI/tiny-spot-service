package com.yifan.admin.api.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/2/17 18:28
 */
public class DateTimeUtil {

    private static final ZoneOffset ZONEOFFSET = ZoneOffset.of("+8");
    public static DateTimeFormatter FULLY_DIGITAL = (new DateTimeFormatterBuilder()).appendPattern("yyyyMMddHHmmss").toFormatter();
    public static DateTimeFormatter DATETIME_DEFAULT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static DateTimeFormatter DATE_DIGITAL = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static DateTimeFormatter DATE_KEBAB = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter DATE_SNAKE = DateTimeFormatter.ofPattern("yy/MM/dd");
    public static DateTimeFormatter TIME_DEFAULT = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static DateTimeFormatter SIMPLE_TIME_DEFAULT = DateTimeFormatter.ofPattern("HH:mm");
    public static DateTimeFormatter SIMPLE_DATE_TIME_DEFAULT = DateTimeFormatter.ofPattern("MM-dd HH:mm");
    public static DateTimeFormatter DATETIME_SHORT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final String START_TIME = " 00:00:00";
    public static final String END_TIME = " 23:59:59";

    public DateTimeUtil() {
    }

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static long currentMilli() {
        return LocalDateTime.now().toInstant(ZONEOFFSET).toEpochMilli();
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static Date currentDate() {
        return Date.from(LocalDateTime.now().toInstant(ZONEOFFSET));
    }

    /**
     * 获取unix时间戳
     *
     * @return
     */
    public static long unixTimeStamp() {
        return currentMilli() / 1000L;
    }

    public static String currentYMDHMS() {
        return LocalDateTime.now().format(FULLY_DIGITAL);
    }

    public static String currentYMD() {
        return LocalDateTime.now().format(DATE_KEBAB);
    }

    public static String getTodayStartTimeFormat() {
        return dateFormat(dateStartTime(currentDate()), DATETIME_DEFAULT);
    }

    public static String getTodayEndTimeFormat() {
        return dateFormat(dateEndTime(currentDate()), DATETIME_DEFAULT);
    }

    /**
     * 根据日期获取开始日期
     *
     * @param date
     * @return
     */
    public static Date dateStartTime(Date date) {
        LocalDate ld = date.toInstant().atZone(ZONEOFFSET).toLocalDate();
        return Date.from(LocalDateTime.of(ld.getYear(), ld.getMonth(), ld.getDayOfMonth(), 0, 00, 00).toInstant(ZONEOFFSET));
    }

    /**
     * 根据日期获取最终日期
     *
     * @param date
     * @return
     */
    public static Date dateEndTime(Date date) {
        LocalDate ld = date.toInstant().atZone(ZONEOFFSET).toLocalDate();
        return Date.from(LocalDateTime.of(ld.getYear(), ld.getMonth(), ld.getDayOfMonth(), 23, 59, 59).toInstant(ZONEOFFSET));
    }

    /**
     * 根据时间戳获取开始日期
     *
     * @param timestamp
     * @return
     */
    public static long milliStartTime(long timestamp) {
        LocalDate ld = milliToLdt(timestamp).toLocalDate();
        return LocalDateTime.of(ld.getYear(), ld.getMonth(), ld.getDayOfMonth(), 0, 00, 00).toInstant(ZONEOFFSET).toEpochMilli();
    }

    /**
     * 根据时间戳获取最终日期
     *
     * @param timestamp
     * @return
     */
    public static long milliEndTime(long timestamp) {
        LocalDate ld = milliToLdt(timestamp).toLocalDate();
        return LocalDateTime.of(ld.getYear(), ld.getMonth(), ld.getDayOfMonth(), 23, 59, 59).toInstant(ZONEOFFSET).toEpochMilli();
    }

    public static String milliFormat(long time, DateTimeFormatter formatter) {
        return milliToLdt(time).format(formatter);
    }

    public static String dateFormat(Date date, DateTimeFormatter formatter) {
        return dateToLdt(date).format(formatter);
    }

    public static LocalDateTime dateToLdt(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZONEOFFSET);
    }

    public static Date ldtToDate(LocalDateTime ldt) {
        return Date.from(ldt.toInstant(ZONEOFFSET));
    }

    public static LocalDateTime milliToLdt(long time) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZONEOFFSET);
    }

    public static long ldtToMilli(LocalDateTime ldt) {
        return ldt.toInstant(ZONEOFFSET).toEpochMilli();
    }


    /**
     * 获取过去7天内的日期数组
     *
     * @param intervals intervals天内
     * @return 日期数组
     */
    public static List<String> getDayStrList(int intervals) {
        List<String> pastDaysList = new ArrayList<>();
        for (int i = intervals - 1; i >= 0; i--) {
            pastDaysList.add(getPastDate(i));
        }
        return pastDaysList;
    }

    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        return dateFormat(today, DATE_KEBAB);
    }

    public static String buildBeginTime(String beginTimeYMD){
        return beginTimeYMD + START_TIME;
    }

    public static String buildEndTime(String endTimeYMD){
        return endTimeYMD + END_TIME;
    }

    public static String formatConversationLastMessageTime(Date date) {
        if (date == null) {
            return "";
        }
        LocalDateTime now = LocalDateTime.now();

        String todayStr = now.format(DATE_KEBAB);
        String yesterdayStr = now.minusDays(1).format(DATE_KEBAB);
        String beforeYesterdayStr = now.minusDays(2).format(DATE_KEBAB);

        LocalDateTime messageTime = date.toInstant().atZone(ZONEOFFSET).toLocalDateTime();
        String messageStr = messageTime.format(DATE_KEBAB);

        if (todayStr.equals(messageStr)) {
            return messageTime.format(SIMPLE_TIME_DEFAULT);
        } else if (yesterdayStr.equals(messageStr)) {
            return "昨天";
        } else if (beforeYesterdayStr.equals(messageStr)) {
            return "前天";
        } else {
            return messageTime.format(DATE_SNAKE);
        }
    }

    public static String formatConversationHistoryTime(Date date) {
        if (date == null) {
            return "";
        }
        LocalDateTime now = LocalDateTime.now();
        int thisYear = now.getYear();

        String todayDateStr = now.format(DATE_KEBAB);
        String yesterdayDateStr = now.minusDays(1).format(DATE_KEBAB);
        String beforeYesterdayDateStr = now.minusDays(2).format(DATE_KEBAB);

        LocalDateTime messageTime = date.toInstant().atZone(ZONEOFFSET).toLocalDateTime();
        int messageTimeYear = messageTime.getYear();
        String messageDateStr = messageTime.format(DATE_KEBAB);

        if (todayDateStr.equals(messageDateStr)) {
            return messageTime.format(SIMPLE_TIME_DEFAULT);
        } else if (yesterdayDateStr.equals(messageDateStr)) {
            return "昨天 " + messageTime.format(SIMPLE_TIME_DEFAULT);
        } else if (beforeYesterdayDateStr.equals(messageDateStr)) {
            return "前天 " + messageTime.format(SIMPLE_TIME_DEFAULT);
        } else if (messageTimeYear == thisYear){
            return messageTime.format(SIMPLE_DATE_TIME_DEFAULT);
        }else  {
            return messageTime.format(DATETIME_SHORT);
        }
    }

    public static void main(String[] args) {
        System.out.println(getTodayStartTimeFormat());
        System.out.println(getTodayEndTimeFormat());
        getDayStrList(15).forEach(System.out::println);
    }
}
