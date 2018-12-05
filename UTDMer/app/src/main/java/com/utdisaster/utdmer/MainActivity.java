package com.utdisaster.utdmer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.utdisaster.utdmer.models.Sms;
import com.utdisaster.utdmer.utility.SmsUtility;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private View messageView;
    private ArrayAdapter<Sms> arrayAdapter;

    // Enum to keep track of where the user is when they are requested sms permission
    enum RequestCode {
        APPLICATION_LAUNCH, FAB_ACTION, BROADCAST_RECEIVER
    }

    private static final String TAG = MainActivity.class.getName();

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
        Log.v(TAG, "Permission request result: " + Arrays.toString(permissions) + ":" + Arrays.toString(grantResults));
        // Check if permissions were granted
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            RequestCode code = RequestCode.values()[requestCode];
            // Check request context
            switch (code) {
                case FAB_ACTION:
                    // Open new message activity if recent action was a FAB Action
                    Intent intent = new Intent(MainActivity.this, NewMessageActivity.class);
                    startActivity(intent);
                    break;
                case APPLICATION_LAUNCH:
                    SmsUtility.updateMessageView();
                    break;
                case BROADCAST_RECEIVER:
                    SmsUtility.updateMessageView();
                    break;
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SmsUtility.setContext(this.getApplicationContext());
        ListView messageView = findViewById(R.id._messageView);
        SmsUtility.setMessageView(messageView);
        // Request read permissions
        if(getSmsPermissions(RequestCode.APPLICATION_LAUNCH)) {
            // If granted, populate listview with messages
            SmsUtility.updateMessageView();
        }
        else {
            // If not granted, explain to the user why there is nothing to see
            ArrayList<String> messageList = new ArrayList<>();
            messageList.add("We need access to your SMS in order to display your messages");
            messageList.add("Please relaunch the app to bring up the permission dialog");
            ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messageList);
            messageView.setAdapter(arrayAdapter);
        }

        if( !Telephony.Sms.getDefaultSmsPackage(this).equals(getPackageName())) {
            // Application is not default SMS app
            requestDefaultSms();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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


    public void requestDefaultSms() {

        Toast.makeText(this, "Please make UTDMer your default SMS application so that we can delete messages", Toast.LENGTH_LONG).show();
        Intent intent =
                new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                getPackageName());
        Log.v(TAG, "Requesting user set application as default SMS app");
        startActivity(intent);
    }
}
