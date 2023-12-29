package com.example.tabs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Delete_Alert extends AppCompatActivity {
    private SQLiteDatabase database = null;
    private ListView listView;
    private String path;
    Button Delete_goBackButton,Delete_no,Delete_yes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_alert);

        listView = findViewById(R.id.Delete_list_view);
        Delete_yes = findViewById(R.id.Delete_yes);
        Delete_no = findViewById(R.id.Delete_no);
        Delete_goBackButton = findViewById(R.id.Delete_goBackButton);

        // Retrieve data passed from MainActivity
        Intent intent = getIntent();
        String faculty = intent.getStringExtra("faculty");
        String lecturer = intent.getStringExtra("lecturer");
        String department = intent.getStringExtra("department");
        String courseCode = intent.getStringExtra("courseCode");
        String courseName = intent.getStringExtra("courseName");

        File myDbPath = getApplication().getFilesDir();
        path = myDbPath + "/" + "finalProject"; //name of the database

        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);

            // Execute the query and display the results
            Cursor cursor = database.rawQuery(buildSelectQuery(faculty,lecturer,department,courseCode,courseName), null);
            ArrayList<String> searchResults = new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),R.layout.text_color_layout, searchResults);

            while (cursor.moveToNext()) {
                String result = "Faculty: " + cursor.getString(cursor.getColumnIndexOrThrow("faculty")) +
                        "\nLecturer: " + cursor.getString(cursor.getColumnIndexOrThrow("lecturer")) +
                        "\nDepartment: " + cursor.getString(cursor.getColumnIndexOrThrow("department")) +
                        "\nCourse Code: " + cursor.getString(cursor.getColumnIndexOrThrow("courseCode")) +
                        "\nCourse Name: " + cursor.getString(cursor.getColumnIndexOrThrow("courseName"));

                searchResults.add(result);
            }

            // Set the adapter to the ListView to display the search results
            listView.setAdapter(adapter);

            // closing the database
            cursor.close();
            database.close();

        }catch (SQLiteException e){
            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
        }


        Delete_goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Delete_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Delete_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
                    database.execSQL(buildDeleteQuery(faculty, lecturer, department, courseCode, courseName));
                    Toast.makeText(getApplication(), "Data is deleted", Toast.LENGTH_LONG).show();
                    // closing the database
                    database.close();
                    // Notify MainActivity that data is deleted
                    Intent resultIntent = new Intent();
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } catch (SQLiteException e) {
                    Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public String buildSelectQuery(String faculty, String lecturer, String department, String courseCode, String courseName) {
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM Administration WHERE ");

        List<String> conditions = new ArrayList<>();

        // Add conditions based on provided variables
        if (faculty != null && !faculty.isEmpty()) {
            conditions.add("faculty = '" + faculty + "'");
        }
        if (lecturer != null && !lecturer.isEmpty()) {
            conditions.add("lecturer = '" + lecturer + "'");
        }
        if (department != null && !department.isEmpty()) {
            conditions.add("department = '" + department + "'");
        }
        if (courseCode != null && !courseCode.isEmpty()) {
            conditions.add("courseCode = '" + courseCode + "'");
        }
        if (courseName != null && !courseName.isEmpty()) {
            conditions.add("courseName = '" + courseName + "'");
        }

        // If all conditions are empty or null, delete records with all five variables as empty
        if (faculty.isEmpty() && lecturer.isEmpty() && department.isEmpty() && courseCode.isEmpty() && courseName.isEmpty()) {
            return "SELECT * FROM Administration WHERE faculty = '' AND lecturer = '' AND department = '' AND courseCode = '' AND courseName = ''";
        }

        // Join conditions with 'AND'
        queryBuilder.append(String.join(" AND ", conditions));

        return queryBuilder.toString();
    }
    public String buildDeleteQuery(String faculty, String lecturer, String department, String courseCode, String courseName) {
        StringBuilder queryBuilder = new StringBuilder("DELETE FROM Administration WHERE ");

        List<String> conditions = new ArrayList<>();

        // Add conditions based on provided variables
        if (faculty != null && !faculty.isEmpty()) {
            conditions.add("faculty = '" + faculty + "'");
        }
        if (lecturer != null && !lecturer.isEmpty()) {
            conditions.add("lecturer = '" + lecturer + "'");
        }
        if (department != null && !department.isEmpty()) {
            conditions.add("department = '" + department + "'");
        }
        if (courseCode != null && !courseCode.isEmpty()) {
            conditions.add("courseCode = '" + courseCode + "'");
        }
        if (courseName != null && !courseName.isEmpty()) {
            conditions.add("courseName = '" + courseName + "'");
        }
        // If all conditions are empty or null, delete records with all five variables as empty
        if (faculty.isEmpty() && lecturer.isEmpty() && department.isEmpty() && courseCode.isEmpty() && courseName.isEmpty()) {
            return "DELETE FROM Administration WHERE faculty = '' AND lecturer = '' AND department = '' AND courseCode = '' AND courseName = ''";
        }
        // Join conditions with 'AND'
        queryBuilder.append(String.join(" AND ", conditions));

        return queryBuilder.toString();
    }

}