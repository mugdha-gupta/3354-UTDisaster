package com.utdisaster.utdmer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.utdisaster.utdmer.models.Sms;
import com.utdisaster.utdmer.utility.SmsUtility;

import java.util.Collections;
import java.util.List;

public class ViewConversation extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.utdisaster.utdmer.MESSAGE";
    public static String address;
    public static TextView recipientText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_conversation);

        Intent intent = getIntent();
        address = intent.getStringExtra(EXTRA_MESSAGE);

        recipientText = findViewById(R.id.recipientText);
        recipientText.setHint("Conversation with: " + address);

    }

    @Override
    protected void onStart() {
        super.onStart();
        ListView conversationView = findViewById(R.id._conversationView);
        SmsUtility.setMessageView(conversationView);
        SmsUtility.setAddress(address);
        SmsUtility.setContext(this.getApplicationContext());
        SmsUtility.updateMessageView();

        Button button = findViewById(R.id.sendNewMessage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView editText = findViewById(R.id.messageField);
                Sms sms = new Sms();
                sms.setAddress(address);
                sms.setMsg(editText.getText().toString());
                sms.setFolderName("sent");
                sms.setReadState(true);
                SmsUtility.sendMessage(sms);
                editText.setText("");
                // Notify user that message was sent
                Snackbar.make(v, "Message has been sent", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    }