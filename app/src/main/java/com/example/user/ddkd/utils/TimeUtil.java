package com.example.user.ddkd.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by User on 2016-04-27.
 */
public class TimeUtil {
    // 将时间戳转为字符串
    public static String getStrTime(String cc_time) {
        String re_StrTime = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd   HH:mm");
// 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }
}
