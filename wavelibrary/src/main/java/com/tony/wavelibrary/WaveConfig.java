/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.tony.wavelibrary;

import android.graphics.Color;

/**
 * Created by sanyinchen on 16/1/19.
 */
public class WaveConfig {
    private static final int DEFAULT_WAVE_COLOR = Color.parseColor("#FF4081");
    private static final int DEFAULT_TITLE_COLOR = Color.parseColor("#212121");
    private static final float DEFAULT_WATER_LEVEL_RATIO = 0.1f;
    private int mWaveColor;
    private int mTitleSizeSp;
    private int mBoardSize;
    private int mTitleColor;
    private float mWavelevel; //0.1-1f

    public WaveConfig() {
        mWaveColor = DEFAULT_WAVE_COLOR;
        mTitleColor = DEFAULT_TITLE_COLOR;
        mBoardSize = 4;
        mTitleSizeSp = 15;
        mWavelevel = DEFAULT_WATER_LEVEL_RATIO;
    }

    public float getmWavelevel() {
        return mWavelevel;
    }

    public WaveConfig setmWavelevel(float mWavelevel) {
        this.mWavelevel = mWavelevel;
        return this;
    }

    public int getmTitleColor() {
        return mTitleColor;
    }

    private WaveLoadingInterface waveLoadingInterface;

    public int getmWaveColor() {
        return mWaveColor;
    }

    public int getmTitleSizeSp() {
        return mTitleSizeSp;
    }

    public int getmBoardSize() {
        return mBoardSize;
    }

    public WaveLoadingInterface getWaveLoadingInterface() {
        return waveLoadingInterface;
    }

    public WaveConfig setmWaveColor(int mWaveColor) {
        this.mWaveColor = mWaveColor;
        return this;
    }

    public WaveConfig setmTitleSizeSp(int mTitleSizeSp) {
        this.mTitleSizeSp = mTitleSizeSp;
        return this;
    }

    public WaveConfig setmBoardSize(int mBoardSize) {
        this.mBoardSize = mBoardSize;
        return this;
    }

    public WaveConfig setmTitleColor(int mTitleColor) {
        this.mTitleColor = mTitleColor;
        return this;
    }

    public WaveConfig setWaveLoadingInterface(WaveLoadingInterface waveLoadingInterface) {
        this.waveLoadingInterface = waveLoadingInterface;
        return this;
    }

    public WaveConfig build(WaveLoadingInterface waveLoadingInterface) {
        this.waveLoadingInterface = waveLoadingInterface;
        return this;
    }
}
