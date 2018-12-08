package com.utdisaster.utdmer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.utdisaster.utdmer.models.Sms;
import com.utdisaster.utdmer.utility.SmsUtility;

import java.util.HashMap;

// Creating a new message
public class NewMessageActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.utdisaster.utdmer.MESSAGE";
    Button sendButton, saveButton, getDraft;
    TextView messageField, recipientField;
    private static HashMap<String, String> hmap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        messageField = findViewById(R.id.messageField);
        recipientField = findViewById(R.id.recipientField);
        sendButton = findViewById(R.id.sendNewMessage);

        // If forwarded message, retrieve message
        Intent intent = getIntent();
        messageField.setText(intent.getStringExtra(EXTRA_MESSAGE));


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
    }
    // Send message using SmsUtility
    public void sendSmsMessage(View v) {
        Sms sms = new Sms();
        sms.setAddress(recipientField.getText().toString());
        sms.setMsg(messageField.getText().toString());
        sms.setFolderName("sent");
        sms.setReadState(true);
        SmsUtility.sendMessage(sms);
        messageField.setText("");
        // Notify user that message was sent
        Snackbar.make(v, "Message has been sent", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
    // Save a draft when user clicks save draft
    public void saveDraft(View v) {
        String phoneNumber = recipientField.getText().toString();
        String draftText = messageField.getText().toString();
        hmap.put(phoneNumber,draftText);
        // tell user message draft saved
        Snackbar.make(v, "Draft Saved", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        // clear message
        messageField.setText("");
    }
    // retrieve the draft
    public void getDraft(View v){
        String draftKey = recipientField.getText().toString();

        String draft = hmap.get(draftKey);
        messageField.setText(draft);
        // inform the user
        Snackbar.make(v, "Draft Returned", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
    // delete existing draft
    public void deleteDraft(View v){
        String phoneNumber = recipientField.getText().toString();

        hmap.remove(phoneNumber);
        // inform user it was deleted
        Snackbar.make(v, "Draft Deleted", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

}