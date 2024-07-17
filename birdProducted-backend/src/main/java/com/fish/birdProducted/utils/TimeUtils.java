package com.fish.birdProducted.utils;

/**
 * @author fish
 * <p>
 * 创建时间：2024/2/17 9:24
 * 时间工具类
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeUtils {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String getCurrentTime() {
       return LocalDateTime.now().format(dateTimeFormatter);
    }
    /*public static String getFormatTime(Date time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
    }*/
    public static Date getFormatTime(Date time) {
        // 创建 SimpleDateFormat 对象
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 格式化日期并返回格式化后的日期字符串
        String formattedDateStr = formatter.format(time);
        try {
            // 将格式化后的日期字符串解析为 Date 对象并返回
            return formatter.parse(formattedDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            // 解析失败时返回 null
            return null;
        }
    }

}
