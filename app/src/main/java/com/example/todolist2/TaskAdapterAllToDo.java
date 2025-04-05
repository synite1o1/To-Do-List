package com.example.todolist2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class TaskAdapterAllToDo extends RecyclerView.Adapter<TaskAdapterAllToDo.ViewHolder> {
    private Context context;
    private ArrayList<ToDoItem> taskList;
    private DBHelper dbHelper;

    public TaskAdapterAllToDo(Context context, ArrayList<ToDoItem> taskList, DBHelper dbHelper) {
        this.context = context;
        this.taskList = taskList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_to_do_recyclerlist, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ToDoItem task = taskList.get(position);
        holder.taskText.setText(task.getToDoText());

        // Click listener to open TaskDetailActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TaskDetailActivity.class);
            intent.putExtra("TASK_ID", task.getId()); // Pass the task ID
            intent.putExtra("TASK_NAME", task.getToDoText()); // Pass the task name
            context.startActivity(intent);
        });

        // Delete task on button click
        holder.deleteButton.setOnClickListener(v -> {
            dbHelper.deleteTask(task.getId());
            taskList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, taskList.size());
        });
    }
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        ToDoItem task = taskList.get(position);
//        holder.taskText.setText(task.getToDoText());
//
//        // Delete task on button click
//        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dbHelper.deleteTask(task.getId());
//                taskList.remove(position);
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, taskList.size());
//            }
//        });
//    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskText;
        ImageView deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskText = itemView.findViewById(R.id.taskText);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
