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
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by sanyinchen on 16/1/19.
 */
public class WaveLoadingView extends View implements WaveLoadChangeInterface {
    // draw
    private Context mContext;
    private RectF rect;
    private float perCent = 0.1f;
    // Dynamic Properties.
    private int mWaveColor;
    private WaveConfig waveConfig;

    // Properties
    private int mCanvasSize;
    private float mWaveShiftRatio = DEFAULT_WAVE_SHIFT_RATIO;
    private String mTitle = "0%";
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

    private Paint mTextBoardPaint;
    private float mWavelevel;
    private Handler handler;
    // Animation.
    private AnimatorSet mAnimatorSet;
    private AnimatorSet waveLevelanimatorSet;
    // Default config
    private static final float DEFAULT_AMPLITUDE_RATIO = 0.1f;
    private static final float DEFAULT_WATER_LEVEL_RATIO = 0.5f;
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
        rect = new RectF();
        handler = new Handler(mContext.getMainLooper());
        waveLevelanimatorSet = new AnimatorSet();
        mAnimatorSet = new AnimatorSet();
        // Log.d("srcomp_wave", "waveLevel-------" + waveConfig.getmWavelevel());
        // Init Wave.
        mShaderMatrix = new Matrix();
        mWavePaint = new Paint();
        // The ANTI_ALIAS_FLAG bit AntiAliasing smooths out the edges of what is being drawn,
        // but is has no impact on the interior of the shape.
        mWavePaint.setAntiAlias(true);

        mWaveColor = waveConfig.getmWaveColor();

        // init Border
        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(waveConfig.getmBoardSize());
        mBorderPaint.setColor(waveConfig.getmWaveColor());

        mTextBoardPaint = new Paint();
        mTextBoardPaint.setAntiAlias(true);
        mTextBoardPaint.setColor(waveConfig.getmTitleColor());
        mTextBoardPaint.setStyle(Paint.Style.STROKE);
        mTextBoardPaint.setStrokeWidth(5);
        // mTextBoardPaint.setTextSize(waveConfig.getmTitleSizeSp());
        mTextBoardPaint.setTextSize(50f);

        int w = getWidth();
        int h = getHeight();
        mCanvasSize = w < h ? w : h;

        // Init Animation
        initAnimation();
//        canvas.drawText(String.format("%d%%", progress), startX - txtMarginX, startY + txtMarginY,
//                textPaint);
//        canvas.restore();
    }

    private void setProgressValue(float level) {
        ObjectAnimator waveLevel =
                ObjectAnimator.ofFloat(this, "waveLevelRatio", mWavelevel, level);
        waveLevel.setDuration(1000);
        waveLevel.setInterpolator(new DecelerateInterpolator());

        waveLevelanimatorSet.play(waveLevel);
        handler.post(new Runnable() {
            @Override
            public void run() {
                waveLevelanimatorSet.start();
            }
        });

    }

    public void setWaveLevelRatio(float waveLevelRatio) {
        if (this.mWavelevel < waveLevelRatio) {
            mWavelevel = waveLevelRatio;
            mTitle = (int) (waveLevelRatio * 100) + "%";
            // Log.d("srcomp_wave", "setWaveLevelRatio-------" + waveLevelRatio);
            invalidate();
        }
    }

    private void initAnimation() {
        ObjectAnimator waveShift = ObjectAnimator.ofFloat(this, "waveShiftRatio", 0f, 1f);
        waveShift.setRepeatCount(ValueAnimator.INFINITE);
        waveShift.setDuration(1000);
        waveShift.setInterpolator(new LinearInterpolator());
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
        mCanvasSize = canvas.getWidth();
        mShaderMatrix.setScale(1, waveConfig.getmAmplitudeRatio() / DEFAULT_AMPLITUDE_RATIO, 0, mDefaultWaterLevel);
        mShaderMatrix.postTranslate((float) (mWaveShiftRatio * getWidth()),
                (DEFAULT_WATER_LEVEL_RATIO - mWavelevel) * getHeight());
        mWaveShader.setLocalMatrix(mShaderMatrix);
        // Log.d("srcomp_wave", "getmWavelevel:" + waveConfig.getmWavelevel());
        float borderWidth = mBorderPaint.getStrokeWidth();
        if (borderWidth > 0) {
            canvas.drawCircle(getWidth() / 2f, getHeight() / 2f,
                    (getWidth() - borderWidth) / 2f - 5f, mBorderPaint);
        }
        float radius = getWidth() / 2f - borderWidth;
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius - 5, mWavePaint);
        // canvas.drawBitmap(bitmapBuffer, 0, 0, mWavePaint);
        float midle = mTextBoardPaint.measureText(mTitle);
        canvas.drawText(mTitle, getWidth() / 2 - midle, mDefaultWaterLevel, mTextBoardPaint);
        canvas.drawArc(rect, -90, 360 * perCent, false, mTextBoardPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureWidth(heightMeasureSpec);
        int radius = width < height ? width : height;
        rect.set(0 + 5, 0 + 5, radius - 5, radius - 5);
        setMeasuredDimension(radius, radius);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Log.d("srcomp_wave", "onSizeChanged-------");
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

    @Override
    public void onProgressFinish() {

    }

    @Override
    public void onProcess(int process) {
        process = process >= 0 ? process : 0;
        process = process <= 100 ? process : 100;
        perCent = (float) process / 100.0f;
        setProgressValue(perCent);
    }
}
