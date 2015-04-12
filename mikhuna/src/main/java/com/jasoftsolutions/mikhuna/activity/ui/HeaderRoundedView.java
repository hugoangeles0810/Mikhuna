package com.jasoftsolutions.mikhuna.activity.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Hugo on 11/04/2015.
 */
public class HeaderRoundedView extends View {

    private float scale;
    private Paint p1;
    private RectF oval;
    private static final String COLOR = "#fffe5824";
    private static final String COLOR_BACKGROUND = "#FFE8E8E8";

    public HeaderRoundedView(Context context) {
        super(context);
        init();
    }

    public HeaderRoundedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeaderRoundedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        scale = getResources().getDisplayMetrics().density;
        p1 = new Paint();
        p1.setAntiAlias(true);
        p1.setStrokeWidth(3);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        oval = new RectF(-20,0, getWidth()+20, getHeight()*1.5f);
        p1.setColor(Color.parseColor(COLOR_BACKGROUND));
        p1.setStyle(Paint.Style.FILL);
        p1.setColor(Color.parseColor(COLOR_BACKGROUND));
        canvas.drawRect(oval.left, oval.bottom/2, oval.right, getHeight(), p1);
        canvas.drawOval(oval, p1);
        p1.setStyle(Paint.Style.STROKE);
        p1.setColor(Color.parseColor(COLOR));
        canvas.drawArc(oval, 0, -180, false, p1);
        p1.setColor(Color.RED);
    }
}
