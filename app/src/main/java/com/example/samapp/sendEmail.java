package com.example.samapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class sendEmail extends AppCompatActivity {
    private EditText mETvTo;
    private EditText mETvSubject;
    private EditText mETvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        mETvTo = findViewById(R.id.eTvRecipent);
        mETvSubject = findViewById(R.id.eTvSubject);
        mETvMessage = findViewById(R.id.etvTextMessageInput);

        ImageButton sendButton = findViewById(R.id.sendEmailButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNewEmail();
            }
        });
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

        in.setType("message/rfc822");
        startActivity(in);
    }
}
