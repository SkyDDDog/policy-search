package com.west2.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 时间工具类
 * @author 天狗
 */
public class DateTimeUtil {

    public static List<String> getChartWeekDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            list.add(sdf.format(calendar.getTime()));
            calendar.add(Calendar.DATE, -1);
        }
        return list;
    }

}
