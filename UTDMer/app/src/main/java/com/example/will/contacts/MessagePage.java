package com.example.will.contacts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MessagePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_page);
    }

    public static class SearchList extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_search_list);
        }
    }
}
