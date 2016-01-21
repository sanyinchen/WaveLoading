/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.tony.wavelibrary;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

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
    private float mDefaultWaterLevel;
    private float mWaveShiftRatio = DEFAULT_WAVE_SHIFT_RATIO;

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
    private static final float DEFAULT_AMPLITUDE = 0.07f;
    private static final float DEFAULT_WATER_LEVEL_RATIO = 0.5f;
    private static final float DEFAULT_WAVE_LENGTH_RATIO = 1.0f;
    private static final float DEFAULT_WAVE_SHIFT_RATIO = 0.0f;
    private static final int DEFAULT_WAVE_PROGRESS_VALUE = 50;
    private static final int DEFAULT_WAVE_COLOR = Color.parseColor("#FF4081");
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
        mAmplitudeRatio = DEFAULT_AMPLITUDE;

        // init Border
        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(4);
        mBorderPaint.setColor(DEFAULT_WAVE_COLOR);

        // Init Animation
        initAnimation();

    }

    private void initAnimation() {
        ObjectAnimator waveShift = ObjectAnimator.ofFloat(this, "waveShiftRatio", 0f, 1f);
        waveShift.setRepeatCount(ValueAnimator.INFINITE);
        waveShift.setDuration(1000);
        waveShift.setInterpolator(new LinearInterpolator());

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.play(waveShift);
    }

    public float getWaveShiftRatio() {
        return mWaveShiftRatio;
    }

    public void setWaveShiftRatio(float waveShiftRatio) {
        // Log.d("srcomp_wave", "setWaveShiftRatio-------");
        if (this.mWaveShiftRatio != waveShiftRatio) {
            this.mWaveShiftRatio = waveShiftRatio;
            // Log.d("srcomp_wave", "setWaveShiftRatio-------" + waveShiftRatio);
            invalidate();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        Log.d("srcomp_wave", "onAttachedToWindow-------");
        if (mAnimatorSet != null) {
            mAnimatorSet.start();
        }
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.d("srcomp_wave", "onDetachedFromWindow-------");
        if (mAnimatorSet != null) {
            mAnimatorSet.end();
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Log.d("srcomp_wave", "onDraw-------");

        mShaderMatrix.setScale(1, mAmplitudeRatio / DEFAULT_AMPLITUDE_RATIO, 0, mDefaultWaterLevel);
        mShaderMatrix.postTranslate((float) (mWaveShiftRatio * getWidth()), 0);
        mWaveShader.setLocalMatrix(mShaderMatrix);
        // Log.d("srcomp_wave", "getWidth:" + getWidth());
        float borderWidth = mBorderPaint.getStrokeWidth();
        if (borderWidth > 0) {
            canvas.drawCircle(getWidth() / 2f, getHeight() / 2f,
                    (getWidth() - borderWidth) / 2f - 1f, mBorderPaint);
        }
        float radius = getWidth() / 2f - borderWidth;
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, mWavePaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Log.d("srcomp_wave", "onMeasure-------");
        int width = measureWidth(widthMeasureSpec);
        int height = measureWidth(heightMeasureSpec);
        int radius = width < height ? width : height;
        // Log.d("srcomp_wave", "radius:" + radius);
        setMeasuredDimension(radius, radius);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d("srcomp_wave", "onSizeChanged-------");
        initWaveShader();
    }

    private void initWaveShader() {

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (width > 0 && height > 0) {
            // 2*pi
            double defaultAngularFrequency = 2.0f * Math.PI / DEFAULT_WAVE_LENGTH_RATIO / width;
            float defaultAmplitude = height * DEFAULT_AMPLITUDE_RATIO;
            mDefaultWaterLevel = height * DEFAULT_WATER_LEVEL_RATIO;
            float defaultWaveLength = width;
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);

            Paint wavePaint = new Paint();
            wavePaint.setStrokeWidth(2);
            wavePaint.setAntiAlias(true);

            int endX = width;
            int endY = height;

            float[] waveY = new float[endX + 1];

            wavePaint.setColor(adjustAlpha(mWaveColor, 0.3f));
            for (int beginX = 0; beginX <= endX; beginX++) {
                double wx = beginX * defaultAngularFrequency;
                float beginY = (float) (mDefaultWaterLevel + defaultAmplitude * Math.sin(wx));
                canvas.drawLine(beginX, beginY, beginX, endY, wavePaint);
                waveY[beginX] = beginY;
            }

            wavePaint.setColor(mWaveColor);
            int waveShift = (int) defaultWaveLength / 4;
            for (int beginX = 0; beginX <= endX; beginX++) {
                canvas.drawLine(beginX, waveY[(beginX + waveShift) % endX], beginX, endY, wavePaint);
            }

            mWaveShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
            this.mWavePaint.setShader(mWaveShader);
            bitmapBuffer = bitmap;
        }

    }

    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    private int measureWidth(int measureSpec) {
        int result = mCanvasSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = mCanvasSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;

    }
}
