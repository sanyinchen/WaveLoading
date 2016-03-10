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
    private static final float DEFAULT_AMPLITUDE = 0.05f;
    private int mWaveColor;
    private int mTitleSizeSp;
    private int mTitleBoardSize;
    private int mCircleBoardStorkeSize;
    private int mCircleBoardColor;//  growing hoop
    private int mDistancePadding;// the distance of between  growing hoop and  board
    private int mBoardSize;
    private int mTitleColor;
    private float mAmplitudeRatio;
    private float mDistanceOffset;
    private float mOffesetOfCircleBoard;
    private boolean mCenterlTitle;
    private boolean mShowProcess;
    private boolean mShowHoopGrow;

    public boolean ismShowHoopGrow() {
        return mShowHoopGrow;
    }

    public void setmShowHoopGrow(boolean mShowHoopGrow) {
        this.mShowHoopGrow = mShowHoopGrow;
    }

    public boolean ismShowProcess() {
        return mShowProcess;
    }

    public void setmShowProcess(boolean mShowProcess) {
        this.mShowProcess = mShowProcess;
    }

    public boolean ismCenterlTitle() {

        return mCenterlTitle;
    }

    public void setmCenterlTitle(boolean mCenterlTitle) {
        this.mCenterlTitle = mCenterlTitle;
    }

    public float getmOffesetOfCircleBoard() {
        return mOffesetOfCircleBoard;
    }

    public void setmOffesetOfCircleBoard(float mOffesetOfCircleBoard) {
        this.mOffesetOfCircleBoard = mOffesetOfCircleBoard;
    }

    public float getmDistanceOffset() {
        return mDistanceOffset;
    }

    public void setmDistanceOffset(float mDistanceOffset) {
        this.mDistanceOffset = mDistanceOffset;
    }

    private float mHoopPadding;

    public int getmDistancePadding() {
        return mDistancePadding;
    }

    public void setmDistancePadding(int mDistancePadding) {
        this.mDistancePadding = mDistancePadding;
    }

    public float getmHoopPadding() {
        return mHoopPadding;
    }

    public void setmHoopPadding(float mHoopPadding) {
        this.mHoopPadding = mHoopPadding;
    }

    public int getmTitleBoardSize() {
        return mTitleBoardSize;
    }

    public void setmTitleBoardSize(int mTitleBoardSize) {
        this.mTitleBoardSize = mTitleBoardSize;
    }

    public int getmCircleBoardStorkeSize() {
        return mCircleBoardStorkeSize;
    }

    public void setmCircleBoardStorkeSize(int mCircleBoardStorkeSize) {
        this.mCircleBoardStorkeSize = mCircleBoardStorkeSize;
    }

    public int getmCircleBoardColor() {
        return mCircleBoardColor;
    }

    public void setmCircleBoardColor(int mCircleBoardColor) {
        this.mCircleBoardColor = mCircleBoardColor;
    }

    public WaveConfig() {
        mWaveColor = DEFAULT_WAVE_COLOR;
        mTitleColor = DEFAULT_TITLE_COLOR;
        mAmplitudeRatio = DEFAULT_AMPLITUDE;
        mBoardSize = 4;
        mTitleSizeSp = 15;
        mTitleBoardSize = 5;
        mCircleBoardStorkeSize = 5;
        mCircleBoardColor = DEFAULT_WAVE_COLOR;
        mDistancePadding = 0;
        mDistanceOffset = 2f;
        mHoopPadding = 0;
        mOffesetOfCircleBoard = 2f;
        mCenterlTitle = true;
        mShowProcess = true;
        mShowHoopGrow = true;
    }

    public float getmAmplitudeRatio() {
        return mAmplitudeRatio;
    }

    public WaveConfig setmAmplitudeRatio(float mAmplitudeRatio) {
        this.mAmplitudeRatio = mAmplitudeRatio;
        return this;
    }

    public int getmTitleColor() {
        return mTitleColor;
    }

    private WaveLoadChangeInterface waveLoadingInterface;

    public int getmWaveColor() {
        return mWaveColor;
    }

    public int getmTitleSizeSp() {
        return mTitleSizeSp;
    }

    public int getmBoardSize() {
        return mBoardSize;
    }

    public WaveLoadChangeInterface getWaveLoadingInterface() {
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

    public WaveConfig setWaveLoadingInterface(WaveLoadChangeInterface waveLoadingInterface) {
        this.waveLoadingInterface = waveLoadingInterface;
        return this;
    }

    public WaveConfig build(WaveLoadChangeInterface waveLoadingInterface) {
        this.waveLoadingInterface = waveLoadingInterface;
        return this;
    }
}
