package com.ehelp.ehelp.util;

import com.ehelp.ehelp.R;

/**
 * Created by UWTH on 2015/12/9.
 */
public class Reputation2level {
    static public int getLevel(double value) {
        if (value >= 0 && value < 1) {
            return R.mipmap.creditlevel0;
        } else if (value >= 1 && value < 2) {
            return R.mipmap.creditlevel1;
        } else if (value >= 2 && value < 3) {
            return R.mipmap.creditlevel2;
        } else if (value >= 3 && value < 4) {
            return R.mipmap.creditlevel3;
        } else if (value >= 4 && value < 5) {
            return R.mipmap.creditlevel4;
        } else if (value == 5) {
            return R.mipmap.creditlevel5;
        } else {
            return R.mipmap.creditlevel0;
        }
    }
}
