package com.tony.waveloading;

import com.tony.wavelibrary.WaveLoadingView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        WaveLoadingView waveLoadingView = new WaveLoadingView(this);
        setContentView(waveLoadingView);
    }
}
