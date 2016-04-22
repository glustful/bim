package com.example;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyClass {
    public static void main(String[] args) throws Exception{

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        System.out.println(simpleDateFormat.format(new Date()));


    }
}
