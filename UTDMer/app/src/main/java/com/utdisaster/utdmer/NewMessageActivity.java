package com.utdisaster.utdmer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NewMessageActivity extends AppCompatActivity {

    Button sendButton;
    TextView messageField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        messageField = findViewById(R.id.messageField);
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

        sendButton = findViewById(R.id.sendNewMessage);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Send Message: " + messageField.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}