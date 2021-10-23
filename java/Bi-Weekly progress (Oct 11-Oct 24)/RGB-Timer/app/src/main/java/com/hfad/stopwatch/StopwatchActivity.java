package com.hfad.stopwatch;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Arrays;


public class StopwatchActivity extends Activity {

    protected int seconds = 0;//秒數
    protected boolean running;//運行狀態
    protected boolean wasRunning;//檢測是否維持運行
    protected boolean randomising;//判斷是否要生成隨機數字
    protected Vibrator mVibrator;//震動器
    protected int[] RGBcolor = new int[3];//目前文本的顏色(RGB)值
    //protected boolean colorAdd ; //判斷顏色的變化
    protected final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE); //獲取震動效果的系統服務
        Arrays.fill(RGBcolor, 0);
        if (savedInstanceState != null){ //獲取已保存實例的值
            //再次創建活動時獲取savedInstanceState 中保存變量的值
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
            randomising = savedInstanceState.getBoolean("randomising");
            //RGBcolor = savedInstanceState.getIntArray("RGBcolor");
        }
        VibratorRunner();//震動提示的線程
        RandomTimer();//生成隨機時間的線程
        RGBchanger();//RGB刷新時間文本的線程
        runTimer();//顯示時間的線程
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {//活動撤銷前通過onSaveInstanceState自動保存當前變量的值
        savedInstanceState.putInt("seconds", seconds);//用鍵:值的方式存在當前實例的值
        savedInstanceState.putBoolean("running",running);
        savedInstanceState.putBoolean("wasRunning",wasRunning);
        savedInstanceState.putBoolean("randomising",randomising);
        savedInstanceState.putIntArray("RGBcolor",RGBcolor);
        //savedInstanceState.putBoolean("colorAdd",colorAdd);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (wasRunning) {
            running = true;
        }
    }

    @Override
    protected void onStop(){//覆蓋Android 生命周期方法時必須要調用超類中的方法
        super.onStop();
        wasRunning = running;
        running = false;

    }

    protected void runTimer(){//用作運行秒表
        Timing task = new Timing();
        handler.post(task); // post表示馬上啟動
    }

    protected void RandomTimer(){//生成隨機時間
        RandomTiming task = new RandomTiming();
        handler.post(task);
    }

    class Timing implements Runnable{

        @Override
        public void run(){
            final TextView timeView = (TextView) findViewById(R.id.time_view);
            int hours = seconds / 3600 ;
            int minutes = (seconds % 3600) / 60;
            int secs = seconds % 60 ;
            String time = String.format("%d:%02d:%02d",hours,minutes,secs);
            timeView.setText(time);

            if (running && seconds > 0){
                seconds--;
            }

            handler.postDelayed(this, 1000);//每1秒自動調用自身(計時)一次
        }
    }

    public void onTimerStatus(View view){
        running = ((ToggleButton) view).isChecked();
    }

    public void onClickReset(View view){
        running = false;
        seconds = 0;
    }

    public void onCreateRandomNumber(View view){
        randomising = ((ToggleButton) view).isChecked();
    }

    public void onAddSeconds(View view){
        seconds++;
    }

    class RandomTiming implements Runnable{

        @Override
        public void run() {
            if (randomising){
                seconds = (int) (Math.random() * 3600);
                final TextView timeView = (TextView) findViewById(R.id.time_view);
                int hours = seconds / 3600 ;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60 ;
                String time = String.format("%d:%02d:%02d",hours,minutes,secs);
                timeView.setText(time);
            }
            handler.postDelayed(this,50);
        }
    }

    protected void VibratorRunner(){
        VibratorRunning task = new VibratorRunning();
        handler.post(task);
    }

    class VibratorRunning implements Runnable{

        @Override
        public void run() {
            if (seconds == 0 && running){
                long[] pattern = {200, 200 };//震動0.2秒
                mVibrator.vibrate(pattern, -1);// -1：表示不重复 0：循环的震动
            }
            handler.postDelayed(this,1000);
        }
    }

    protected void RGBchanger (){
        RGBchanging task = new RGBchanging();
        handler.post(task);
    }

    class RGBchanging implements Runnable{

        @Override
        public void run() {
            if (running){
                TextView timeView = (TextView) findViewById(R.id.time_view);
                if (RGBcolor[0] < 252 && RGBcolor[1]  < 252 && RGBcolor[2] < 252) {
                    for (int x = 0; x < RGBcolor.length; x++) {
                        //RGBcolor[x] = (int) (Math.random() * 255);
                        RGBcolor[x] += (int) (Math.random() * 7);
                    }
                }else{
                    for (int x = 0; x < RGBcolor.length; x++) {
                        RGBcolor[x] = (int) (Math.random() * 255);
                    }
                }
                timeView.setTextColor(Color.rgb(RGBcolor[0], RGBcolor[1], RGBcolor[2]));
            }
            handler.postDelayed(this,77);
        }
    }

}
