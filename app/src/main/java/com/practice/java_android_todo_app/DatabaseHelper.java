package com.practice.java_android_todo_app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + DatabaseContracts.UserContract.TABLE_NAME + " (" + DatabaseContracts.UserContract._ID +
            " INTEGER PRIMARY KEY, " + DatabaseContracts.UserContract.COLUMN_NAME_FIRST_NAME + " TEXT NOT NULL, " + DatabaseContracts.UserContract.COLUMN_NAME_LAST_NAME +
            " TEXT NOT NULL, " + DatabaseContracts.UserContract.COLUMN_NAME_EMAIL + " TEXT NOT NULL, " + DatabaseContracts.UserContract.COLUMN_NAME_PASSWORD
            + " Text NOT NULL, " + DatabaseContracts.UserContract.COLUMN_NAME_BIRTHDAY + " DATE)";

    private static final String CREATE_TODOS_TABLE = "CREATE TABLE " + DatabaseContracts.TodoContract.TABLE_NAME + " (" + DatabaseContracts.TodoContract._ID +
            " INTEGER PRIMARY KEY, " + DatabaseContracts.TodoContract.COLUMN_NAME_TITLE + " TEXT NOT NULL, " + DatabaseContracts.TodoContract.COLUMN_NAME_DESCRIPTION +
            " TEXT NOT NULL, " + DatabaseContracts.TodoContract.COLUMN_NAME_COMPLETED + " TINYINT DEFAULT 0, " + DatabaseContracts.TodoContract.COLUMN_NAME_DATE_TO_COMPLETE +
            " DATE, " + DatabaseContracts.TodoContract.COLUMN_NAME_PRIORITY + " INTEGER, " + DatabaseContracts.TodoContract.COLUMN_NAME_USER_ID + " INTEGER NOT NULL)";

    private static final String CREATE_TODO_LABELS = "CREATE TABLE " + DatabaseContracts.TodoLabelsContractor.TABLE_NAME + " (" + DatabaseContracts.TodoLabelsContractor._ID +
            "INTEGER PRIMARY KEY, " + DatabaseContracts.TodoLabelsContractor.COLUMN_NAME_ICON + " TEXT NOT NULL, " + DatabaseContracts.TodoLabelsContractor.COLUMN_NAME_TODO_ID +
            " INTEGER NOT NULL)";

    private static final String DROP_USERS_TABLE = "DROP TABLE IF EXISTS " + DatabaseContracts.UserContract.TABLE_NAME;
    private static final String DROP_TODOS_TABLE = "DROP TABLE IF EXISTS " + DatabaseContracts.TodoContract.TABLE_NAME;
    private static final String DROP_TODO_LABELS_TABLE = "DROP TABLE IF EXISTS " + DatabaseContracts.TodoLabelsContractor.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DatabaseContracts.DATABASE_NAME, null, DatabaseContracts.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USERS_TABLE);
        sqLiteDatabase.execSQL(CREATE_TODOS_TABLE);
        sqLiteDatabase.execSQL(CREATE_TODO_LABELS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_USERS_TABLE);
        sqLiteDatabase.execSQL(DROP_TODOS_TABLE);
        sqLiteDatabase.execSQL(DROP_TODO_LABELS_TABLE);
        onCreate(sqLiteDatabase);
    }
}
