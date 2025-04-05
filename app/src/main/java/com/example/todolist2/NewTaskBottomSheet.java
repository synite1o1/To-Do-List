package com.example.todolist2;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class NewTaskBottomSheet extends BottomSheetDialogFragment {
    private EditText taskInput;
    private DBHelper dbHelper;
    private TaskAddedListener taskAddedListener; // Listener for updating MainActivity

    public interface TaskAddedListener {
        void onTaskAdded();
    }

    public void setTaskAddedListener(TaskAddedListener listener) {
        this.taskAddedListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_schedule, container, false);

        taskInput = view.findViewById(R.id.taskInput);
        Button saveTaskButton = view.findViewById(R.id.saveTaskButton);
        dbHelper = new DBHelper(getContext());

        saveTaskButton.setOnClickListener(v -> {
            String taskText = taskInput.getText().toString().trim();

            if (!taskText.isEmpty()) {
                saveTaskToDatabase(taskText);
                if (taskAddedListener != null) {
                    taskAddedListener.onTaskAdded(); // Notify MainActivity
                }
                dismiss();
            } else {
                Toast.makeText(getContext(), "Please enter a task", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void saveTaskToDatabase(String taskText) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("task_name", taskText);

        long result = db.insert("tasks", null, values);
        if (result != -1) {
            Toast.makeText(getContext(), "Task added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to add task", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
}

