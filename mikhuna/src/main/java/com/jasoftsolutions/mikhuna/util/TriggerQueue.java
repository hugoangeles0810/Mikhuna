package com.jasoftsolutions.mikhuna.util;

/**
 * Created by pc07 on 07/05/2014.
 */
public class TriggerQueue {

    private int count;
    private Runnable triggerRunnable;

    public TriggerQueue(int count, Runnable triggerRunnable) {
        this.count = count;
        this.triggerRunnable = triggerRunnable;
    }

    public void completeOne() {
        count--;
        if (count == 0) {
            triggerRunnable.run();
        }
    }

}
