package com.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyClass {
    public static void main(String[] args){
        String string = "P:{0.333,5.55}@{10.66,10}@{10,10}@{10,10.90389483403};LW:5";
        Pattern pattern = Pattern.compile("\\d+(.\\d+)*,\\d+(.\\d+)*");
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()){
            System.out.println(matcher.group());
        }
        String str = "122223";
        System.out.println(str.substring(0,str.length()-1));
    }
}
