package com.example.todolist2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TaskAdapterAllToDo taskAdapter;
    private DBHelper dbHelper;
    private Button newListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        newListButton = findViewById(R.id.newListButton);
        dbHelper = new DBHelper(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadTasks();

        // Navigate to NewListActivity when the "New List" button is clicked
        newListButton.setOnClickListener(v -> {
            NewTaskBottomSheet bottomSheet = new NewTaskBottomSheet();
            bottomSheet.setTaskAddedListener(() -> loadTasks()); // Refresh task list
            bottomSheet.show(getSupportFragmentManager(), "NewTaskBottomSheet");
        });
    }

    private void loadTasks() {
        ArrayList<ToDoItem> taskList = dbHelper.getAllTasks();
        taskAdapter = new TaskAdapterAllToDo(this, taskList, dbHelper);
        recyclerView.setAdapter(taskAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks(); // Refresh task list when returning to MainActivity
    }
}
