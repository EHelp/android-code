package com.ehelp.ehelp.util;

/**
 * Created by chenzhe on 2015/12/13.
 */
public class NullDealer {
    public static String DealNull(String str) {
        if (str.equals("null")) return "无";
        if (str.equals("")) return "无";
        else return str;
    }
}
