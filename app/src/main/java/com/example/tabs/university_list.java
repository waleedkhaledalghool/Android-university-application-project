package com.example.tabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class university_list extends AppCompatActivity {
    private ListView listView;
    Button goBackButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.university_list);

        goBackButton = findViewById(R.id.goBackButton);


        // Retrieve data from the intent
        List<String> originalData = getIntent().getStringArrayListExtra("data");

        // Use the data to populate a new ListView or any other UI component
       listView = findViewById(R.id.fullScreenListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),R.layout.text_color_layout, originalData);
        listView.setAdapter(adapter);


        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}