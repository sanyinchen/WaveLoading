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
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    WaveLoadingView[] waveLoadingViews = new WaveLoadingView[6];
    WaveConfig[] waveConfigs = new WaveConfig[6];
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        getConfigs();
        for (int i = 1; i <= 6; i++) {
            waveLoadingViews[i - 1] =
                    (WaveLoadingView) findViewById(this.getResources().getIdentifier("wave_type" + i, "id",
                            this.getPackageName()));
        }
        for (int i = 0; i < waveConfigs.length; i++) {
            waveLoadingViews[i].setConfig(waveConfigs[i]);
        }
        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                for (int i = 0; i < 6; i++) {
                    waveLoadingViews[i].setProgress(seekBar.getProgress());
                }
            }
        });
        // waveConfig.setmHoopPadding(5);

    }

    private void getConfigs() {
        for (int i = 0; i < 6; i++) {
            waveConfigs[i] = new WaveConfig();
        }
        waveConfigs[0].setmWaveColor(Color.parseColor("#EE82EE"));
        waveConfigs[0].setmTitleColor(Color.RED);
        waveConfigs[0].setmCircleBoardColor(Color.RED);
        waveConfigs[0].setmTitleSizeSp(15);
        // waveConfig.setmShowHoopGrow(false);
        // waveConfig.setmShowProcess(false);
        waveConfigs[0].setmCenterlTitle(false);
        waveConfigs[0].setWaveLoadingInterface(new WaveLoadChangeInterface() {
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

        waveConfigs[1].setmShowHoopGrow(true);
        waveConfigs[1].setmCircleBoardColor(Color.GREEN);

        waveConfigs[3].setmHoopPadding(5);
        waveConfigs[3].setmCircleBoardColor(Color.BLUE);

        waveConfigs[4].setmShowHoopGrow(true);
        waveConfigs[4].setmCircleBoardColor(Color.YELLOW);

        waveConfigs[5].setmShowProcess(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
