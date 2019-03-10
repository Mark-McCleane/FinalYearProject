package com.example.samapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class textUser extends AppCompatActivity {
    private Spinner contactNameSpinner;
    private EditText messageBox ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_user);

        contactNameSpinner = findViewById(R.id.contactNameSpinner);
        ArrayList<String> contactNames = getIntent().getExtras().getStringArrayList("contact names");
        Collections.sort(contactNames);
        ArrayAdapter<String> contactNameAdapter = new ArrayAdapter<String>(this, android.R.layout.
                simple_spinner_dropdown_item, contactNames);
        contactNameSpinner.setAdapter(contactNameAdapter);

        ImageButton sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSMSButtonClick();
            }
        });
    }

    private void onSMSButtonClick() {
        String contactName = contactNameSpinner.getSelectedItem().toString();
        String phoneNumberToText = getContactNumber(contactName, getApplicationContext());
        messageBox = findViewById(R.id.textMessageInput);
        Intent smsIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("sms:" + phoneNumberToText) );
        String textMessage = messageBox.getText().toString();
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
}
