package com.nabdulla.chatdemo;

import android.app.TimePickerDialog;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Locale;

public class TimePicker implements View.OnClickListener {
    @Override
    public void onClick(final View v) {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(
                v.getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
                        EditText editText = (EditText) v;
                        String am_pm;
                        int hh;
                        if (hourOfDay < 12) {
                            hh = hourOfDay == 0 ? 12 : hourOfDay;
                            am_pm = "AM";
                        } else {
                            hh = hourOfDay > 12 ? hourOfDay - 12 : hourOfDay;
                            am_pm = "PM";
                        }
                        editText.setText(String.format(Locale.ENGLISH, "%02d:%02d %s", hh, minute, am_pm));
                    }
                },
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                false
        ).show();
    }
}
