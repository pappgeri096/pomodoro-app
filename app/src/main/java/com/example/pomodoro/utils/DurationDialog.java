package com.example.pomodoro.utils;

import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.pomodoro.R;

public class DurationDialog extends DialogFragment {

    private Button cancelBtn, okBtn;
    private EditText duration;
    private int type;

    public interface OnDurationUpdated {
        void updateDuration(long time, int type);
    }

    public OnDurationUpdated mOnDurationUpdated;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dialog_times, container, false);

        cancelBtn = view.findViewById(R.id.cancelBtn);
        okBtn = view.findViewById(R.id.okBtn);

        duration = view.findViewById(R.id.newTime);

        cancelBtn.setOnClickListener((view1 -> {
            getDialog().dismiss();
        }));

        okBtn.setOnClickListener(view1 -> {
            String input = duration.getText().toString();
            if(!input.equals("")) {
                mOnDurationUpdated.updateDuration(Long.parseLong(input), type);
            }
            getDialog().dismiss();
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnDurationUpdated = (OnDurationUpdated) getTargetFragment();
        } catch (ClassCastException e) {
            System.out.println("Not ok");
        }
    }

    public void setType(int type) {
        this.type = type;
    }
}
