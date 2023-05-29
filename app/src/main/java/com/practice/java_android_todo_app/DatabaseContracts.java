package com.practice.java_android_todo_app;

import android.provider.BaseColumns;

public class DatabaseContracts {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Todo.db";

    private DatabaseContracts() {
    }

    public static class UserContract implements BaseColumns {
        public static final String TABLE_NAME = "Users";
        public static final String COLUMN_NAME_FIRST_NAME = "First_Name";
        public static final String COLUMN_NAME_LAST_NAME = "Last_Name";
        public static final String COLUMN_NAME_EMAIL = "Email";
        public static final String COLUMN_NAME_PASSWORD = "Password";
        public static final String COLUMN_NAME_BIRTHDAY = "Birthday";
    }

    public static class TodoContract implements BaseColumns {
        public static final String TABLE_NAME = "Todos";

        public static final String COLUMN_NAME_TITLE = "Title";
        public static final String COLUMN_NAME_DESCRIPTION = "Description";
        public static final String COLUMN_NAME_COMPLETED = "Completed";
        public static final String COLUMN_NAME_DATE_TO_COMPLETE = "Date_To_Complete";
        public static final String COLUMN_NAME_PRIORITY = "Priority";
        public static final String COLUMN_NAME_USER_ID = "User_ID";
    }

    public static class TodoLabelsContractor implements  BaseColumns {
        public static final String TABLE_NAME = "Todo_Labels";
        public static final String COLUMN_NAME_ICON = "Icon";
        public static final String COLUMN_NAME_TODO_ID = "Todo_ID";
    }
}
