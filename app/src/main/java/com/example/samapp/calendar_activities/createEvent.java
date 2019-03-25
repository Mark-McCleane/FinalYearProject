package com.example.samapp.calendar_activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.example.samapp.DatePickerFragment;
import com.example.samapp.R;
import com.example.samapp.TimePickerFragment;

import java.text.DateFormat;
import java.util.Calendar;

public class createEvent extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private ImageButton startTimeBtn;
    private ImageButton dateBtn;
    private EditText title;
    private EditText description;

    private int flag = 0;
    private int beginHour = 0;
    private int beginminute = 0;
    private long year = 0;
    private int month = 0;
    private int date = 0;
    private int finishHour = 0;
    private int finishMinute = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        startTimeBtn = findViewById(R.id.startingButtonTime);
        dateBtn = findViewById(R.id.dateButton);
        title = findViewById(R.id.titleETV);
        description = findViewById(R.id.descriptionETV);
        startTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickStartTimeButton();
            }
        });
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDateButtonClick();
            }
        });

        Button save = findViewById(R.id.saveBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    private void save() {
        Calendar beginTime = Calendar.getInstance();
        beginTime.set((int) year,month,date,beginHour,beginminute);

        Calendar endTime = Calendar.getInstance();
        endTime.set((int) year,month,date, finishHour, finishMinute);

        String eventTitle = title.getText().toString();
        String eventDesc = description.getText().toString();


        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis() )
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis() )
                .putExtra(CalendarContract.Events.TITLE,eventTitle)
                .putExtra(CalendarContract.Events.DESCRIPTION, eventDesc);
        startActivity(intent);
    }

    private void onDateButtonClick() {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "Event Date");
    }

    private void onClickStartTimeButton() {
        DialogFragment startTimePicker = new TimePickerFragment();
        startTimePicker.show(getSupportFragmentManager(), "Event Starting Time");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int min) {
        if(flag == 0){
            beginHour = hourOfDay;
            beginminute = min;
            EditText beginTimeEtv = findViewById(R.id.beginningTime);
            beginTimeEtv.setText(hourOfDay + ":" + min);
            flag = 1;
        }
        else {
            finishHour = hourOfDay;
            finishMinute = min;
            EditText finishTimeEtv = findViewById(R.id.finishingTime);
            finishTimeEtv.setText(hourOfDay + ":" + min);
            flag = 0;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int yr, int m, int dayOfMonth) {
        year = yr;
        month = m;
        date = dayOfMonth;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, yr);
        c.set(Calendar.MONTH, m);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        EditText etv = findViewById(R.id.date);
        etv.setText(currentDateString);
    }
}