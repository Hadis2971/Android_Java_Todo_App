package com.practice.java_android_todo_app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerDialogTheme extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private int themeID;

    public DatePickerDialogTheme (int themeID) {
        this.themeID = themeID;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR),
                month = calendar.get(Calendar.MONTH),
                day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this.themeID, this, year, month + 1, day);

        return datePickerDialog;

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        TextView birthdayText = (TextView) (getActivity().findViewById(R.id.birthday_text));
        birthdayText.setText(day + "-" + month + "-" + year);
        birthdayText.setVisibility(View.VISIBLE);
    }
}
