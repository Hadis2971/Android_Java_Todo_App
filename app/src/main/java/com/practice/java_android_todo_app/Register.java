package com.practice.java_android_todo_app;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Register extends AppCompatActivity implements View.OnClickListener {
    EditText firstName, lastName, email, password;
    TextView birthdayText;
    Button openDatePickerBtn, registerBtn;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstName = findViewById(R.id.register_first_name_field);
        lastName = findViewById(R.id.register_last_name_field);
        email = findViewById(R.id.register_email_field);
        password = findViewById(R.id.register_password_field);
        birthdayText = findViewById(R.id.birthday_text);
        openDatePickerBtn = findViewById(R.id.register_open_date_picker_btn);
        registerBtn = findViewById(R.id.register_user_btn);

        Toolbar toolbar = findViewById(R.id.base_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getWritableDatabase();

        openDatePickerBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);


    }

    private void registerUser() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseContracts.UserContract.COLUMN_NAME_FIRST_NAME, firstName.getText().toString());
        contentValues.put(DatabaseContracts.UserContract.COLUMN_NAME_LAST_NAME, lastName.getText().toString());
        contentValues.put(DatabaseContracts.UserContract.COLUMN_NAME_EMAIL, email.getText().toString());
        contentValues.put(DatabaseContracts.UserContract.COLUMN_NAME_PASSWORD, password.getText().toString());
        contentValues.put(DatabaseContracts.UserContract.COLUMN_NAME_BIRTHDAY, birthdayText.getText().toString());

        long newRowId = db.insert(DatabaseContracts.UserContract.TABLE_NAME, null, contentValues);

        System.out.println("New Row Added " + newRowId);

        startActivity(new Intent(this, Login.class));
    }

    private void openDatePicker() {
        DatePickerDialogTheme datePickerDialogTheme = new DatePickerDialogTheme(android.R.style.Theme_Holo_Light_Dialog);
        datePickerDialogTheme.show(getSupportFragmentManager(), "Theme");
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.register_user_btn:
                registerUser();
                break;

            case R.id.register_open_date_picker_btn: {
                openDatePicker();
                break;
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}