package com.example.pomodoro.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pomodoro.R;
import com.example.pomodoro.interfaces.IMainActivity;
import com.example.pomodoro.utils.DurationDialog;

public class SettingsFragment extends Fragment implements DurationDialog.OnDurationUpdated {

    private IMainActivity mIMainActivity;
    private TextView workTimeText;
    private TextView workTimeDurationText;
    private TextView breakTimeText;
    private TextView longBreakTimeText;
    private TextView breakTimeDurationText;
    private TextView longBreakDurationText;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        workTimeText = view.findViewById(R.id.workTimeText);
        workTimeDurationText = view.findViewById(R.id.workTime);
        breakTimeText = view.findViewById(R.id.breakTimeText);
        breakTimeDurationText = view.findViewById(R.id.breakTime);
        longBreakTimeText = view.findViewById(R.id.longBreakTimeText);
        longBreakDurationText = view.findViewById(R.id.longBreakTime);

        mSharedPreferences = this.getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);

        setText(mSharedPreferences.getLong("workTime", 10000), workTimeDurationText);
        setText(mSharedPreferences.getLong("breakTime", 10000), breakTimeDurationText);

        workTimeDurationText.setOnClickListener((view1 -> {
            DurationDialog dialog = new DurationDialog();
            dialog.setTargetFragment(SettingsFragment.this, 1);
            dialog.setType(1);
            dialog.show(getFragmentManager(),"");
        }));

        breakTimeDurationText.setOnClickListener((view1 -> {
            DurationDialog dialog = new DurationDialog();
            dialog.setTargetFragment(SettingsFragment.this, 2);
            dialog.setType(2);
            dialog.show(getFragmentManager(),"");
        }));

        longBreakDurationText.setOnClickListener((view1 -> {
            DurationDialog dialog = new DurationDialog();
            dialog.setTargetFragment(SettingsFragment.this, 3);
            dialog.setType(2);
            dialog.show(getFragmentManager(),"");
        }));

        return view;
    }

    private void setText(long millisec, TextView text) {
        int seconds = (int) (millisec / 1000);
        int minutes = seconds / 60;
        text.setText(""+minutes);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mIMainActivity = (IMainActivity) getActivity();
    }

    @Override
    public void updateDuration(long time, int requestcode) {
        System.out.println(requestcode);
        long millisec = 60000 * time;
        mIMainActivity.setDurationTime(millisec, requestcode);

        if (requestcode == 1) {
            workTimeDurationText.setText(""+time);

        } else if (requestcode == 2) {
            breakTimeDurationText.setText(""+time);

        } else if(requestcode == 3) {
            longBreakDurationText.setText(""+time);
        }

    }

}
