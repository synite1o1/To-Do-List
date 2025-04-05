package com.example.todolist2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class TaskDetailActivity extends AppCompatActivity implements AddSubtaskBottomSheet.SubtaskListener {
    private RecyclerView subtaskRecyclerView;
    private SubtaskAdapter subtaskAdapter;
    private DBHelper dbHelper;
    private int taskId;
    private String taskName;
    private Button addSubtaskButton;
    private ArrayList<Subtask> subtaskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        dbHelper = new DBHelper(this);

        // Get task data from intent
        Intent intent = getIntent();
        taskId = intent.getIntExtra("TASK_ID", -1);
        taskName = intent.getStringExtra("TASK_NAME");

        if (taskId == -1) {
            Toast.makeText(this, "Error loading task", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set Task Title
        TextView taskTitle = findViewById(R.id.taskTitle);
        taskTitle.setText(taskName);

        // Initialize Views
        subtaskRecyclerView = findViewById(R.id.subtaskRecyclerView);
        addSubtaskButton = findViewById(R.id.addSubtaskButton);

        // Setup RecyclerView
        subtaskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadSubtasks();

        // Add button click listener to open Bottom Sheet
        addSubtaskButton.setOnClickListener(v -> {
            AddSubtaskBottomSheet bottomSheet = new AddSubtaskBottomSheet();
            bottomSheet.show(getSupportFragmentManager(), "AddSubtaskBottomSheet");
        });
    }

    private void loadSubtasks() {
        subtaskList = dbHelper.getSubtasksForTask(taskId);
        subtaskAdapter = new SubtaskAdapter(this, subtaskList, dbHelper);
        subtaskRecyclerView.setAdapter(subtaskAdapter);
    }

    // 👇 Updated to support date and time
    @Override
    public void onSubtaskAdded(String subtaskText, String dueDate, String time) {
        // Save to DB (you’ll need to update DBHelper and Subtask class to support these fields)
        dbHelper.addSubtask(taskId, subtaskText, dueDate, time);

        Subtask newSubtask = new Subtask(
                subtaskList.size() + 1,
                taskId,
                subtaskText,
                false,
                dueDate,
                time
        );

        subtaskList.add(newSubtask);
        subtaskAdapter.notifyItemInserted(subtaskList.size() - 1);
    }
}
