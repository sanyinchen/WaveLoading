/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.tony.wavelibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by sanyinchen on 16/1/19.
 */
public class WaveLoadingView extends View implements WaveLoadingInterface {
    public WaveLoadingView(Context context) {
        super(context);
    }

    public WaveLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WaveLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onProcess(int process) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
