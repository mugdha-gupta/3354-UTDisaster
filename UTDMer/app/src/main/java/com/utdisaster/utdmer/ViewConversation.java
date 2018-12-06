package com.utdisaster.utdmer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.utdisaster.utdmer.models.Sms;
import com.utdisaster.utdmer.utility.SmsUtility;

import java.util.List;

public class ViewConversation extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.utdisaster.utdmer.MESSAGE";
    public static String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_conversation);

        Intent intent = getIntent();
        address = intent.getStringExtra(EXTRA_MESSAGE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ListView conversationView = findViewById(R.id._conversationView);
        List<Sms> conversationMessageList = SmsUtility.getConversation(address);

        if(conversationMessageList!=null) {
            ArrayAdapter arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, conversationMessageList);
            conversationView.setAdapter(arrayAdapter);
        }


    }

    }