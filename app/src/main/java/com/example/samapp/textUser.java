package com.example.samapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class textUser extends AppCompatActivity {
    private Spinner contactNameSpinner;
    private EditText messageBox ;
    private SpeechRecognizer speechRecognizer;
    private ArrayList<String> contactNames;
    private String TAG = "textUser";
    private FloatingActionButton textFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_user);

        contactNameSpinner = findViewById(R.id.contactNameSpinner);
        contactNames = getIntent().getExtras().getStringArrayList("contact names");
        Collections.sort(contactNames);
        ArrayAdapter<String> contactNameAdapter = new ArrayAdapter<String>(this, android.R.layout.
                simple_spinner_dropdown_item, contactNames);
        contactNameSpinner.setAdapter(contactNameAdapter);

        messageBox = findViewById(R.id.textMessageInput);

        ImageButton sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSMSButtonClick();
            }
        });

        textFab = findViewById(R.id.callFab);
        textFab.setOnClickListener(new View.OnClickListener() {
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
//        Date date = new Date();
//        String time = DateUtils.formatDateTime(getApplicationContext(), date.getTime(),
//                DateUtils.FORMAT_SHOW_TIME);

        if(userInput.contains("contact")){
            for(int i = 0; i < contactNames.size(); i++){
                Log.d(TAG, "inside for loop");
                if(userInput.contains(contactNames.get(i).toLowerCase())) {
                    Log.d(TAG, "inside if statement");
                    ArrayAdapter tempAdapter = (ArrayAdapter) contactNameSpinner.getAdapter();
                    int spinnerPosition = tempAdapter.getPosition(contactNames.get(i));
                    contactNameSpinner.setSelection(spinnerPosition);
                }
            }
        }
        else if(userInput.contains("message") ){
            String userText = userInput.substring(7, userInput.length());
            messageBox.setText(userText);
        }
        else if (userInput.contains("text")){
//          int firstIndex = userInput.indexOf("text");
            String userText = userInput.substring(4, userInput.length());
            messageBox.setText(userText);
        }

        if(userInput.contains("send")){
            onSMSButtonClick();
        }
    }

    private void onSMSButtonClick() {
        String contactName = contactNameSpinner.getSelectedItem().toString();
        String phoneNumberToText = getContactNumber(contactName, getApplicationContext());
        Intent smsIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("sms:" + phoneNumberToText) );
        String textMessage = messageBox.getText().toString();
        textMessage= textMessage.substring(0,1).toUpperCase()
                + textMessage.substring(1, textMessage.length());
        smsIntent.putExtra("sms_body", textMessage);
        smsIntent.setData(Uri.parse("sms:" + phoneNumberToText));
        startActivity(smsIntent);
    }

    private String getContactNumber(String contactName, Context context) {
        String phoneNumber = null;
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" like'%" +
                contactName +"%'";//query string to get name similiar to my contact name
        String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor c = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, selection, null, null
        );
        if (c.moveToFirst()) {
            phoneNumber = c.getString(0);
        }
        c.close();

        if(phoneNumber==null) {
            phoneNumber = "Unsaved";
        }
        return phoneNumber;
    }
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                textFab.performClick();
                return true;
        }
        return false;
    }
}