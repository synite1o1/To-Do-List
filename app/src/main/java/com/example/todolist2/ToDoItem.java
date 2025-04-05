package com.example.todolist2;

public class ToDoItem {
    private int id;  // Unique ID for database
    private String toDoText;

    public ToDoItem(int id, String toDoText) {
        this.id = id;
        this.toDoText = toDoText;
    }

    // Getter for ID
    public int getId() {
        return id;
    }

    // Getter for ToDo text
    public String getToDoText() {
        return toDoText;
    }

}
