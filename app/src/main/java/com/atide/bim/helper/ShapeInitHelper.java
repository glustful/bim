package com.atide.bim.helper;

import android.database.Cursor;
import android.graphics.PointF;

import com.atide.bim.utils.PointUtils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by atide on 2016/3/30.
 */
public class ShapeInitHelper {
    public static void rectInit(Cursor cursor,PointF lt,PointF rb){
        String range = cursor.getString(cursor.getColumnIndex("rang")).toUpperCase();
        String start = range.substring(range.indexOf("SP"), range.indexOf("EP"));
        String end = range.substring(range.indexOf("EP"));
        String sp = start.substring(start.indexOf("{")+1,start.indexOf("}"));
        String ep = end.substring(end.indexOf("{")+1,end.indexOf("}"));
        String[] a1 = sp.split(",");
        String[] a2 = ep.split(",");
        lt.set(PointUtils.convertOriginalToPercent(a1));

        rb.set(PointUtils.convertOriginalToPercent(a2));
    }

    public static void pointInit(Cursor cursor,PointF center){
        String range = cursor.getString(cursor.getColumnIndex("rang")).toUpperCase();
        String start = range.substring(range.indexOf("SP"), range.indexOf("EP"));
        String end = range.substring(range.indexOf("EP"));
        String sp = start.substring(start.indexOf("{")+1,start.indexOf("}"));
        String ep = end.substring(end.indexOf("{") + 1, end.indexOf("}"));
        String[] a1 = sp.split(",");
        String[] a2 = ep.split(",");
        PointF lt = PointUtils.convertOriginalToPercent(a1);
        PointF rb = PointUtils.convertOriginalToPercent(a2);
        center.set(new PointF(lt.x + (rb.x-lt.x)/2,lt.y + (rb.y-lt.y)/2));
    }

    public static void lineInit(Cursor cursor,ArrayList<PointF> dots){
        //P:{112,233}@{};LW:5
        String range = cursor.getString(cursor.getColumnIndex("rang"));
        Pattern pattern = Pattern.compile("\\d+(.\\d+)*,\\d+(.\\d+)*");
        Matcher matcher = pattern.matcher(range);
        while (matcher.find()){
            String[] arr = matcher.group().split(",");
            if (arr.length<2)
                continue;
            dots.add(PointUtils.convertOriginalToPercent(arr));
        }
    }
}
