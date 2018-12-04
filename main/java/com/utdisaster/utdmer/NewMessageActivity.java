package com.utdisaster.utdmer;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.HashMap;

import static com.utdisaster.utdmer.MainActivity.hmap;


public class NewMessageActivity extends AppCompatActivity {

    Button sendButton, saveButton, getDraft;
    TextView messageField, recipientField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);


        messageField = findViewById(R.id.messageField);
        recipientField = findViewById(R.id.recipientField);
        sendButton = findViewById(R.id.sendNewMessage);

        saveButton = findViewById(R.id.saveDraftButton);
        getDraft = findViewById(R.id.getDraftButton);

        messageField.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Save draft message
            }
        });

        recipientField.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Save draft for this number
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Insert draft for this number
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSmsMessage(v);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                saveDraft(v);
            }


        });

        getDraft.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                getDraft(v);
            }
        });



    }

    public void sendSmsMessage(View v) {
        SmsManager smsManager = SmsManager.getDefault();
        // Send message
        smsManager.sendTextMessage(recipientField.getText().toString(), null, messageField.getText().toString(), null, null);
        messageField.setText("");
        // Notify user that message was sent
        Snackbar.make(v, "Message has been sent", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void saveDraft(View v) {
        String phoneNumber = recipientField.getText().toString();
        String draftText = messageField.getText().toString();
        hmap.put(phoneNumber,draftText);

        messageField.setText("");
    }

    public void getDraft(View v){
        String draftKey = recipientField.getText().toString();

        String draft = hmap.get(draftKey);
        messageField.setText(draft);

        Snackbar.make(v, "Draft returned", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }




}
