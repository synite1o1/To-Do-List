package com.example.todolist2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ToDoListDB";
    private static final int DATABASE_VERSION = 3; // Incremented version

    private static final String TABLE_TASKS = "tasks";
    private static final String COLUMN_TASK_ID = "taskid";
    private static final String COLUMN_TASK_NAME = "task_name";

    private static final String TABLE_SUBTASKS = "subtasks";
    private static final String COLUMN_SUBTASK_ID = "subtaskid";
    private static final String COLUMN_SUBTASK_TEXT = "subtask_text";
    private static final String COLUMN_SUBTASK_STATUS = "status"; // 0 = unchecked, 1 = checked
    private static final String COLUMN_DUE_DATE = "due_date";
    private static final String COLUMN_TIME = "time";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Enable foreign keys
        db.execSQL("PRAGMA foreign_keys=ON;");

        // Create Tasks Table
        String createTasksTable = "CREATE TABLE " + TABLE_TASKS + " ("
                + COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TASK_NAME + " TEXT NOT NULL)";
        db.execSQL(createTasksTable);

        // Create Subtasks Table with due_date and time columns
        String createSubtasksTable = "CREATE TABLE " + TABLE_SUBTASKS + " ("
                + COLUMN_SUBTASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TASK_ID + " INTEGER, "
                + COLUMN_SUBTASK_TEXT + " TEXT NOT NULL, "
                + COLUMN_SUBTASK_STATUS + " INTEGER DEFAULT 0, "
                + COLUMN_DUE_DATE + " TEXT, "
                + COLUMN_TIME + " TEXT, "
                + "FOREIGN KEY(" + COLUMN_TASK_ID + ") REFERENCES " + TABLE_TASKS + "(" + COLUMN_TASK_ID + ") ON DELETE CASCADE)";
        db.execSQL(createSubtasksTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBTASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    // Insert a new task
    public void addTask(String taskName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, taskName);
        db.insert(TABLE_TASKS, null, values);
    }

    // Get all tasks
    public ArrayList<ToDoItem> getAllTasks() {
        ArrayList<ToDoItem> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TASKS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_ID));
                String taskName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_NAME));
                taskList.add(new ToDoItem(id, taskName));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return taskList;
    }

    // Delete a task (subtasks will be deleted due to ON DELETE CASCADE)
    public void deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, COLUMN_TASK_ID + "=?", new String[]{String.valueOf(taskId)});
    }

    // Insert a subtask without date/time
    public void addSubtask(int taskId, String subtaskText) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_ID, taskId);
        values.put(COLUMN_SUBTASK_TEXT, subtaskText);
        db.insert(TABLE_SUBTASKS, null, values);
    }

    // Insert a subtask with date/time
    public void addSubtask(int taskId, String subtaskText, String dueDate, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_ID, taskId);
        values.put(COLUMN_SUBTASK_TEXT, subtaskText);
        values.put(COLUMN_DUE_DATE, dueDate);
        values.put(COLUMN_TIME, time);
        db.insert(TABLE_SUBTASKS, null, values);
    }

    // Get all subtasks for a given task (unchecked ones first)
    public ArrayList<Subtask> getSubtasksForTask(int taskId) {
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_SUBTASKS + " WHERE " + COLUMN_TASK_ID + "=? ORDER BY " + COLUMN_SUBTASK_STATUS + " ASC",
                new String[]{String.valueOf(taskId)}
        );

        if (cursor.moveToFirst()) {
            do {
                int subtaskId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SUBTASK_ID));
                String subtaskText = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBTASK_TEXT));
                boolean status = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SUBTASK_STATUS)) == 1;
                String dueDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DUE_DATE));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME));
                subtaskList.add(new Subtask(subtaskId, taskId, subtaskText, status, dueDate, time));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return subtaskList;
    }

    // Update subtask status (checked/unchecked)
    public void updateSubtaskStatus(int subtaskId, boolean isChecked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SUBTASK_STATUS, isChecked ? 1 : 0);
        db.update(TABLE_SUBTASKS, values, COLUMN_SUBTASK_ID + "=?", new String[]{String.valueOf(subtaskId)});
    }

    // Delete a subtask
    public void deleteSubtask(int subtaskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SUBTASKS, COLUMN_SUBTASK_ID + "=?", new String[]{String.valueOf(subtaskId)});
    }
}
