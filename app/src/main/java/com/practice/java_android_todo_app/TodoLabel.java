package com.practice.java_android_todo_app;

public class TodoLabel {
    private String icon;
    private int todoID;

    public TodoLabel(String icon, int todoID) {
        this.icon = icon;
        this.todoID = todoID;
    }

    public String getIcon() {
        return icon;
    }

    public int getTodoID() {
        return todoID;
    }

    public void setTodoID(int todoID) {
        this.todoID = todoID;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
