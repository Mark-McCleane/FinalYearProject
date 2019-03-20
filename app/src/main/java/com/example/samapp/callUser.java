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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.example.samapp.sam_app.say;

public class callUser extends AppCompatActivity {
    private Spinner contactNameSpinner;
    private SpeechRecognizer speechRecognizer;
    private ArrayList<String> contactNames;
    private final String TAG = "callUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_user);

        contactNameSpinner = findViewById(R.id.contactNameSpinner);
        contactNames = getIntent().getExtras().getStringArrayList("contact names");
        Collections.sort(contactNames);
        ArrayAdapter<String> contactNameAdapter = new ArrayAdapter<String>(this, android.R.layout.
                simple_spinner_dropdown_item, contactNames);
        contactNameSpinner.setAdapter(contactNameAdapter);

        ImageButton callButton = findViewById(R.id.callButton);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallButtonClick();
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
        Date date = new Date();
        String time = DateUtils.formatDateTime(getApplicationContext(), date.getTime(),
                DateUtils.FORMAT_SHOW_TIME);

        if (userInput.contains("time")) { //word time and what is found
            sam_app.say("The time is \"\t" + time + "\t\"");
        }
        else if(userInput.contains("contact")){
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

        if(userInput.contains("call")){
            onCallButtonClick();
        }

    }

    private void onCallButtonClick() {
        String contactName = contactNameSpinner.getSelectedItem().toString();
        String phoneNumberToCall = getContactNumber(contactName, getApplicationContext());
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumberToCall));
        startActivity(callIntent);
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
}