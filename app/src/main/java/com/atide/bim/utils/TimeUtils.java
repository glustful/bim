package com.atide.bim.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by atide on 2016/3/29.
 */
public class TimeUtils {

    public static String convertToString(Date date,String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }
}
