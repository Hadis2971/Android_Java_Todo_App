package com.practice.java_android_todo_app;

import java.util.ArrayList;

public class Todo {

    private String id;
    private String title;
    private String description;
    private boolean completed;
    private String dateToComplete;
    private ArrayList<String> labels = new ArrayList<String>();
    private int priority;
    private int userID;

    public Todo(String id, String title, String description, boolean completed, String dateToComplete, ArrayList<String> labels, int priority, int userID) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.dateToComplete = dateToComplete;
        this.labels = labels;
        this.priority = priority;
        this.userID = userID;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getDateToComplete() {
        return dateToComplete;
    }

    public void setDateToComplete(String dateToComplete) {
        this.dateToComplete = dateToComplete;
    }

    public ArrayList<String> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<String> labels) {
        this.labels = labels;
    }


}
