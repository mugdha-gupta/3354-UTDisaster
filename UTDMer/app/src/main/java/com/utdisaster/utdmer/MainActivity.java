package com.utdisaster.utdmer;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.utdisaster.utdmer.models.Conversation;
import com.utdisaster.utdmer.models.Sms;
import com.utdisaster.utdmer.utility.ConversationUtility;
import com.utdisaster.utdmer.utility.SmsUtility;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArrayAdapter arrayAdapter;
    private ListView messageView;
    // Create broadcast receiver that updates message view anytime a sms is received
    private BroadcastReceiver broadcaseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateMessageView();
        }
    };

    // Enum to keep track of where the user is when they are requested sms permission
    enum RequestCode {
        APPLICATION_LAUNCH, FAB_ACTION, BROADCAST_RECEIVER
    }

    private boolean getSmsPermissions(RequestCode requestCode) {
        switch(requestCode) {
            // If permissions requested at application launch, we will need read permissions to display all sms
            case APPLICATION_LAUNCH:
                // Check if permissions already granted
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED){
                    return true;
                }  else {
                // Requests permissions if not granted
                    requestPermissions(new String[]{Manifest.permission.READ_SMS}, requestCode.ordinal());
                    return false;
                }
            // If permissions requested by a FAB action, we will need send permissions to send a sms
            case FAB_ACTION:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    return true;
                }  else {
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS}, requestCode.ordinal());
                    return false;
                }
            case BROADCAST_RECEIVER:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
                    return true;
                }  else {
                    requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, requestCode.ordinal());
                    return false;
                }

            default: return false;
        }
    }

    //How to handle response to permission requests
    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String permissions[], @NotNull int[] grantResults) {
        // View to display snackbar message in
        View view = findViewById(android.R.id.content);
        // Check if permissions were granted
        if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            RequestCode code = RequestCode.values()[requestCode];
            // Check request context
            switch (code) {
                case FAB_ACTION:
                    // Open new message activity if recent action was a FAB Action
                    Intent intent = new Intent(MainActivity.this, NewMessageActivity.class);
                    startActivity(intent);
                    break;
                case APPLICATION_LAUNCH:
                        updateMessageView();
                    break;
                case BROADCAST_RECEIVER:
                    IntentFilter filter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
                    registerReceiver(broadcaseReceiver, filter);
                    break;
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        } else{
            // If permissions were denied, notify user
            Snackbar.make(view, "SMS permissions denied.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Request read permissions
        if(getSmsPermissions(RequestCode.APPLICATION_LAUNCH)) {
            // If granted, populate listview with messages
            updateMessageView();
        }
        else {
            // If not granted, explain to the user why there is nothing to see
            ArrayList<String> messageList = new ArrayList<>();
            messageList.add("We need access to your SMS in order to display your messages");
            messageList.add("Please relaunch the app to bring up the permission dialog");
            messageView = findViewById(R.id._messageView);
            arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messageList);
            messageView.setAdapter(arrayAdapter);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSmsPermissions(RequestCode.BROADCAST_RECEIVER)) {
            // Request receiver for received sms messages
            IntentFilter filter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
            registerReceiver(broadcaseReceiver, filter);
        }


        // Activate Fab
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When fab clicked, open new message activity
                if(getSmsPermissions(RequestCode.FAB_ACTION)) {
                    Intent intent = new Intent(MainActivity.this, NewMessageActivity.class);
                    startActivity(intent);
                } else {
                    // If we don't have send permissions, notify user and refuse to open activity
                    Snackbar.make(view, "We need send SMS permissions before you send a message.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //////
    //
    // Reload message view with all sms messages
    //
    //////
    private void updateMessageView() {
        // Get list of messages
        // List<Sms> messageList = SmsUtility.getSmsInbox(getApplicationContext());

//        if(messageList!=null) {
//            // Create adapter to display message
//            arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messageList);
//            // Replace message view with messages
//            messageView.setAdapter(arrayAdapter);
//        }


        List<Conversation> conversationList = ConversationUtility.getCoversations(getApplicationContext());

        // Find message view
        messageView = findViewById(R.id._messageView);



    }
}
