package com.example.samapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class sam_app extends AppCompatActivity {
    private static TextToSpeech txtToSpeech;
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
        txtView.setTextSize(20);
        Date date = new Date();
        String time = DateUtils.formatDateTime(getApplicationContext(), date.getTime(),
                DateUtils.FORMAT_SHOW_TIME);
        if(userCommand.contains("time")) { //word time and what is found

            say("The time is \"\t" + time + "\t\"");
        }

        else if(userCommand.contains("hello") || userCommand.contains("hi")){
            Random r = new Random();
            String morningEveningReply;
            if(Integer.parseInt(time.substring(0,1)) < 12){
                morningEveningReply = "Good Morning";
            }
            else{
                morningEveningReply = "Good Evening";
            }

            String[] replies = {"Hello", "Hi","Hola", morningEveningReply};
            say(replies[r.nextInt(replies.length-1)] );

        }
        else if(userCommand.contains("your")&& userCommand.contains("name")){
            say("My name is SAM");
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
        else if(userCommand.contains("call")){
            getContactList(callUser.class);
        }
        else if(userCommand.contains("date")){
            String year = DateUtils.formatDateTime(getApplicationContext(), date.getTime(),
                    DateUtils.FORMAT_SHOW_YEAR);
            say("The date is " + year);
        }
        else if(userCommand.contains("message") || userCommand.contains("text")) {
            getContactList(textUser.class);
        }
        else if( userCommand.contains("email")){
            Intent goToEmail = new Intent(getApplicationContext(), sendEmail.class);
            say("Opening email");
            startActivity(goToEmail);
        }
        else {
            say("Please try again");
        }
    }

    private void getContactList(Class goToClass) {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        ArrayList<String> phoneNumber = new ArrayList<>();
        ArrayList<String> contactName = new ArrayList<>();
        ArrayList<String> contactID= new ArrayList<>();

        // if cursor found
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //send contacts to new activity
                        contactName.add(name);
                        phoneNumber.add(phoneNo);
                        contactID.add(id);
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null) {
            cur.close();
        }
        Intent in = new Intent();
        in.putExtra("contact names", contactName);
        in.putExtra("phone numbers",phoneNumber);
        in.putExtra("contact id", contactID);
        in.setClass(getApplicationContext(), goToClass);
        startActivity(in);
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
                else {
                    txtToSpeech.setLanguage(Locale.ENGLISH);
                    say("Welcome, I am Sam");
                }
            }
        });
    }

    public static void say(String text) {
        if(Build.VERSION.SDK_INT >= 21){
            txtToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
        }
        else {
            //api level less than 21 requires three params
            txtToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        txtToSpeech.shutdown();
    }
}