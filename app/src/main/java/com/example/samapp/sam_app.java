package com.example.samapp;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samapp.calendar_activities.createEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class sam_app extends AppCompatActivity {
    private static TextToSpeech txtToSpeech;
    private SpeechRecognizer speechRecognizer;
    private TextView txtView;
    private FloatingActionButton helpFab;
    private String[] commandRequest = {"Call Function", "Text Function", "Date Function",
            "Time Function", "To-Do List Function", "Send Email Function", "Open Email Function",
            "Open Calendar Function", "Open Alarm Function", "Create Calendar Event","Search Function",
            "Youtube Search Function"};

    private String[] commands = {"Call", "Text", "Date", "Time", "To-do list", "Send Email",
            "Open Email", "Open Calendar", "Open Alarm", "Calendar Event", "'Search' + your question",
            "'Youtube' + Type Of Video",
    };
    private FloatingActionButton fab;

    //    private String ACCOUNT_TYPE_GOOGLE = "com.google";
//    private final String[] FEATURES_MAIL = {
//            "service_mail"
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("SAM APP");
        setContentView(R.layout.activity_sam_app);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //auto-generated
        fab = findViewById(R.id.fab);
        helpFab = findViewById(R.id.helpFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                speechRecognizer.startListening(intent);
            }
        });


        helpFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNeedsHelp();
            }
        });
        startTextToSpeech();
        startSpeechRecognizer();
    }

    private void userNeedsHelp() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Command List");
        String message = "";
        for (int i = 0; i < commandRequest.length; i++) {
            message = message + commandRequest[i] + ":\t\t\tSay " + commands[i] + "\n";
        }
        alert.setMessage(message);

        // Create TextView
        final TextView input = new TextView(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                // Do something with value!
            }
        });

//        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                // Canceled.
//            }
//        });
        alert.show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                fab.performClick();
                return true;
        }
        return false;
    }

    private void startSpeechRecognizer() {
        try {
            boolean speechRecogniserAvalible = SpeechRecognizer.isRecognitionAvailable(getApplicationContext());
            if (speechRecogniserAvalible) {
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
        } catch (Exception exception) {
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

        if (userCommand.contains("time")) { //word time and what is found
            Toast.makeText(getApplicationContext(),
                    "The time is " + time, Toast.LENGTH_LONG).show();
            say("The time is \"\t" + time + "\t\"");
        } else if (userCommand.contains("hello") || userCommand.contains("hi")) {
            Random r = new Random();
            String morningEveningReply;
            if (Integer.parseInt(time.substring(0, 1)) < 12) {
                morningEveningReply = "Good Morning";
            } else {
                morningEveningReply = "Good Evening";
            }
            String[] replies = {"Hello", "Hi", "Hola", morningEveningReply};
            say(replies[r.nextInt(replies.length - 1)]);
        } else if (userCommand.contains("your") && userCommand.contains("name")) {
            say("My name is SAM");
        } else if (userCommand.contains("directions to")) {
            Intent goToDirections = new Intent(Intent.ACTION_VIEW);

            //example tesco wexford
            // String goToLocation = userCommand.substring(10,userCommand.length());

            goToDirections.setData(Uri.parse("http://maps.google.co.in/maps?q=" + userCommand));

            if (goToDirections.resolveActivity(getPackageManager()) != null) {
                startActivity(goToDirections);
            }
        } else if (userCommand.contains("search")) {
            Intent search = new Intent(Intent.ACTION_VIEW);

            //example tesco wexford
            // String goToLocation = userCommand.substring(10,userCommand.length());

            search.setData(Uri.parse("https://www.google.ie/search?q=" + userCommand.substring(7)));

            if (search.resolveActivity(getPackageManager()) != null) {
                startActivity(search);
            }
        } else if (userCommand.contains("call")) {
            getContactList(callUser.class);
        } else if (userCommand.contains("date")) {
            Date year = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                year = Calendar.getInstance().getTime();
                say("The date is " + year);
                Toast.makeText(getApplicationContext(), "The date is " + year,
                        Toast.LENGTH_LONG).show();
            }
        } else if (userCommand.contains("message") || userCommand.contains("text")) {
            getContactList(textUser.class);
        } else if (userCommand.contains("email")) {
            if (userCommand.contains("email") && (userCommand.contains("view")
                    || userCommand.contains("read") || userCommand.contains("open"))) {
                // TODO: 27/03/2019 open gmail
                Intent gmail = new Intent(getPackageManager()
                        .getLaunchIntentForPackage("com.google.android.gm"));
                startActivity(gmail);
            }
            else {
                Intent goToEmail = new Intent(getApplicationContext(), sendEmail.class);
                startActivity(goToEmail);
            }
        } else if (userCommand.contains("to do list") || userCommand.contains("to-do list")) {
            Intent toDoList = new Intent(getApplicationContext(), com.example.samapp.ToDoList.toDoList.class);
            startActivity(toDoList);
        } else if (userCommand.contains("calendar")) {
            if (userCommand.contains("today") || userCommand.contains("todays") ||
                    userCommand.contains("today's") || userCommand.contains("open")) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        Calendar beginTime = Calendar.getInstance();
                    long timeInMillis = beginTime.getTimeInMillis();
                    Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, timeInMillis);
                    Intent intent = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    startActivity(intent);
                }
                else {
                    say("Android SDK Version is lower than " + android.os.Build.VERSION_CODES.N
                            + " and your version is only " + android.os.Build.VERSION.SDK_INT);
                    return;
                }
            }
            if(userCommand.contains("event")) {
                Intent calendarEventIntent = new Intent(getApplicationContext(), createEvent.class);
                startActivity(calendarEventIntent);
            }
        } else if (userCommand.contains("youtube")) {
            Intent youtubeIntent = new Intent();
            youtubeIntent.setData(
                    Uri.parse("https://www.google.ie/search?q=" + userCommand));
            startActivity(youtubeIntent);
            //            String videoSearch = userCommand.substring(8,userCommand.length());
//            Intent youtubeIntent = new Intent();
//            youtubeIntent.setData(Uri.parse("https://www.youtube.com/results?search_query="
//                    + videoSearch + "&page=&utm_source=opensearch"));
//            startActivity(youtubeIntent);
        }
        else if (userCommand.contains("alarm")) {
            Intent alarmIntent = new Intent(getApplicationContext(), Alarm.class);
            startActivity(alarmIntent);
        } else {
            say("Please try again");
        }
    }

    private void getContactList(Class goToClass) {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        ArrayList<String> phoneNumber = new ArrayList<>();
        ArrayList<String> contactName = new ArrayList<>();
        ArrayList<String> contactID = new ArrayList<>();

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
        if (cur != null) {
            cur.close();
        }

        Intent in = new Intent();
        in.putExtra("contact names", contactName);
        in.putExtra("phone numbers", phoneNumber);
        in.putExtra("contact id", contactID);
        in.setClass(getApplicationContext(), goToClass);
        startActivity(in);
    }

    private void startTextToSpeech() {
        txtToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (txtToSpeech.getEngines().size() == 0) {
                    //no engines installed, not usuable on device.
                    Toast.makeText(getApplicationContext(), "Error: No TTS Engine on device",
                            Toast.LENGTH_LONG).show();
                    finish(); //exit app because unusable
                } else {
                    txtToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });
    }

    public static void say(String text) {
        if (Build.VERSION.SDK_INT >= 21) {
            txtToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            //api level less than 21 requires three params, just incase I dedide to lower min api
            txtToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        txtToSpeech.shutdown();
    }
}