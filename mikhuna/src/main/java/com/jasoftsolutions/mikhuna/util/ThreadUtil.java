package com.jasoftsolutions.mikhuna.util;

/**
 * Created by mgaray on 30/09/14.
 */
public class ThreadUtil {

    public static void timeout(final Runnable r, final long delay) {
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                try {
                    wait(delay);
                    r.run();
                } catch (InterruptedException e) {
                    ExceptionUtil.handleException(e);
                }
            }
        }).start();
    }

}
