package com.utdisaster.utdmer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.utdisaster.utdmer.models.Sms;
import com.utdisaster.utdmer.utility.SmsUtility;

public class ViewConversation extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.utdisaster.utdmer.MESSAGE";
    public static String address;
    public static TextView recipientText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_conversation);

        // get the address for the conversation to view
        Intent intent = getIntent();
        address = intent.getStringExtra(EXTRA_MESSAGE);

        recipientText = findViewById(R.id.recipientText);
        // tells user who the conversation is with
        recipientText.setHint("Conversation with: " + address);

    }

    @Override
    protected void onStart() {
        super.onStart();
        final ListView conversationView = findViewById(R.id._conversationView);
        conversationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get selected message
                final Sms selectedMessage = (Sms) parent.getItemAtPosition(position);
                // inflates forward delete menu
                PopupMenu menu = new PopupMenu(ViewConversation.this, view);
                menu.getMenuInflater().inflate(R.menu.message_menu, menu.getMenu());
                // if forward, opens new message activity with address filled out
                // if delete, removes message from memory and updates conversation view
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()) {
                            case R.id.message_forward:
                                Intent intent = new Intent(ViewConversation.this, NewMessageActivity.class);
                                intent.putExtra(EXTRA_MESSAGE, selectedMessage.getMsg());
                                startActivity(intent);
                                return true;
                            case R.id.message_delete:
                                boolean result = SmsUtility.deleteSmsFromMemory(Integer.valueOf(selectedMessage.getId()));
                                SmsUtility.updateConversationMessageView(conversationView);
                                return result;
                        }
                        return false;
                    }
                });
                menu.show();
            }

        });

        // updates conversation view on start
        SmsUtility.setContext(this.getApplicationContext());
        SmsUtility.updateConversationMessageView(conversationView);

        // sends reply
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
                SmsUtility.updateConversationMessageView(conversationView);
            }
        });


    }

    }