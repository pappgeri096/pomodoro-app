package com.example.pomodoro.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pomodoro.R;
import com.example.pomodoro.interfaces.IMainActivity;
import com.example.pomodoro.utils.CountDownTimer;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {


    private IMainActivity mIMainActivity;

    private long workTime = 1500000;
    private long longBreakTime = 1800000;
    private long shortBreak = 300000;

    private View contentView;
    private WifiManager mWifiManager;

    private Button mStartButton;
    private Button mStopBtn;
    private TextView mTimer;
    private TextView mCurrentSession;
    private TextView mTaskName;
    private ProgressBar mProgressBar;

    private SharedPreferences mSharedPreferences;

    private CountDownTimer mCountDownTimer;
    private Refresher mRefresher;

    private boolean isBreak;
    private int complatedSessions;
    private boolean  isRefreshingTimer = false;

    final int PROGRESS_BAR_INCREMENT = (int) Math.ceil(100.0 / 4);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_home, container, false);
        mCountDownTimer = new CountDownTimer(workTime, 1000);
        mRefresher = new Refresher();
        mWifiManager = (WifiManager) this.getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        init();

        setTimerText();

        mStartButton.setOnClickListener((view) -> {
            if(mStartButton.getText().equals("Start")) {
                startTimer();
            }else {
                pauseTimer();
            }
        });

        mStopBtn.setOnClickListener((view) -> {
            stopTimer();
        });

        mTaskName.setOnClickListener((view -> {

        }));

        return contentView;
    }

    private void init() {
        initComponents();
        initTimes();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mIMainActivity = (IMainActivity) getActivity();
    }

    private void initTimes() {
        workTime = mSharedPreferences.getLong("workTime",10000);
        shortBreak = mSharedPreferences.getLong("breakTime",10000);
        longBreakTime = mSharedPreferences.getLong("longBreakTime",10000);

        mCountDownTimer.setMillisInFuture(workTime);
    }

    private void initComponents() {
        mSharedPreferences = this.getActivity().getSharedPreferences("data", MODE_PRIVATE);
        mStartButton = contentView.findViewById(R.id.startBtn);
        mStopBtn = contentView.findViewById(R.id.stopBtn);
        mTimer = contentView.findViewById(R.id.timer);
        mProgressBar = contentView.findViewById(R.id.progressBar);
        mCurrentSession = contentView.findViewById(R.id.currentSession);
        mTaskName = contentView.findViewById(R.id.taskName);
    }

    private void stopTimer() {
        mCountDownTimer.Stop();
        mCountDownTimer.setMillisInFuture(workTime);
        complatedSessions = 0;
        mProgressBar.setProgress(0);
        isBreak = false;
        mCurrentSession.setText("Work");
        mStartButton.setText("Start");
        setTimerText();
        mWifiManager.setWifiEnabled(true);

    }

    private void pauseTimer() {
        mCountDownTimer.Pause();
        mStartButton.setText("Start");
    }

    private void startTimer() {
        if(!mCountDownTimer.isInitialized()) mCountDownTimer.Initialize();

        if(!isRefreshingTimer) {
            isRefreshingTimer = true;
            mRefresher.start();
        }
        System.out.println(mCountDownTimer.getCurrentTime());
        mWifiManager.setWifiEnabled(false);
        mCountDownTimer.Start();
        mStartButton.setText("Pause");
    }

    private void handleProgress() {
        complatedSessions++;
        System.out.println(complatedSessions);
        mProgressBar.incrementProgressBy(PROGRESS_BAR_INCREMENT);
        if(complatedSessions == 4) {
            mCountDownTimer.Pause();
            mCountDownTimer.setMillisInFuture(longBreakTime);
            setTimerText();
            mProgressBar.setProgress(0);
        }
    }

    public void setTimerText() {
        long millis = mCountDownTimer.getCurrentTime();
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        mTimer.setText(String.format("%d:%02d", minutes, seconds));
    }

    private long getWorkTime() {
        long time;
        time = workTime;
        mCurrentSession.setText("Work");
        return time;
    }

    private long getBreakTime() {
        long time;
        String breakText = "Break";
        if(complatedSessions == 4) {
            time = longBreakTime;
            breakText = "Long " + breakText;
        } else {
            time = shortBreak;
        }
        mCurrentSession.setText(breakText);
        return time;
    }

    private void vibratePhone() {
        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(1000);
        }
    }


    class Refresher implements Runnable {

        private Handler mHandler = new Handler();

        @Override
        public void run() {
            setTimerText();

            if(mCountDownTimer.getCurrentTime() > 0) {
                mHandler.postDelayed(this, 100);
            } else {
                if(!isBreak) handleProgress();
                isBreak = !isBreak;
                long time;
                if(isBreak) {
                    time = getBreakTime();
                } else {
                    time = getWorkTime();
                }
                mCountDownTimer.setMillisInFuture(time);
                mCountDownTimer.Pause();
                setTimerText();

                mStartButton.setText("Start");
                isRefreshingTimer = false;
                vibratePhone();
                mWifiManager.setWifiEnabled(true);
                mHandler.removeCallbacks(this,0);
            }
        }

        public void start()
        {
            mHandler.postDelayed(this, 100);
        }
    }

}
