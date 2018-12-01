package com.utdisaster.utdmer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class NewMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
    }

        sendButton = findViewById(R.id.sendNewMessage);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Send Message: " + messageField.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}