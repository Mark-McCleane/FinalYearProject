package com.example.samapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class sam_app extends AppCompatActivity {
    private TextToSpeech txtToSpeech;
    private SpeechRecognizer speechRecognizer;
    private TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("SAM APP");
        setContentView(R.layout.activity_sam_app);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //auto-generated
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM) ;
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                speechRecognizer.startListening(intent);
            }
        });
        startTextToSpeech();
        startSpeechRecognizer();
    }

    private void startSpeechRecognizer() {
        try {
            boolean speechRecogniserAvalible = SpeechRecognizer.isRecognitionAvailable(getApplicationContext());
            if(speechRecogniserAvalible) {
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                speechRecognizer.setRecognitionListener(new RecognitionListener() {
                    @Override
                    public void onReadyForSpeech(Bundle params) {
                    }

                    @Override
                    public void onBeginningOfSpeech() {
                    }

                    @Override
                    public void onRmsChanged(float rmsdB) {
                    }

                    @Override
                    public void onBufferReceived(byte[] buffer) {
                    }

                    @Override
                    public void onEndOfSpeech() {
                    }

                    @Override
                    public void onError(int error) {
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
                    }

                    @Override
                    public void onEvent(int eventType, Bundle params) {
                    }
                });
            }
        }
        catch (Exception exception){
            Log.d("IO Exception", "Exception Found" + exception.getStackTrace());
        }
    }

    private void processResult(String userCommand) {
        userCommand = userCommand.toLowerCase(); //simplify command processing
        txtView = findViewById(R.id.audioInput);
        txtView.setText("Audio Input: " + userCommand);
        if(userCommand.contains("time")) { //word time and what is found
            Date date = new Date();
            String time = DateUtils.formatDateTime(getApplicationContext(), date.getTime(),
                    DateUtils.FORMAT_SHOW_TIME);
            say("The time is \"\t" + time + "\t\"");
        }
        else if(userCommand.contains("directions")) {
            if(userCommand.contains("carlow it") || userCommand.contains("it carlow") ||
                    userCommand.contains("carlow i t") || userCommand.contains("i t carlow") ||
                    userCommand.contains("80 carlow")){
                Intent goToDirections = new Intent();
                say("Giving directions to Carlow IT");
                goToDirections.setClass(getApplicationContext(), MapsActivity.class);
                startActivity(goToDirections);
            }
        }
    }

    private void startTextToSpeech() {
        txtToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(txtToSpeech.getEngines().size() == 0){
                    //no engines installed, not usuable on device.
                    Toast.makeText(getApplicationContext(), "Error: No TTS Engine on device",
                            Toast.LENGTH_LONG).show();
                    finish(); //exit app because unusable
                }
                else{
                    txtToSpeech.setLanguage(Locale.ENGLISH);
                    say("TTS Works");
                }
            }
        });
    }

    private void say(String text) {
        if(Build.VERSION.SDK_INT >= 21){
            txtToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
        }
        else {
            //api level less than 21 requires three params
            txtToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        txtToSpeech.shutdown();
    }
}