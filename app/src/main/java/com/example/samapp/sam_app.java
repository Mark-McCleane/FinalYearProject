package com.example.samapp;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.Locale;

public class sam_app extends AppCompatActivity {
    private TextToSpeech txtToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sam_app);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        startTextToSpeech();
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
