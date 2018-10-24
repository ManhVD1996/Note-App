package com.example.manhvd.noteapp.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ViewItemUtils extends View {

    private Paint mPaint;

    public void init() {
        mPaint = new Paint();
        mPaint.setStrokeWidth(1);
        mPaint.setColor(Color.DKGRAY);;
    }

    public ViewItemUtils(Context context) {
        super(context);
        init();
    }

    public ViewItemUtils(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int stopX = canvas.getWidth();
        super.onDraw(canvas);
        canvas.drawLine(0, 0, stopX, 0, mPaint);
    }
}
