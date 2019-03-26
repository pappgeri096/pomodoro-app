package com.example.pomodoro.models;

public class TaskItem {
    private String taskName;

    public TaskItem(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
