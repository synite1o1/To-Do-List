package com.example.todolist2;

public class Subtask {
    private int subtaskId;
    private int taskId;
    private String subtaskText;
    private boolean isChecked;
    private String dueDate;
    private String time;

    // Full Constructor
    public Subtask(int subtaskId, int taskId, String subtaskText, boolean isChecked, String dueDate, String time) {
        this.subtaskId = subtaskId;
        this.taskId = taskId;
        this.subtaskText = subtaskText;
        this.isChecked = isChecked;
        this.dueDate = dueDate;
        this.time = time;
    }

    // Getters and Setters
    public int getSubtaskId() {
        return subtaskId;
    }

    public void setSubtaskId(int subtaskId) {
        this.subtaskId = subtaskId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getSubtaskText() {
        return subtaskText;
    }

    public void setSubtaskText(String subtaskText) {
        this.subtaskText = subtaskText;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
