package com.example.todolist2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class SubtaskAdapter extends RecyclerView.Adapter<SubtaskAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Subtask> subtaskList;
    private DBHelper dbHelper;

    public SubtaskAdapter(Context context, ArrayList<Subtask> subtaskList, DBHelper dbHelper) {
        this.context = context;
        this.subtaskList = subtaskList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.subtask_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subtask subtask = subtaskList.get(position);
        holder.subtaskText.setText(subtask.getSubtaskText());

        // Set due date and time if available
        String dueDate = subtask.getDueDate();
        String time = subtask.getTime();
        if ((dueDate != null && !dueDate.isEmpty()) || (time != null && !time.isEmpty())) {
            holder.dueDateTime.setVisibility(View.VISIBLE);
            holder.dueDateTime.setText("Due: " + (dueDate != null ? dueDate : "") +
                    (time != null && !time.isEmpty() ? " " + time : ""));
        } else {
            holder.dueDateTime.setVisibility(View.GONE);
        }

        // Set checkbox without triggering the listener initially
        holder.subtaskCheckbox.setOnCheckedChangeListener(null);
        holder.subtaskCheckbox.setChecked(subtask.isChecked());

        holder.subtaskCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (subtask.isChecked() != isChecked) {
                dbHelper.updateSubtaskStatus(subtask.getSubtaskId(), isChecked);
                subtask.setChecked(isChecked);
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            dbHelper.deleteSubtask(subtask.getSubtaskId());
            subtaskList.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return subtaskList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox subtaskCheckbox;
        TextView subtaskText, dueDateTime;
        ImageView deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subtaskCheckbox = itemView.findViewById(R.id.subtaskCheckbox);
            subtaskText = itemView.findViewById(R.id.subtaskText);
            dueDateTime = itemView.findViewById(R.id.dueDateTime);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
