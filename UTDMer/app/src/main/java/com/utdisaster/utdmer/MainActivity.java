package com.utdisaster.utdmer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.utdisaster.utdmer.adapater.ConversationAdapter;
import com.utdisaster.utdmer.models.Sms;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter arrayAdapter;
    ListView messageView;

    public enum PERMISSION_REQUEST {
        READ_SMS
    }

    private boolean getReadSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }  else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)) {
                Toast.makeText(this, "We need to read your SMS so that we can show them to you.", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.READ_SMS}, PERMISSION_REQUEST.READ_SMS.ordinal());
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST.READ_SMS.ordinal()) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read SMS permissions granted", Toast.LENGTH_SHORT).show();
                List<String> messageList = getSmsInbox();
                messageView = (ListView) findViewById(R.id._messageView);
                arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, messageList);
                messageView.setAdapter(arrayAdapter);
                ViewGroup vg = findViewById (R.id._messageView);
                vg.invalidate();
            } else {
                Toast.makeText(this, "Read SMS permissions denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private List<String> getSmsInbox() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInbox = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInbox.getColumnIndex("body");
        int indexAddress = smsInbox.getColumnIndex("address");
        if (indexBody < 0 || !smsInbox.moveToFirst()) {
            return null;
        }
        ArrayList<String> messages = new ArrayList<>();
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
            messages.add(objSms.toString());
        } while (smsInbox.moveToNext());

        return messages;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(getReadSmsPermission()) {
            List<String> messageList = getSmsInbox();
            messageView = (ListView) findViewById(R.id._messageView);
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, messageList);
            messageView.setAdapter(arrayAdapter);
        }
        else {
            ArrayList<String> messageList = new ArrayList<>();
            messageList.add("We need access to your SMS in order to display your messages");
            messageList.add("Please relaunch the app to bring up the permission dialog");
            messageView = (ListView) findViewById(R.id._messageView);
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, messageList);
            messageView.setAdapter(arrayAdapter);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewMessageActivity.class);
                startActivity(intent);
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
