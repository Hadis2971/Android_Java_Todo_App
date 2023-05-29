package com.practice.java_android_todo_app;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import at.blogc.android.views.ExpandableTextView;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> implements Filterable {

    private Context context;
    private UpdateTodoAdapterCallback updateTodoAdapterCallback;
    private ArrayList todos = new ArrayList<Todo>(), originalTodos = new ArrayList<Todo>();
    private int[] priorityImages = {R.drawable.baseline_priority_low_24, R.drawable.baseline_priority_medium_24, R.drawable.baseline_priority_high_24, R.drawable.baseline_priority_critical_24};
    private String lastFilterUsed;

    public TodoListAdapter(Context context, ArrayList<Todo> todos) {
        this.context = context;
        this.todos.addAll(todos);
        this.originalTodos.addAll(todos);
    }

    public void setUpdateTodoAdapterCallback (UpdateTodoAdapterCallback updateTodoAdapterCallback) {
        this.updateTodoAdapterCallback = updateTodoAdapterCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View view = layoutInflater.inflate(R.layout.todo_layout, parent, false);

        return new TodoListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TodoListAdapter.ViewHolder holder, int position) {
        Todo item = (Todo) this.todos.get(position);
        TextView titleView = holder.getTitleView();
        ExpandableTextView descriptionView = (ExpandableTextView) holder.getDescriptionView();
        Button expandDescriptionBtn = holder.getExpandDescriptionBtn();
        ImageView priority = holder.getPriority();
        ImageView status = holder.getStatus();
        TextView todoDate = holder.getTodoDate();

        titleView.setText(item.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Todo todo = (Todo) todos.get(holder.getAdapterPosition());
                updateTodoAdapterCallback.openUpdateTodoDialog(todo.getId());
            }
        });

        descriptionView.setText(item.getDescription());
        descriptionView.setAnimationDuration(750L);
        descriptionView.setInterpolator(new OvershootInterpolator());

        expandDescriptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                expandDescriptionBtn.setText(descriptionView.isExpanded() ? R.string.expand : R.string.collapse);
                descriptionView.toggle();
            }
        });

        priority.setImageResource(priorityImages[item.getPriority() - 1]);
        status.setImageResource(item.isCompleted() ? R.drawable.baseline_check_circle_24 : R.drawable.baseline_pending_24);
        todoDate.setText("Date to complete " + item.getDateToComplete());

        status.setOnClickListener(new View.OnClickListener() {

            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();


            @Override
            public void onClick(View view) {
                if (item.isCompleted()) {
                    item.setCompleted(false);
                    status.setImageResource(R.drawable.baseline_pending_24);


                } else {
                    item.setCompleted(true);
                    status.setImageResource(R.drawable.baseline_check_circle_24);
                }

                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseContracts.TodoContract.COLUMN_NAME_COMPLETED, item.isCompleted());
                String selection = DatabaseContracts.TodoContract._ID + " Like ?";

                sqLiteDatabase.update(DatabaseContracts.TodoContract.TABLE_NAME, contentValues, selection, new String[]{item.getId()});
            }
        });
    }

    public void addTodo(Todo todo) {
        todos.add(todo);
        originalTodos.add(todo);
        notifyItemInserted(todos.size() - 1);
    }

    @Override
    public int getItemCount() {
        return this.todos.size();
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                ArrayList<Todo> filteredList = new ArrayList<>();

                System.out.println(charSequence);

                if (charSequence == lastFilterUsed) {
                    filteredList.addAll(originalTodos);
                } else if (charSequence == "Completed") {
                    lastFilterUsed = "Completed";

                    for (int i = 0; i < originalTodos.size(); i++) {
                        Todo todo = (Todo) originalTodos.get(i);
                        if (todo.isCompleted()) {
                            filteredList.add(todo);
                        }
                    }

                } else if (charSequence == "Pending") {
                    lastFilterUsed = "Pending";

                    for (int i = 0; i < originalTodos.size(); i++) {
                        Todo todo = (Todo) originalTodos.get(i);
                        if (!todo.isCompleted()) {
                            filteredList.add(todo);
                        }
                    }

                } else if (charSequence == "Low") {
                    lastFilterUsed = "Low";

                    for (int i = 0; i < todos.size(); i++) {
                        Todo todo = (Todo) todos.get(i);
                        if (todo.getPriority() == Priority.LOW.getValue()) {
                            filteredList.add(todo);
                        }
                    }

                } else if (charSequence == "Medium") {
                    lastFilterUsed = "Medium";

                    for (int i = 0; i < todos.size(); i++) {
                        Todo todo = (Todo) todos.get(i);
                        if (todo.getPriority() == Priority.MEDIUM.getValue()) {
                            filteredList.add(todo);
                        }
                    }

                } else if (charSequence == "High") {
                    lastFilterUsed = "High";

                    for (int i = 0; i < todos.size(); i++) {
                        Todo todo = (Todo) todos.get(i);
                        if (todo.getPriority() == Priority.HIGH.getValue()) {
                            filteredList.add(todo);
                        }
                    }

                } else if (charSequence == "Critical") {
                    lastFilterUsed = "Critical";

                    for (int i = 0; i < todos.size(); i++) {
                        Todo todo = (Todo) todos.get(i);
                        if (todo.getPriority() == Priority.CRITICAL.getValue()) {
                            filteredList.add(todo);
                        }
                    }
                } else {
                    if (charSequence.length() == 0) {
                        filteredList.addAll(originalTodos);
                    } else {
                        for (int i = 0; i < todos.size(); i++) {
                            Todo todo = (Todo) todos.get(i);
                            System.out.println(todo.getTitle().toLowerCase() + " " + charSequence.toString().toLowerCase());
                            if (todo.getTitle().toLowerCase().contains(charSequence.toString().toLowerCase()) || todo.getDescription().toLowerCase().contains(charSequence)) {
                                filteredList.add(todo);
                            }
                        }
                    }

                }

                results.count = filteredList.size();
                results.values = filteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults != null && filterResults.values != null) {

                    todos = (ArrayList<Todo>) filterResults.values;
                    notifyDataSetChanged();


                }
            }
        };

        return filter;
    }

    public void updateTodo(String updatedTitle, String updatedDescription, String updatedDateToComplete, String id) {
        ContentValues contentValues = new ContentValues();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        Todo todo = (Todo) todos.get(Integer.parseInt(id) - 1);


        if (updatedTitle != null && !updatedTitle.equals("")) {
            contentValues.put(DatabaseContracts.TodoContract.COLUMN_NAME_TITLE, updatedTitle);
            todo.setTitle(updatedTitle);
        }
        if (updatedDescription != null && !updatedDescription.equals("")) {
            contentValues.put(DatabaseContracts.TodoContract.COLUMN_NAME_DESCRIPTION, updatedDescription);
            todo.setDescription(updatedDescription);
        }
        if (updatedDateToComplete != null && !updatedDateToComplete.equals("")) {
            contentValues.put(DatabaseContracts.TodoContract.COLUMN_NAME_DATE_TO_COMPLETE, updatedDateToComplete);
            todo.setDateToComplete(updatedDateToComplete);
        }

        String selection = DatabaseContracts.TodoContract._ID + " Like ?";

        sqLiteDatabase.update(DatabaseContracts.TodoContract.TABLE_NAME, contentValues, selection, new String[]{id});

        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, todoDate;
        ExpandableTextView description;
        Button expandDescriptionBtn;
        ImageView priority, status;

        public ViewHolder(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.todo_title);
            description = view.findViewById(R.id.todo_description);
            expandDescriptionBtn = view.findViewById(R.id.expand_description_btn);
            priority = view.findViewById(R.id.priority_icon);
            status = view.findViewById(R.id.status_icon);
            todoDate = view.findViewById(R.id.todo_date);
        }

        public ImageView getPriority() {
            return priority;
        }

        public TextView getTodoDate() {
            return todoDate;
        }

        public ImageView getStatus() {
            return status;
        }

        public TextView getTitleView() {
            return title;
        }

        public ExpandableTextView getDescriptionView() {
            return description;
        }

        public Button getExpandDescriptionBtn() {
            return expandDescriptionBtn;
        }
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
