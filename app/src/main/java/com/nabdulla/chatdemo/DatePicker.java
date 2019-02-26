package com.nabdulla.chatdemo;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Locale;

public class DatePicker implements View.OnClickListener {

    @Override
    public void onClick(final View v) {
        Calendar c = Calendar.getInstance();

        new DatePickerDialog(
                v.getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        EditText messageBox = (EditText) v;
                        messageBox.setText(String.format(Locale.ENGLISH, "%02d-%02d-%d", dayOfMonth, month, year));
                    }
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
        ).show();
    }
}
