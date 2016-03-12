# WaveLoading
##Describe  
This is a loading ui lib.The lib is inspired by another [WaveLoading](https://github.com/tangqi92/WaveLoadingView) project.There are a config object to set up your wave loading.So you can set  a variety of loading style,and it includes three status callback in order to do your action.
##ScreenShot  

![](https://github.com/sanyinchen/WaveLoading/blob/master/screenshot/scrennshot.gif)

##Dependencies  


	repositories {
			...
			maven { url "https://jitpack.io" }
	}
	
	dependencies {
	        compile 'com.github.sanyinchen:WaveLoading:2.1'
	}
	
##How to use
You can use two styles to instance the WaveLoadingView object.  

+ ```WaveLoadingView waveLoading =new WaveLoadingView();  ```

+ 

```
<com.tony.wavelibrary.WaveLoadingView
                android:id="@+id/waveLoading"
                android:layout_height="wrap_content"
                android:layout_width="wrap_content"
                
        />
```
Now you can use the WaveConfig to config your loading.  

```
WaveConfig  waveConfigs = new WaveConfig();
waveConfigs.setmWaveColor(Color.parseColor("#EE82EE"));
        waveConfigs.setmTitleColor(Color.RED);
        waveConfigs.setmCircleBoardColor(Color.RED);
        waveConfigs.setmTitleSizeSp(15);
        waveConfigs.setmCenterlTitle(false);
        waveConfigs.setWaveLoadingInterface(new WaveLoadChangeInterface() {
            @Override
            public void onProgressStart() {
               // do something
            }

            @Override
            public void onProgressFinish() {
                // do something
            }

            @Override
            public void onProgress(int process) {
               // do something
            }
        });
```
e.g

Finally, set the config to your waveLoading instance.  

```waveLoadingView.setConfig(waveConfigs);```

##Summary  
* If you have any question,please email to me(My email:sanyinchen@gmail.com)
* Welcome to subscribe my [google+](https://plus.google.com/u/0/100465464266192894461)  