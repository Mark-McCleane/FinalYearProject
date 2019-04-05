package com.example.samapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samapp.calendar_activities.*;

import java.util.ArrayList;
import java.util.Collections;

public class calendar extends AppCompatActivity {
    private Spinner spin;
    private TextView tv;
    private Button btnSubmitChoice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar2);

        spin = findViewById(R.id.choiceSpinner);
        tv = findViewById(R.id.choiceTV);
        btnSubmitChoice = findViewById(R.id.btnSubmitChoice);

        ArrayList<String> spinnerList = new ArrayList<String>();
        spinnerList.add("Create Event");
        spinnerList.add("Create Reminder");

        Collections.sort(spinnerList);
        ArrayAdapter<String> spinnerListAdapter = new ArrayAdapter<String>(this, android.R.layout.
                simple_spinner_dropdown_item, spinnerList);
        spin.setAdapter(spinnerListAdapter);

        btnSubmitChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });
    }

    private void onSubmit() {
        Class goToClass = null;
        switch (spin.getSelectedItem().toString() ){
            case ("Create Event"):
                goToClass = createEvent.class;
                break;
            case ("Create Reminder"):
                goToClass = createReminder.class;
                break;

            default:
                Toast.makeText(getApplicationContext(), "No Class Selected", Toast.LENGTH_SHORT)
                        .show();
        }
        Intent dest = new Intent(getApplicationContext(), goToClass);
        startActivity(dest);
    }
}
