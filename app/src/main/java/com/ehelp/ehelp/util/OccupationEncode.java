package com.ehelp.ehelp.util;

/**
 * Created by chenzhe on 2015/12/6.
 */
public class OccupationEncode {
    private static final String[] oc = new String[]{"其他", "无业游民", "工人", "白领", "工程师",
            "教师", "学生", "警察", "保安", "医生", "运动员", "农民"};
    public static int OccupationToInt(String occupation) {
        for (int i = 0; i < oc.length; i++) {
            if (occupation.equals(oc[i])) return i;
        }
        return -1;
    }

    public static String OccupationToString(int occupation) {
        return oc[occupation];
    }
}
