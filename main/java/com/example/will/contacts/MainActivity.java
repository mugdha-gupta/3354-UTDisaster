package com.example.will.contacts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void addContact(View view){
        Intent intent = new Intent(this, ContactPage.class);
        startActivity(intent);
    }

    public void addMessage(View view) {
        Intent intent = new Intent(this, MessagePage.class);
        startActivity(intent);
    }
}
