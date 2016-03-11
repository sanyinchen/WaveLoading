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
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by sanyinchen on 16/1/19.
 */
public class WaveLoadingView extends View {
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
    private Paint mCircleBoardPaint;
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

    // title font measure
    private float titleMide;
    private float titleAscent;
    private float titleDescent;
    private float titleLeading;
    private float titleheight;
    private volatile boolean isFinish = false;
    private volatile boolean isStart = false;

    public WaveLoadingView(Context context, WaveConfig waveConfig) {
        super(context);
        if (waveConfig == null) {
            new Exception("WaveConfig must not be null");
        }
        this.waveConfig = waveConfig;
        init(context);

    }

    public WaveLoadingView(Context context) {
        super(context);
        this.mContext = context;

    }

    public WaveLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.waveConfig = new WaveConfig();
        init(context);


    }

    public void setConfig(WaveConfig waveConfig) {
        if (waveConfig == null) {
            new Exception("WaveConfig must not be null");
        }
        this.waveConfig = waveConfig;
        init(this.mContext);
    }

    private void init(Context mContext) {
        this.mContext = mContext;
        rect = new RectF();
        handler = new Handler(mContext.getMainLooper());
        waveLevelanimatorSet = new AnimatorSet();
        mAnimatorSet = new AnimatorSet();
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

        // init titleText
        mTextBoardPaint = new Paint();
        mTextBoardPaint.setAntiAlias(true);
        mTextBoardPaint.setColor(waveConfig.getmTitleColor());
        mTextBoardPaint.setStyle(Paint.Style.FILL);
        mTextBoardPaint.setStrokeWidth(waveConfig.getmTitleBoardSize());
        // mTextBoardPaint.setTextSize(waveConfig.getmTitleSizeSp());
        mTextBoardPaint.setTextSize(CommonUtils.dp2px(mContext, waveConfig.getmTitleSizeSp()));

        // init board hoop
        mCircleBoardPaint = new Paint();
        mCircleBoardPaint.setAntiAlias(true);
        mCircleBoardPaint.setColor(waveConfig.getmCircleBoardColor());
        mCircleBoardPaint.setStyle(Paint.Style.STROKE);
        mCircleBoardPaint.setStrokeWidth(waveConfig.getmCircleBoardStorkeSize());

        int w = getWidth();
        int h = getHeight();
        mCanvasSize = w < h ? w : h;

        // Init Animation
        initAnimation();

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
        // if (this.mWavelevel < waveLevelRatio) {
            mWavelevel = waveLevelRatio;
            mTitle = (int) (waveLevelRatio * 100) + "%";
            // Log.d("srcomp_wave", "setWaveLevelRatio-------" + waveLevelRatio);
            invalidate();
      //  }
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
        // Log.d("srcomp_wave", "getmWavelevel:" + mWavelevel);
        float borderWidth = mBorderPaint.getStrokeWidth();
        if (borderWidth > 0) {
            canvas.drawCircle(getWidth() / 2f, getHeight() / 2f,
                    (getWidth() - borderWidth) / 2f - waveConfig.getmOffesetOfCircleBoard(), mBorderPaint);
        }
        float radius = getWidth() / 2f - borderWidth;
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f,
                radius - waveConfig.getmHoopPadding() + waveConfig.getmDistanceOffset(),
                mWavePaint);
        // canvas.drawBitmap(bitmapBuffer, 0, 0, mWavePaint);
        titleMide = mTextBoardPaint.measureText(mTitle);
        titleAscent = mTextBoardPaint.getFontMetrics().ascent;
        titleDescent = mTextBoardPaint.getFontMetrics().descent;
        titleLeading = mTextBoardPaint.getFontMetrics().leading;
        // Log.d("srcomp",
        //         "titleAscent: " + titleAscent + " titleDescent: " + titleDescent + " titleLeading" + titleLeading);
        titleheight = Math.abs(titleDescent - titleAscent + titleLeading);
        drawProcessText(canvas);
        drawGrowingHoop(canvas);

    }

    private void drawGrowingHoop(Canvas canvas) {
        if (waveConfig.ismShowHoopGrow()) {
            canvas.drawArc(rect, -90, 360 * mWavelevel, false, mCircleBoardPaint);
        }
    }

    private void drawProcessText(Canvas canvas) {
        if (waveConfig.ismShowProcess()) {

            if (waveConfig.ismCenterlTitle()) {
                canvas.drawText(mTitle, canvas.getWidth() / 2 - titleMide / 2, mDefaultWaterLevel, mTextBoardPaint);
            } else {
                canvas.drawText(mTitle, canvas.getWidth() / 2 - titleMide / 2,
                        (1 - mWavelevel) * getHeight() >= titleheight ? (1 - mWavelevel)
                                * getHeight() : titleheight,
                        mTextBoardPaint);
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureWidth(heightMeasureSpec);
        int radius = width < height ? width : height;
        rect.set(0 + getrectPadding(), 0 + getrectPadding(),
                radius - getrectPadding(), radius - getrectPadding());
        setMeasuredDimension(radius, radius);
    }

    private float getrectPadding() {
        //return waveConfig.getmCircleBoardStorkeSize() + waveConfig.getmHoopPadding();
        // return mBorderPaint.getStrokeWidth() + waveConfig.getmHoopPadding();
        if (Math.abs(waveConfig.getmHoopPadding() - 0) >= 0.001) {
            return waveConfig.getmHoopPadding();
        } else {
            return mBorderPaint.getStrokeWidth();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Log.d("srcomp_wave", "onSizeChanged-------");
        initWaveShader();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

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

    public void progressStart() {
        if (waveConfig != null && waveConfig.getWaveLoadingInterface() != null) {
            waveConfig.getWaveLoadingInterface().onProgressStart();
        } else {
            Log.d("demo", "waveConfig is null");
        }
    }

    public void progressFinish() {
        if (!isFinish) {
            if (waveConfig != null && waveConfig.getWaveLoadingInterface() != null) {
                waveConfig.getWaveLoadingInterface().onProgressFinish();
            }
            isFinish = true;
            isStart = false;
        }

    }

    public void setProgress(int process) {
        if (!isStart) {
            isStart = true;
            isFinish = false;
            progressStart();

        }
        process = process >= 0 ? process : 0;
        process = process <= 100 ? process : 100;
        perCent = (float) process / 100.0f;
        setProgressValue(perCent);
        if (waveConfig != null && waveConfig.getWaveLoadingInterface() != null) {
            waveConfig.getWaveLoadingInterface().onProgress(process);
        }

        if (Math.abs(perCent - 1) <= 0.00000001) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressFinish();
                }
            }, 100);
        }
    }

}
