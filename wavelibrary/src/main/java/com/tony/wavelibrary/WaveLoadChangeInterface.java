/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.tony.wavelibrary;

/**
 * Created by sanyinchen on 16/1/19.
 */
public interface WaveLoadChangeInterface {

    void onProgressStart();

    void onProgressFinish();

    void onProgress(int process);
}
