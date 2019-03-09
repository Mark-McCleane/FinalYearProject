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
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;

public class callUser extends AppCompatActivity {
    private Spinner contactNameSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_user);

        contactNameSpinner = findViewById(R.id.contactNameSpinner);
        ArrayList<String> contactNames = getIntent().getExtras().getStringArrayList("contact names");
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
