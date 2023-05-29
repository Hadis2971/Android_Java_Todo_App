package com.practice.java_android_todo_app;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TodoList extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, SearchView.OnQueryTextListener, UpdateTodoAdapterCallback {

    public String dateToComplete;
    int priority = Priority.LOW.value;
    RadioGroup priorityGroup;
    private TodoListAdapter todoListAdapter;
    private RecyclerView todosList;
    private TextView currentTemperature;
    private EditText todoTitleField, todoDescriptionField;
    private Button addTodoBtn, openCalendarBtn;
    private SearchView searchTodoInput;
    private String userFirstName, userLastName;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        Intent intent = getIntent();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.open-meteo.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JSONWeatherApi jsonWeatherApi = retrofit.create(JSONWeatherApi.class);

        Call<Weather> call = jsonWeatherApi.getWeather();

        todosList = findViewById(R.id.todos_list);
        todoTitleField = findViewById(R.id.add_todo_title_field);
        todoDescriptionField = findViewById(R.id.add_todo_description_field);
        addTodoBtn = findViewById(R.id.add_todo_btn);
        openCalendarBtn = findViewById(R.id.add_todo_open_calendar_btn);
        priorityGroup = findViewById(R.id.priority_group);
        searchTodoInput = findViewById(R.id.search_todo_input);
        currentTemperature = findViewById(R.id.current_temperature);

        userFirstName = intent.getStringExtra("user_name");
        userLastName = intent.getStringExtra("last_name");
        userID = intent.getIntExtra("user_id", 0);

        addTodoBtn.setOnClickListener(this);
        openCalendarBtn.setOnClickListener(this);
        priorityGroup.setOnCheckedChangeListener(this);
        searchTodoInput.setOnQueryTextListener(this);

        Toolbar toolbar = findViewById(R.id.base_toolbar);
        setSupportActionBar(toolbar);

        todoListAdapter = new TodoListAdapter(this, loadTodos());
        todoListAdapter.setUpdateTodoAdapterCallback(this);

        todosList.setAdapter(todoListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        todosList.setLayoutManager(layoutManager);

        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(TodoList.this, "Something went wrong", Toast.LENGTH_SHORT);
                    return;
                }

                currentTemperature.setText(String.valueOf(response.body().getCurrentWeather().getTemperature()));
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Toast.makeText(TodoList.this, t.getMessage(), Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.base_toolbar_menu, menu);
        return true;
    }

    private ArrayList<Todo> loadTodos() {
        ArrayList<Todo> todos = new ArrayList();
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        String[] projection = {
                DatabaseContracts.TodoContract._ID,
                DatabaseContracts.TodoContract.COLUMN_NAME_TITLE,
                DatabaseContracts.TodoContract.COLUMN_NAME_DESCRIPTION,
                DatabaseContracts.TodoContract.COLUMN_NAME_COMPLETED,
                DatabaseContracts.TodoContract.COLUMN_NAME_PRIORITY,
                DatabaseContracts.TodoContract.COLUMN_NAME_DATE_TO_COMPLETE
        };

        Cursor cursor = database.query(DatabaseContracts.TodoContract.TABLE_NAME, projection, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.TodoContract._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.TodoContract.COLUMN_NAME_TITLE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.TodoContract.COLUMN_NAME_DESCRIPTION));
            int completed = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.TodoContract.COLUMN_NAME_COMPLETED));
            int priority = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.TodoContract.COLUMN_NAME_PRIORITY));
            String dateToComplete = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.TodoContract.COLUMN_NAME_DATE_TO_COMPLETE));

            Todo todo = new Todo(id, title, description, completed == 1, dateToComplete, new ArrayList<>(), priority, userID);

            todos.add(todo);
        }

        return todos;
    }

    private void addTodo() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        String title = todoTitleField.getText().toString();
        String description = todoDescriptionField.getText().toString();

        if (title.equals("") || description.equals("")) {
            Toast.makeText(this, R.string.title_description_mandatory, Toast.LENGTH_SHORT).show();
            return;
        }


        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseContracts.TodoContract.COLUMN_NAME_TITLE, title);
        contentValues.put(DatabaseContracts.TodoContract.COLUMN_NAME_DESCRIPTION, description);
        contentValues.put(DatabaseContracts.TodoContract.COLUMN_NAME_DATE_TO_COMPLETE, dateToComplete);
        contentValues.put(DatabaseContracts.TodoContract.COLUMN_NAME_COMPLETED, false);
        contentValues.put(DatabaseContracts.TodoContract.COLUMN_NAME_PRIORITY, priority);
        contentValues.put(DatabaseContracts.TodoContract.COLUMN_NAME_USER_ID, userID);

        String id = String.valueOf(database.insert(DatabaseContracts.TodoContract.TABLE_NAME, null, contentValues));

        Todo todo = new Todo(id, title, description, false, dateToComplete, new ArrayList<>(), priority, userID);

        todoListAdapter.addTodo(todo);
    }

    public void openCalendar() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.datePicker);

        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;
                dateToComplete = day + "-" + month + "-" + year;
            }
        });

        datePickerDialog.show();
    }

    public void openUpdateTodoDialog(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.updateTodoDialogTheme);
        builder.setTitle(R.string.update_todo);
        final FrameLayout frameView = new FrameLayout(this);
        builder.setView(frameView);

        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.update_todo_dialog, frameView);

        final EditText updateTodoTitleField = view.findViewById(R.id.update_todo_title_field);
        final EditText updateTodoDescriptionField = view.findViewById(R.id.update_todo_description_field);
        final Button updateTodoOpenCalendarBtn = view.findViewById(R.id.update_todo_open_calendar_btn);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String updatedTitle = updateTodoTitleField.getText().toString();
                String updatedDescription = updateTodoDescriptionField.getText().toString();
                todoListAdapter.updateTodo(updatedTitle, updatedDescription, dateToComplete, id);

                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        final AlertDialog alertDialog = builder.create();

        updateTodoOpenCalendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCalendar();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_todo_btn:
                addTodo();
                break;
            case R.id.add_todo_open_calendar_btn:
                openCalendar();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {
        Toast.makeText(TodoList.this, "Clicked " + id, Toast.LENGTH_SHORT).show();
        switch (id) {
            case R.id.low_priority_btn:
                priority = Priority.LOW.value;
                break;
            case R.id.medium_priority_btn:
                System.out.println("Medium Btn Clicked!!!");
                priority = Priority.MEDIUM.value;
                break;
            case R.id.high_priority_btn:
                priority = Priority.HIGH.value;
                break;
            case R.id.critical_priority_btn:
                priority = Priority.CRITICAL.value;
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Filter filter = todoListAdapter.getFilter();

        switch (item.getItemId()) {
            case R.id.completed_filter:
                filter.filter("Completed");
                return true;

            case R.id.pending_filter:
                filter.filter("Pending");
                return true;

            case R.id.low_priority_filter:
                filter.filter("Low");
                return true;

            case R.id.medium_priority_filter:
                filter.filter("Medium");
                return true;

            case R.id.high_priority_filter:
                filter.filter("High");
                return true;

            case R.id.critical_priority_filter:
                filter.filter("Critical");

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        Filter filter = todoListAdapter.getFilter();
        filter.filter(s);

        return true;
    }

    private enum Priority {
        LOW(1),
        MEDIUM(2),
        HIGH(3),
        CRITICAL(4);

        private final int value;

        Priority(final int newValue) {
            value = newValue;
        }

        public int getValue() {
            return value;
        }
    }
}