package com.utdisaster.utdmer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.utdisaster.utdmer.models.Sms;
import com.utdisaster.utdmer.utility.SmsUtility;

import java.util.ArrayList;
import java.util.List;

public class SearchMessageActivity extends AppCompatActivity {

    private static EditText searchEditText;
    private static ListView searchResultListView;
    private static Button searchButton;
    private static List<Sms> searchResults = new ArrayList<>();
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_message);

        searchEditText = (EditText) findViewById(R.id._search_text);
        searchResultListView = (ListView) findViewById(R.id._search_result_list_view);
        searchButton = (Button) findViewById(R.id._search_button_enter);


    }

    @Override
    protected void onStart() {
        super.onStart();


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchString = searchEditText.getText().toString();
                List<Sms> allMessages = SmsUtility.getAllMessages(getApplicationContext());

                if(searchResults != null){
                    searchResults = new ArrayList<>();
                }
                for(Sms message: allMessages){
                    if(message.getMsg().contains(searchString))
                        searchResults.add(message);
                }

                if(searchResults != null){
                    // Create adapter to display message
                    arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, searchResults);
                    // Replace message view with messages
                    searchResultListView.setAdapter(arrayAdapter);
                }
            }
        });



    }

    @Override
    protected void onStop() {
        super.onStop();
        searchResultListView = null;
        arrayAdapter.clear();

    }
}
