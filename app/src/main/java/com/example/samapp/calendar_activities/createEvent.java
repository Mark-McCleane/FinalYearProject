package com.example.samapp.calendar_activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.appwidget.AppWidgetHost;
import android.content.Intent;
import android.provider.CalendarContract;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.samapp.DatePickerFragment;
import com.example.samapp.R;
import com.example.samapp.TimePickerFragment;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class createEvent extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private ImageButton startTimeBtn;
    private ImageButton dateBtn;
    private EditText title;
    private EditText description;
    private FloatingActionButton eventFab;
    private int flag = 0;
    private int beginHour = 0;
    private int beginminute = 0;
    private long year = 0;
    private int month = 0;
    private int date = 0;
    private int finishHour = 0;
    private int finishMinute = 0;
    private SpeechRecognizer speechRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        startTimeBtn = findViewById(R.id.startingButtonTime);
        dateBtn = findViewById(R.id.dateButton);
        title = findViewById(R.id.titleETV);
        description = findViewById(R.id.descriptionETV);
        eventFab = findViewById(R.id.eventFab);

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
        eventFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTouch();
            }
        });


        Button save = findViewById(R.id.saveBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        startSpeechRecognizer();
    }

    private void startSpeechRecognizer() {
        try {
            boolean speechRecogniserAvalible = SpeechRecognizer.isRecognitionAvailable(getApplicationContext());
            if (speechRecogniserAvalible) {
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                speechRecognizer.setRecognitionListener(new RecognitionListener() {
                    @Override
                    public void onReadyForSpeech(Bundle params) {
                        // TODO: 20/03/2019 if needed
                    }

                    @Override
                    public void onBeginningOfSpeech() {
                        // TODO: 20/03/2019 if needed
                    }

                    @Override
                    public void onRmsChanged(float rmsdB) {
                        // TODO: 20/03/2019 if needed
                    }

                    @Override
                    public void onBufferReceived(byte[] buffer) {
                        // TODO: 20/03/2019 if needed
                    }

                    @Override
                    public void onEndOfSpeech() {
                        // TODO: 20/03/2019 if needed
                    }

                    @Override
                    public void onError(int error) {
                        // TODO: 20/03/2019 if needed
                    }

                    @Override
                    public void onResults(Bundle bundle) {
                        List<String> results = bundle.getStringArrayList(
                                SpeechRecognizer.RESULTS_RECOGNITION
                        );
                        processResult(results.get(0));
                    }

                    @Override
                    public void onPartialResults(Bundle partialResults) {
                        // TODO: 20/03/2019 if needed
                    }

                    @Override
                    public void onEvent(int eventType, Bundle params) {
                        // TODO: 20/03/2019 if needed
                    }
                });
            }
        } catch (Exception exception) {
            Log.d("IO Exception", "Exception Found" + exception.getStackTrace());
        }
    }

    private void save() {
        Calendar beginTime = Calendar.getInstance();
        beginTime.set((int) year, month, date, beginHour, beginminute);

        Calendar endTime = Calendar.getInstance();
        endTime.set((int) year, month, date, finishHour, finishMinute);

        String eventTitle = title.getText().toString();
        String eventDesc = description.getText().toString();

        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, eventTitle)
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
        if (flag == 0) {
            beginHour = hourOfDay;
            beginminute = min;
            EditText beginTimeEtv = findViewById(R.id.beginningTime);
            beginTimeEtv.setText(hourOfDay + ":" + min);
            flag = 1;
        } else {
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

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                eventFab.performClick();
                return true;
        }
        return false;
    }

    private void onTouch() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        speechRecognizer.startListening(intent);
    }

    private void processResult(String userInput) {
        userInput = userInput.toLowerCase(); //simplify command processing
        Toast.makeText(getApplicationContext(), "User Input is:" + userInput,
                Toast.LENGTH_LONG).show();
        //
//        private ImageButton startTimeBtn;
//        private ImageButton dateBtn;
//        private EditText title;
//        private EditText description;
//

        if (userInput.contains("date")) {
            dateBtn.performClick();
        } else if (userInput.contains("title")) {
            title.setText(userInput.substring(6,userInput.length()));
        } else if (userInput.contains("event description")) {
            description.setText(userInput.substring(
                    "event description".length(), userInput.length()) );
        } else if (userInput.contains("event description") && userInput.contains("save")) {
            description.setText(userInput.substring(
                    "event description".length() + " save".length(), userInput.length()) );
            save();
        } else if (userInput.contains("save")) {
            save();
        }
    }
}