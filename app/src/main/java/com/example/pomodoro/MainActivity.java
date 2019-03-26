package com.example.pomodoro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.pomodoro.fragments.HomeFragment;
import com.example.pomodoro.fragments.SettingsFragment;
import com.example.pomodoro.fragments.StatisticFragment;
import com.example.pomodoro.fragments.TasksFragment;
import com.example.pomodoro.interfaces.IMainActivity;
import com.example.pomodoro.models.TaskItem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements IMainActivity {

    private HomeFragment mHomeFragment = new HomeFragment();
    private TasksFragment mTasksFragment = new TasksFragment();
    private SettingsFragment mSettingsFragment = new SettingsFragment();
    private StatisticFragment mStatisticFragment = new StatisticFragment();
    private  SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);

        if(!sharedPreferences.contains("workTime")) {
            saveData();
            System.out.println("Run!!!");
        };

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();

        navigationView.setOnNavigationItemSelectedListener((item) -> {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.nav_home:
                    fragment = mHomeFragment;
                break;
                case R.id.nav_tasks:
                    fragment = mTasksFragment;
                break;
                case R.id.nav_settings:
                    fragment = mSettingsFragment;
                 break;

                case R.id.nav_stat:
                    fragment = mStatisticFragment ;
                break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    fragment).commit();

            return true;
        });

    }

    private void saveData() {

        SharedPreferences.Editor editor = sharedPreferences.edit();

        ArrayList<TaskItem> taskItems = new ArrayList<>();
        taskItems.add(new TaskItem("Programming"));
        taskItems.add(new TaskItem("Exercise"));
        taskItems.add(new TaskItem("Reading"));

        Gson gson = new Gson();
        String data = gson.toJson(taskItems);

        editor.putLong("workTime", 1500000);
        editor.putLong("breakTime", 300000);
        editor.putLong("longBreakTime", 1800000);
        editor.putString("taskList", data);
        editor.apply();
    }

    @Override
    public void setDurationTime(long time, int type) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (type == 1) {
            editor.putLong("workTime", time);
        } else if (type == 2) {
            editor.putLong("breakTime", time);
        } else {
            editor.putLong("longBreakTime", time);
        }
        editor.apply();
    }


}
