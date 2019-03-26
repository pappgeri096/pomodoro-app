package com.example.pomodoro.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pomodoro.R;
import com.example.pomodoro.adapters.TaskAdapter;
import com.example.pomodoro.models.TaskItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TasksFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SharedPreferences mSharedPreferences;
    private ArrayList<TaskItem> mTaskItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_tasks, container, false);

        mSharedPreferences = this.getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        System.out.println(mSharedPreferences.getLong("workTime", 10000));
        Gson gson = new Gson();
        String items = mSharedPreferences.getString("taskList", null);
        Type type = new TypeToken<ArrayList<TaskItem>>() {}.getType();
        mTaskItems = gson.fromJson(items, type);
        if(mTaskItems==null) {
            System.out.println("hello");
            mTaskItems = new ArrayList<>();
        }

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new TaskAdapter(mTaskItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);



        return view;
    }
}
