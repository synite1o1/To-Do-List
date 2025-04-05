package com.example.todolist2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class AddSubtaskBottomSheet extends BottomSheetDialogFragment {

    private EditText newSubtaskInput;
    private TextInputEditText dueDateEditText, timeEditText;
    private Button saveSubtaskButton;
    private SubtaskListener subtaskListener;

    public interface SubtaskListener {
        void onSubtaskAdded(String subtaskText, String dueDate, String time);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SubtaskListener) {
            subtaskListener = (SubtaskListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SubtaskListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_dialogue_sub_task, container, false);

        newSubtaskInput = view.findViewById(R.id.newSubtaskInput);
        dueDateEditText = view.findViewById(R.id.dueDateEditText);
        timeEditText = view.findViewById(R.id.timeEditText);
        saveSubtaskButton = view.findViewById(R.id.saveSubtaskButton);

        // Open Date Picker
        dueDateEditText.setOnClickListener(v -> showDatePicker());

        // Open Time Picker
        timeEditText.setOnClickListener(v -> showTimePicker());

        // Save Button Click
        saveSubtaskButton.setOnClickListener(v -> {
            String subtaskText = newSubtaskInput.getText().toString().trim();
            String dueDate = dueDateEditText.getText().toString().trim();
            String time = timeEditText.getText().toString().trim();

            if (subtaskText.isEmpty()) {
                Toast.makeText(getContext(), "Enter a subtask", Toast.LENGTH_SHORT).show();
                return;
            }

            subtaskListener.onSubtaskAdded(subtaskText, dueDate, time);
            dismiss();
        });

        return view;
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    dueDateEditText.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                (view, hourOfDay, minute) -> {
                    String timeFormatted = String.format("%02d:%02d", hourOfDay, minute);
                    timeEditText.setText(timeFormatted);
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true // 24-hour format
        );
        timePickerDialog.show();
    }
}
