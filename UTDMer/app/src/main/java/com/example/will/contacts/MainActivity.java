package com.example.will.contacts;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    Dialog search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search = new Dialog(this);
    }

    public void addContact(View view){
        Intent intent = new Intent(this, ContactPage.class);
        startActivity(intent);
    }

    public void addMessage(View view) {
        Intent intent = new Intent(this, MessagePage.class);
        startActivity(intent);
    }

    public void showSearchList(View view) {
        Intent intent = new Intent(this, SearchList.class);
        startActivity(intent);
    }

    public void showSearch(View view){
        TextView close;
        Button searchB;
        search.setContentView(R.layout.searchmessage);
        close= search.findViewById(R.id.close);
        searchB = search.findViewById(R.id.search);
        close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                search.dismiss();
            }
        });

        search.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        search.show();
    }
}