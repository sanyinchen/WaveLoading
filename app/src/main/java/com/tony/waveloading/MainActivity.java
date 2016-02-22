package com.tony.waveloading;

import com.tony.wavelibrary.WaveConfig;
import com.tony.wavelibrary.WaveLoadingView;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    Thread thread;
    int temp;
    WaveConfig waveConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        waveConfig = new WaveConfig();
        waveConfig.setmWaveColor(Color.parseColor("#EE82EE"));
        waveConfig.setmTitleColor(Color.BLUE);
        waveConfig.setmTitleSizeSp(30);
        final WaveLoadingView waveLoadingView = new WaveLoadingView(this, waveConfig);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.addView(waveLoadingView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(linearLayout);
        temp = 0;
        thread = new Thread() {
            @Override
            public void run() {

                while (temp <= 100) {
                    // Log.d("srcomp_wave", "temp:" + temp);
                    temp = temp + 10;
                    waveLoadingView.onProcess(temp);
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        thread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (thread.isAlive()) {
            thread.stop();
        }
    }
}
