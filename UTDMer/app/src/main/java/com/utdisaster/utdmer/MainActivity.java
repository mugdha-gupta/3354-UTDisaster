package com.utdisaster.utdmer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.utdisaster.utdmer.models.Sms;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter arrayAdapter;
    ListView messageView;

    enum RequestCode {
        APPLICATION_LAUNCH, FAB_ACTION
    }

    private boolean getSmsPermissions(RequestCode requestCode) {
        switch(requestCode) {
            case APPLICATION_LAUNCH:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                    return true;
                }  else {
                    requestPermissions(new String[]{Manifest.permission.READ_SMS}, requestCode.ordinal());
                    return false;
                }
            case FAB_ACTION:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    return true;
                }  else {
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS}, requestCode.ordinal());
                    return false;
                }
            default: return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String permissions[], @NotNull int[] grantResults) {
        View view = findViewById(android.R.id.content);

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            List<Sms> messageList;
            RequestCode code = RequestCode.values()[requestCode];
            switch (code) {
                case FAB_ACTION:
                    Intent intent = new Intent(MainActivity.this, NewMessageActivity.class);
                    startActivity(intent);
                    break;
                case APPLICATION_LAUNCH:
                    messageList = getSmsInbox();
                    if (messageList != null) {
                        messageView = findViewById(R.id._messageView);
                        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messageList);
                        messageView.setAdapter(arrayAdapter);
                        messageView.invalidate();
                    }
                    break;
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        } else{
            Snackbar.make(view, "SMS permissions denied.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private List<Sms> getSmsInbox() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInbox = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        Cursor smsOutbox = contentResolver.query(Uri.parse("content://sms/sent"), null, null, null, null);
        ArrayList<Sms> messages = new ArrayList<>();
        if(smsInbox != null) {
            int indexBody = smsInbox.getColumnIndex("body");
            if (indexBody < 0 || !smsInbox.moveToFirst()) {
                return null;
            }
            do {
                Sms objSms = new Sms();
                objSms.setId(smsInbox.getString(smsInbox.getColumnIndexOrThrow("_id")));
                objSms.setAddress(smsInbox.getString(smsInbox
                        .getColumnIndexOrThrow("address")));
                objSms.setMsg(smsInbox.getString(smsInbox.getColumnIndexOrThrow("body")));
                objSms.setReadState(Boolean.valueOf(smsInbox.getString(smsInbox.getColumnIndex("read"))));
                objSms.setTime(new Timestamp(Long.valueOf(smsInbox.getString(smsInbox.getColumnIndexOrThrow("date")))));
                if (smsInbox.getString(smsInbox.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms.setFolderName("inbox");
                } else {
                    objSms.setFolderName("sent");
                }
                messages.add(objSms);
            } while (smsInbox.moveToNext());
            smsInbox.close();
        }
        if(smsOutbox != null) {
            int indexBody = smsOutbox.getColumnIndex("body");
            if (indexBody < 0 || !smsOutbox.moveToFirst()) {
                return null;
            }
            do {
                Sms objSms = new Sms();
                objSms.setId(smsOutbox.getString(smsOutbox.getColumnIndexOrThrow("_id")));
                objSms.setAddress(smsOutbox.getString(smsOutbox
                        .getColumnIndexOrThrow("address")));
                objSms.setMsg(smsOutbox.getString(smsOutbox.getColumnIndexOrThrow("body")));
                objSms.setTime(new Timestamp(Long.valueOf(smsOutbox.getString(smsOutbox.getColumnIndexOrThrow("date")))));
                if (smsOutbox.getString(smsOutbox.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms.setFolderName("inbox");
                } else {
                    objSms.setFolderName("sent");
                }
                messages.add(objSms);
            } while (smsOutbox.moveToNext());
            smsOutbox.close();
        }
        if(messages.isEmpty()){
            return null;
        }
        Collections.sort(messages);
        Collections.reverse(messages);
        return messages;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(getSmsPermissions(RequestCode.APPLICATION_LAUNCH)) {
            List<Sms> messageList = getSmsInbox();
            messageView = findViewById(R.id._messageView);
            if(messageList!=null) {
                arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messageList);
                messageView.setAdapter(arrayAdapter);
            }
        }
        else {
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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getSmsPermissions(RequestCode.FAB_ACTION)) {
                    Intent intent = new Intent(MainActivity.this, NewMessageActivity.class);
                    startActivity(intent);
                } else {
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
}
