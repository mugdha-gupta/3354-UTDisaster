package com.utdisaster.utdmer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class SearchMessageActivity extends AppCompatActivity {

    private static EditText searchEditText;
    private static ListView searchResultListView;
    private static Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_message);

        searchEditText = (EditText) findViewById(R.id._search_text);
        searchResultListView = (ListView) findViewById(R.id._search_result_list_view);
        searchButton = (Button) findViewById(R.id._search_button_enter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchString = searchEditText.getText().toString();
            }
        });
    }




}
