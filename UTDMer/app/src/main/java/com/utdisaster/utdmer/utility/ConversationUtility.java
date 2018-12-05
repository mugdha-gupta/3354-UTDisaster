package com.utdisaster.utdmer.utility;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.utdisaster.utdmer.models.Conversation;
import com.utdisaster.utdmer.models.Sms;

import java.util.List;

public class ConversationUtility extends AppCompatActivity {

    public List<Conversation> getConversations(Context context){
        List<Sms> messageList = SmsUtility.getSmsInbox(getApplicationContext());

    }
}
