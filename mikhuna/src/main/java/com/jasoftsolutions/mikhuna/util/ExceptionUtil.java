package com.jasoftsolutions.mikhuna.util;

import com.bugsense.trace.BugSenseHandler;
import com.jasoftsolutions.mikhuna.remote.Const;

/**
 * Created by pc07 on 31/03/2014.
 */
public class ExceptionUtil {

    public static void handleException(Exception e) {
//        handleExceptionWithMessage(e, null);
        ignoreException(e);
        if (Const.BUGSENSE_ENABLED) {
            BugSenseHandler.sendException(e);
        }
    }

    public static void handleExceptionWithMessage(Exception e, String title, String message) {
        ignoreException(e);
        if (Const.BUGSENSE_ENABLED) {
            try {
                throw new Exception(title + ":\n" + message, e);
            } catch (Exception se) {
                BugSenseHandler.sendException(se);
            }
        }
    }

    public static void ignoreException(Exception e) {
        e.printStackTrace();
    }

    public static void traceByException() {
        try {
            throw new Exception("stack trace exception...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
