package com.example.pomodoro.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pomodoro.R;
import com.example.pomodoro.models.TaskItem;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private ArrayList<TaskItem> items;

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        private TextView mTaskName;

        public TaskViewHolder(View itemView) {
            super(itemView);
            mTaskName = itemView.findViewById(R.id.taskName);
        }
    }

    public TaskAdapter(ArrayList<TaskItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
       TaskViewHolder taskViewHolder = new TaskViewHolder(view);
       return taskViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskItem taskItem = items.get(position);

        holder.mTaskName.setText(taskItem.getTaskName());

        holder.mTaskName.setOnClickListener((view -> {

        }));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
