package com.tony.waveloading;

import com.tony.wavelibrary.WaveConfig;
import com.tony.wavelibrary.WaveLoadChangeInterface;
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
        waveConfig.setmTitleColor(Color.RED);
        waveConfig.setmCircleBoardColor(Color.RED);
        waveConfig.setmTitleSizeSp(20);
        // waveConfig.setmShowHoopGrow(false);
        // waveConfig.setmShowProcess(false);
        waveConfig.setmCenterlTitle(false);
        waveConfig.setWaveLoadingInterface(new WaveLoadChangeInterface() {
            @Override
            public void onProgressStart() {
                Log.d("demon", "--------start");
            }

            @Override
            public void onProgressFinish() {
                Log.d("demon", "--------end");
            }

            @Override
            public void onProgress(int process) {
                Log.d("demon", "--------process: " + process);
            }
        });
        // waveConfig.setmHoopPadding(5);
        final WaveLoadingView waveLoadingView = new WaveLoadingView(this, waveConfig);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.addView(waveLoadingView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(linearLayout);
        temp = 0;
        thread = new Thread() {
            @Override
            public void run() {

                while (temp < 100) {
                    // Log.d("srcomp_wave", "temp:" + temp);
                    temp = temp + 10;
                    waveLoadingView.setProgress(temp);
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
