package com.utdisaster.utdmer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
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
        List<Sms> conversationMessageList = SmsUtility.getConversation(address);
        Collections.reverse(conversationMessageList);
        if(conversationMessageList!=null) {
            ArrayAdapter arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, conversationMessageList);
            conversationView.setAdapter(arrayAdapter);
        }


    }

    }