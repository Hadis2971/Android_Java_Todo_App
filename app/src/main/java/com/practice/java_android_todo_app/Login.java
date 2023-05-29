package com.practice.java_android_todo_app;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Login extends AppCompatActivity implements View.OnClickListener {
    EditText email, password;
    Button loginBtn;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.base_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        email = findViewById(R.id.login_email_field);
        password = findViewById(R.id.login_password_field);
        loginBtn = findViewById(R.id.login_user_btn);

        loginBtn.setOnClickListener(this);
    }

    private void loginUser() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getReadableDatabase();

        String[] protection = {
                BaseColumns._ID,
                DatabaseContracts.UserContract.COLUMN_NAME_FIRST_NAME,
                DatabaseContracts.UserContract.COLUMN_NAME_LAST_NAME,
                DatabaseContracts.UserContract.COLUMN_NAME_EMAIL,
                DatabaseContracts.UserContract.COLUMN_NAME_BIRTHDAY};


        String selection = DatabaseContracts.UserContract.COLUMN_NAME_EMAIL + " = ? " + "AND " +
                DatabaseContracts.UserContract.COLUMN_NAME_PASSWORD + " = ?";

        String[] selectionArgs = {email.getText().toString(), password.getText().toString()};

        Cursor cursor = db.query(DatabaseContracts.UserContract.TABLE_NAME, protection, selection, selectionArgs, null, null, null);

        if (cursor.moveToNext()) {
            Intent intent = new Intent(this, TodoList.class);
            intent.putExtra("user_name", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.UserContract.COLUMN_NAME_FIRST_NAME)));
            intent.putExtra("last_name", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.UserContract.COLUMN_NAME_LAST_NAME)));
            intent.putExtra("user_id", cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.UserContract._ID)));

            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.wrong_credentials, Toast.LENGTH_SHORT).show();
        }

        cursor.close();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_user_btn:
                loginUser();
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}