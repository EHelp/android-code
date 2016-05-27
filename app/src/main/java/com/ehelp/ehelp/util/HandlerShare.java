package com.ehelp.ehelp.util;

import android.os.Handler;

/**
 * Created by chenzhe on 2015/12/8.
 */
public class HandlerShare {
    public static Handler handler = null;
    //get handler
    public Handler getHandler() {
        return handler;

    }
    //set handler
    public void setHandler(Handler handler) {
        HandlerShare.handler = handler;
    }

}
