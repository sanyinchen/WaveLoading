package com.tony.waveloading;

import com.tony.wavelibrary.WaveConfig;
import com.tony.wavelibrary.WaveLoadingView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    Thread thread;
    float temp;
    WaveConfig waveConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        waveConfig = new WaveConfig();
        waveConfig.setmWavelevel(0.55f);
        WaveLoadingView waveLoadingView = new WaveLoadingView(this, waveConfig);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.addView(waveLoadingView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(linearLayout);
        temp = 0f;
        thread = new Thread() {
            @Override
            public void run() {

                while (Math.abs(temp-1f)>0.001) {
                    Log.d("srcomp_wave", "temp:" + temp);
                    temp = temp + 0.1f;
                    waveConfig.setmWavelevel(temp);
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
