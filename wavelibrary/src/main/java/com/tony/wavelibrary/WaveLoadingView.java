/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.tony.wavelibrary;

import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by sanyinchen on 16/1/19.
 */
public class WaveLoadingView extends View implements WaveLoadingInterface {
    // draw
    private Context mContext;

    // Dynamic Properties.
    private int mCanvasSize;
    private float mAmplitudeRatio;
    private int mWaveColor;
    private int mShapeType;

    // Properties.
    private String mTopTitle;
    private String mCenterTitle;
    private String mBottomTitle;
    private float mDefaultWaterLevel;

    // Object used to draw.
    // Shader containing repeated waves.
    private BitmapShader mWaveShader;
    private Bitmap bitmapBuffer;
    // Shader matrix.
    private Matrix mShaderMatrix;
    // Paint to draw wave.
    private Paint mWavePaint;
    // Paint to draw border.
    private Paint mBorderPaint;

    // Animation.
    private AnimatorSet mAnimatorSet;

    // Default config
    private static final float DEFAULT_AMPLITUDE_RATIO = 0.1f;
    private static final float DEFAULT_WATER_LEVEL_RATIO = 0.5f;
    private static final float DEFAULT_WAVE_LENGTH_RATIO = 1.0f;
    private static final float DEFAULT_WAVE_SHIFT_RATIO = 0.0f;
    private static final int DEFAULT_WAVE_PROGRESS_VALUE = 50;
    private static final int DEFAULT_WAVE_COLOR = Color.parseColor("#212121");
    private static final int DEFAULT_TITLE_COLOR = Color.parseColor("#212121");
    private static final float DEFAULT_BORDER_WIDTH = 0;
    private static final float DEFAULT_TITLE_TOP_SIZE = 18.0f;
    private static final float DEFAULT_TITLE_CENTER_SIZE = 22.0f;
    private static final float DEFAULT_TITLE_BOTTOM_SIZE = 18.0f;

    public WaveLoadingView(Context context) {
        super(context);
        init(context);
    }

    @Override
    public void onProcess(int process) {

    }

    private void init(Context mContext) {
        this.mContext = mContext;

        // Init Wave.
        mShaderMatrix = new Matrix();
        mWavePaint = new Paint();
        // The ANTI_ALIAS_FLAG bit AntiAliasing smooths out the edges of what is being drawn,
        // but is has no impact on the interior of the shape.
        mWavePaint.setAntiAlias(true);

        mWaveColor = DEFAULT_WAVE_COLOR;


        mWavePaint = new Paint();
        mWavePaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(100, 100, 50, mWavePaint);
    }
}
