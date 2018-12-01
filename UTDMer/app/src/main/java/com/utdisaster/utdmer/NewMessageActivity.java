package com.utdisaster.utdmer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NewMessageActivity extends AppCompatActivity {

    Button sendButton;
    TextView messageField, recipientField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        messageField = findViewById(R.id.messageField);
        recipientField = findViewById(R.id.recipientField);
        sendButton = findViewById(R.id.sendNewMessage);

        messageField.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Toast.makeText(getApplicationContext(), "Message Updated: " + messageField.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSmsMessage(v);
            }
        });
    }

    public void sendSmsMessage(View v) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(recipientField.getText().toString(), null, messageField.getText().toString(), null, null);
    }

}