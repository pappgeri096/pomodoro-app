package com.example.pomodoro.utils;

import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import com.example.pomodoro.R;

public class CountDownTimer {
    private long millisInFuture;
    private long countDownInterval;
    private Status status;
    private boolean isInitialized = false;
    public CountDownTimer(long pMillisInFuture, long pCountDownInterval) {
        this.millisInFuture = pMillisInFuture;
        this.countDownInterval = pCountDownInterval;
        status = Status.PAUSE;
    }

    public void Stop() {
        status = Status.STOP;
    }

    public long getCurrentTime() {
        return millisInFuture;
    }

    public void Pause() {
        status = Status.PAUSE;
    }

    public void Start() {
        status = Status.START;
    }
    public void Initialize()
    {
        isInitialized = true;
        final Handler handler = new Handler();
        Log.v("status", "starting");
        Log.v("timeLeft", String.valueOf(millisInFuture));
        final Runnable counter = new Runnable(){

            public void run(){
                long sec = millisInFuture/1000;
                if(status == Status.START) {
                    if(millisInFuture <= 0) {
                        Log.v("status", "done");
                        isInitialized = false;
                        handler.removeCallbacks(this, countDownInterval);
                    } else {
                        Log.v("status", Long.toString(sec) + " seconds remain");
                        millisInFuture -= countDownInterval;
                        handler.postDelayed(this, countDownInterval);
                    }
                } else if (status == Status.PAUSE) {
                    Log.v("status", Long.toString(sec) + " seconds remain and timer has stopped!");
                    handler.postDelayed(this, countDownInterval);
                }
                else {
                    Log.v("status", Long.toString(sec) + " seconds remain and timer has stopped!");
                    //millisInFuture = 0;
                    isInitialized = false;
                    handler.removeCallbacks(this, 0);
                }
            }
        };

        handler.postDelayed(counter, countDownInterval);
    }

    public void setMillisInFuture(long millisInFuture) {
        this.millisInFuture = millisInFuture;
    }

    public boolean isInitialized() {
        return isInitialized;
    }
}
