package com.example.samapp;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

public class sendEmail extends AppCompatActivity {
    private static final String TAG = "sendEmail";
    private EditText mETvTo;
    private EditText mETvSubject;
    private EditText mETvMessage;
    private SpeechRecognizer speechRecognizer;
    private FloatingActionButton emailFab;
    private ImageButton sendButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        mETvTo = findViewById(R.id.eTvRecipent);
        mETvSubject = findViewById(R.id.eTvSubject);
        mETvMessage = findViewById(R.id.etvTextMessageInput);

        sendButton = findViewById(R.id.sendEmailButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNewEmail();
            }
        });

        emailFab = findViewById(R.id.callFab);
        emailFab.setOnClickListener(new View.OnClickListener() {
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

    private void sendNewEmail() {
        String recipientInput = mETvTo.getText().toString();
        String[] recipient = recipientInput.split(",");

        String subject = mETvSubject.getText().toString();
        String message = mETvMessage.getText().toString();

        Intent  in = new Intent(Intent.ACTION_SEND);
        in.putExtra(Intent.EXTRA_EMAIL, recipient);
        in.putExtra(Intent.EXTRA_SUBJECT, subject);
        in.putExtra(Intent.EXTRA_TEXT, message);

        in.setType("message/rfc822");       //intent type for email!
        startActivity(in);
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

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                emailFab.performClick();
                return true;
        }
        return false;
    }
    private void processResult(String userInput) {
        userInput = userInput.toLowerCase(); //simplify command processing
        Toast.makeText(getApplicationContext(),"User Input is :" + userInput,
                Toast.LENGTH_LONG).show();

        if(userInput.contains("contact")){
            String userText = userInput.substring(7, userInput.length());
            mETvTo.setText(userText);
        }
        else if(userInput.contains("subject")){
        String userText = userInput.substring(7, userInput.length());
        mETvSubject.setText(userText);
        }
        else if(userInput.contains("email")){
            String userText = userInput.substring(5, userInput.length());
            mETvMessage.setText(userText);
            sendButton.performClick();
        }
        else if(userInput.contains("e-mail")){
            String userText = userInput.substring(6, userInput.length());
            mETvMessage.setText(userText);
            sendButton.performClick();
        }
        else if(userInput.contains("send")){
            sendButton.performClick();
        }
    }
}