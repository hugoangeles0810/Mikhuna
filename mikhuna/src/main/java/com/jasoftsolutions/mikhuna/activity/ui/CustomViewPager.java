package com.jasoftsolutions.mikhuna.activity.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by pc07 on 23/10/2014.
 */
public class CustomViewPager extends ViewPager {

    private boolean swippingEnabled;

    public CustomViewPager(Context context) {
        super(context);
        setSwippingEnabled(true);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSwippingEnabled(true);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.swippingEnabled) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.swippingEnabled) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }

    public boolean isSwippingEnabled() {
        return swippingEnabled;
    }

    public void setSwippingEnabled(boolean swippingEnabled) {
        this.swippingEnabled = swippingEnabled;
    }
}
