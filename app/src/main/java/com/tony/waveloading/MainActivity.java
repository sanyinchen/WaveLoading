package com.tony.waveloading;

import com.tony.wavelibrary.WaveLoadingView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        WaveLoadingView waveLoadingView = new WaveLoadingView(this);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.addView(waveLoadingView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(linearLayout);
    }
}
