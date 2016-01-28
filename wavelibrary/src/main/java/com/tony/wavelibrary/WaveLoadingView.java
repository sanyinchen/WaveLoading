/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.tony.wavelibrary;

import java.lang.ref.WeakReference;

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
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by sanyinchen on 16/1/19.
 */
public class WaveLoadingView extends View {
    // draw
    private Context mContext;

    // Dynamic Properties.
    private float mAmplitudeRatio;
    private int mWaveColor;
    private WaveConfig waveConfig;

    // Properties
    private int mCanvasSize;
    private float mWaveShiftRatio = DEFAULT_WAVE_SHIFT_RATIO;
    private String mTitle;
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

    private Paint mTextPaint;

    // Animation.
    private AnimatorSet mAnimatorSet;

    // Default config
    private static final float DEFAULT_AMPLITUDE_RATIO = 0.1f;
    private static final float DEFAULT_AMPLITUDE = 0.07f;

    private static final float DEFAULT_WAVE_LENGTH_RATIO = 1.0f;
    private static final float DEFAULT_WAVE_SHIFT_RATIO = 0.0f;

    public WaveLoadingView(Context context, WaveConfig waveConfig) {
        super(context);
        if (waveConfig == null) {
            new Exception("WaveConfig must not be null");
        }
        this.waveConfig = waveConfig;
        init(context);
    }

    private void init(Context mContext) {
        this.mContext = mContext;
        Log.d("srcomp_wave", "waveLevel-------" + waveConfig.getmWavelevel());
        mTitle = (int) (waveConfig.getmWavelevel() * 10000) / 100 + "%";
        // Init Wave.
        mShaderMatrix = new Matrix();
        mWavePaint = new Paint();
        // The ANTI_ALIAS_FLAG bit AntiAliasing smooths out the edges of what is being drawn,
        // but is has no impact on the interior of the shape.
        mWavePaint.setAntiAlias(true);

        mWaveColor = waveConfig.getmWaveColor();
        mAmplitudeRatio = DEFAULT_AMPLITUDE;

        // init Border
        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(waveConfig.getmBoardSize());
        mBorderPaint.setColor(waveConfig.getmWaveColor());

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(waveConfig.getmTitleColor());
        mTextPaint.setStyle(Paint.Style.FILL);
        // mTextPaint.setStrokeWidth(4);
        mTextPaint.setTextSize(CommonUtils.sp2px(getContext(), waveConfig.getmTitleSizeSp()));

        int w = getWidth();
        int h = getHeight();
        mCanvasSize = w < h ? w : h;

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
        if (mAnimatorSet != null) {
            mAnimatorSet.start();
        }
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mAnimatorSet != null) {
            mAnimatorSet.end();
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // mDefaultWaterLevel = getHeight() * (1f - waveConfig.getmWavelevel());
        mDefaultWaterLevel=getHeight();
        mCanvasSize = canvas.getWidth();
        mAmplitudeRatio=0.05f;
        mShaderMatrix.setScale(1, mAmplitudeRatio / DEFAULT_AMPLITUDE_RATIO, 0, mDefaultWaterLevel);
        mShaderMatrix.postTranslate((float) (mWaveShiftRatio * getWidth()), 0);
        mWaveShader.setLocalMatrix(mShaderMatrix);
        // Log.d("srcomp_wave", "getmWavelevel:" + waveConfig.getmWavelevel());
        float borderWidth = mBorderPaint.getStrokeWidth();
        if (borderWidth > 0) {
            canvas.drawCircle(getWidth() / 2f, getHeight() / 2f,
                    (getWidth() - borderWidth) / 2f - 1f, mBorderPaint);
        }
        float radius = getWidth() / 2f - borderWidth;
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, mWavePaint);
        // canvas.drawBitmap(bitmapBuffer, 0, 0, mWavePaint);
        float midle = mTextPaint.measureText(mTitle);
        canvas.drawText(mTitle, getWidth() / 2 - midle, mDefaultWaterLevel, mTextPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureWidth(heightMeasureSpec);
        int radius = width < height ? width : height;
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
            mDefaultWaterLevel = height * 0.5f;
            float defaultWaveLength = width;
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);

            Paint wavePaint = new Paint();
            wavePaint.setStrokeWidth(2);
            wavePaint.setAntiAlias(true);

            int endX = width + 1;
            int endY = height + 1;

            float[] waveY = new float[endX];

            wavePaint.setColor(adjustAlpha(mWaveColor, 0.3f));
            for (int beginX = 0; beginX < endX; beginX++) {
                double wx = beginX * defaultAngularFrequency;
                float beginY = (float) (mDefaultWaterLevel + defaultAmplitude * Math.sin(wx));
                canvas.drawLine(beginX, beginY, beginX, endY, wavePaint);
                waveY[beginX] = beginY;
            }

            wavePaint.setColor(mWaveColor);
            int waveShift = (int) defaultWaveLength / 4;
            for (int beginX = 0; beginX < endX; beginX++) {
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
