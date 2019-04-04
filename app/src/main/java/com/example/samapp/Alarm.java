package com.example.samapp;

import android.app.TimePickerDialog;
import android.appwidget.AppWidgetHost;
import android.content.Intent;
import android.provider.AlarmClock;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Alarm extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private Button saveBtn;
    private ImageButton timeButton;
    private Switch MonSW,TuesSW,WedSW,ThursSW,FriSW,SatSW,SunSW;
    private int beginHour, beginminute = 0;
    private CheckBox everyDayCB, everyWeekDayCB;
    private SpeechRecognizer speechRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        timeButton = findViewById(R.id.timeButton);
        saveBtn = findViewById(R.id.saveBtn);

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTimeButtonClick();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
            }
        });

        MonSW = findViewById(R.id.mon_switch);
        TuesSW = findViewById(R.id.tues_switch);
        WedSW = findViewById(R.id.wed_switch);
        ThursSW = findViewById(R.id.thurs_switch);
        FriSW = findViewById(R.id.fri_switch);
        SatSW = findViewById(R.id.sat_switch);
        SunSW = findViewById(R.id.sun_switch);

        everyDayCB = findViewById(R.id.everyDayCB);
        everyWeekDayCB = findViewById(R.id.evryWeekDayCB);
        everyDayCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    MonSW.setChecked(true);
                    TuesSW.setChecked(true);
                    WedSW.setChecked(true);
                    ThursSW.setChecked(true);
                    FriSW.setChecked(true);
                    SatSW.setChecked(true);
                    SunSW.setChecked(true);
                }
                else{
                    MonSW.setChecked(false);
                    TuesSW.setChecked(false);
                    WedSW.setChecked(false);
                    ThursSW.setChecked(false);
                    FriSW.setChecked(false);
                    SatSW.setChecked(false);
                    SunSW.setChecked(false);
                }
            }
        });

        everyWeekDayCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    MonSW.setChecked(true);
                    TuesSW.setChecked(true);
                    WedSW.setChecked(true);
                    ThursSW.setChecked(true);
                    FriSW.setChecked(true);
                }
                else {
                    MonSW.setChecked(false);
                    TuesSW.setChecked(false);
                    WedSW.setChecked(false);
                    ThursSW.setChecked(false);
                    FriSW.setChecked(false);
                }
            }
        });

        FloatingActionButton callFab = findViewById(R.id.callFab);
        callFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM) ;
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                speechRecognizer.startListening(intent);
            }
        });
        startSpeechRecognizer();
    }

    private void onTimeButtonClick() {
        DialogFragment datePicker = new TimePickerFragment();
        datePicker.show(getSupportFragmentManager(), "Time Of Alarm");
    }

    private void setAlarm() {
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(Calendar.YEAR,Calendar.MONTH,Calendar.DATE,beginHour,beginminute);
        getDays();

        Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
        alarmIntent.putExtra(AlarmClock.EXTRA_MESSAGE,"Test");

        if(getDays() != null || getDays().size() >= 0) {
            alarmIntent.putExtra(AlarmClock.EXTRA_DAYS, getDays());
        }

        alarmIntent.putExtra(AlarmClock.EXTRA_HOUR,beginHour);
        alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES,beginminute);

        startActivity(Intent.createChooser(alarmIntent, "Set alarm") );
    }

    private ArrayList<Integer> getDays() {
        ArrayList<Integer> days = new ArrayList();

        if(MonSW.isChecked()){
            days.add(2);
        }
        if(TuesSW.isChecked()){
            days.add(3);
        }
        if(WedSW.isChecked()){
            days.add(4);
        }
        if(ThursSW.isChecked()){
            days.add(5);
        }
        if(FriSW.isChecked()){
            days.add(6);
        }
        if(SatSW.isChecked()){
            days.add(7);
        }
        if(SunSW.isChecked()){
            days.add(1);
        }

        return days;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        beginHour = hourOfDay;
        beginminute = minute;
        TextView beginTimeEtv = findViewById(R.id.alarmTime);
        beginTimeEtv.setText(hourOfDay + ":" + minute);
    }

    private void startSpeechRecognizer() {
        try {
            boolean speechRecogniserAvalible = SpeechRecognizer.isRecognitionAvailable(getApplicationContext());
            if(speechRecogniserAvalible) {
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
        }
        catch (Exception exception){
            Log.d("IO Exception", "Exception Found" + exception.getStackTrace());
        }
    }

    private void processResult(String userInput) {
        userInput = userInput.toLowerCase(); //simplify command processing
        Toast.makeText(getApplicationContext(),"User Input is :" + userInput,
                Toast.LENGTH_LONG).show();

        if(userInput.contains("set days")){
            String userInputWithoutSetDays = userInput.substring(8, userInput.length());

            if(userInputWithoutSetDays.contains("monday")){
                MonSW.setChecked(true);
            }
            if(userInputWithoutSetDays.contains("tuesday")){
                TuesSW.setChecked(true);
            }
            if(userInputWithoutSetDays.contains("wednesday")){
                WedSW.setChecked(true);
            }
            if(userInputWithoutSetDays.contains("thursday")){
                ThursSW.setChecked(true);
            }
            if(userInputWithoutSetDays.contains("friday")){
                FriSW.setChecked(true);
            }
            if(userInputWithoutSetDays.contains("saturday")){
                SatSW.setChecked(true);
            }
            if(userInputWithoutSetDays.contains("sunday")){
                SunSW.setChecked(true);
            }
            timeButton.performClick();
        }
        else if(userInput.contains("every weekday")) {
            everyWeekDayCB.setChecked(true);
            timeButton.performClick();
        }
        else if(userInput.contains("every day") || userInput.contains("everyday")){
            everyDayCB.setChecked(true);
            timeButton.performClick();
        }
        else if(userInput.contains("save")) {
            saveBtn.performClick();
        }
    }
}