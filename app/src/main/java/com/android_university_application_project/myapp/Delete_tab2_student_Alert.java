package com.android_university_application_project.myapp;

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

public class Delete_tab2_student_Alert extends AppCompatActivity {
    private SQLiteDatabase database = null;
    ListView listView_tab2;
    Button Delete_yes_tab2,Delete_no_tab2,Delete_goBackButton_tab2;
    private String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_tab2_student_alert);

        listView_tab2 = findViewById(R.id.Delete_list_view_tab2);
        Delete_yes_tab2 = findViewById(R.id.Delete_yes_tab2);
        Delete_no_tab2 = findViewById(R.id.Delete_no_tab2);
        Delete_goBackButton_tab2 = findViewById(R.id.Delete_goBackButton_tab2);

        // Retrieve data passed from MainActivity
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String lastname = intent.getStringExtra("lastname");
        String gender = intent.getStringExtra("gender");
        String faculty = intent.getStringExtra("faculty");
        String department = intent.getStringExtra("department");
        String advisor = intent.getStringExtra("advisor");

        File myDbPath = getApplication().getFilesDir();
        path = myDbPath + "/" + "finalProject"; //name of the database

        // open database
        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);

            // Build the SQL query based on the provided search term
            String query;



            // Execute the query and display the results
            Cursor cursor = database.rawQuery(buildSelectStudentQuery(name, lastname, gender, faculty, department, advisor), null);
            ArrayList<String> searchResults = new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.text_color_layout, searchResults);


            // we need to get student_id, gender, name, lastname, faculty, department, advisor
            while (cursor.moveToNext()) {
                String result = "Student Id: " + cursor.getString(cursor.getColumnIndexOrThrow("student_id")) +
                        "\nGender: " + cursor.getString(cursor.getColumnIndexOrThrow("gender")) +
                        "\nName: " + cursor.getString(cursor.getColumnIndexOrThrow("name")) +
                        "\nLastname: " + cursor.getString(cursor.getColumnIndexOrThrow("lastname")) +
                        "\nFaculty: " + cursor.getString(cursor.getColumnIndexOrThrow("faculty"))+
                        "\nDepartment: " + cursor.getString(cursor.getColumnIndexOrThrow("department"))+
                        "\nAdvisor: " + cursor.getString(cursor.getColumnIndexOrThrow("advisor"));

                searchResults.add(result);
            }

            // Set the adapter to the ListView to display the search results
            listView_tab2.setAdapter(adapter);
            cursor.close();
            database.close();

        } catch (SQLiteException e) {
            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
        }



        Delete_goBackButton_tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Delete_no_tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Delete_yes_tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
                    database.execSQL(buildDeleteStudentQuery(name, lastname, gender, faculty, department, advisor));
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
    public String buildSelectStudentQuery(String name, String lastname, String gender, String faculty, String department, String advisor) {
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM Student WHERE ");

        List<String> conditions = new ArrayList<>();

        // Add conditions based on provided variables
        if (name != null && !name.isEmpty()) {
            conditions.add("name = '" + name + "'");
        }
        if (lastname != null && !lastname.isEmpty()) {
            conditions.add("lastname = '" + lastname + "'");
        }
        if (gender != null && !gender.isEmpty()) {
            conditions.add("gender = '" + gender + "'");
        }
        if (faculty != null && !faculty.isEmpty() && !faculty.equals("Select one...")) {
            conditions.add("faculty = '" + faculty + "'");
        }
        if (department != null && !department.isEmpty() && !department.equals("Select one...")) {
            conditions.add("department = '" + department + "'");
        }
        if (advisor != null && !advisor.isEmpty() && !advisor.equals("Select one...")) {
            conditions.add("advisor = '" + advisor + "'");
        }

        // If all conditions are empty or null, delete records with all five variables as empty
        if (name.isEmpty() && lastname.isEmpty() && gender.isEmpty() && faculty.isEmpty() && department.isEmpty() && advisor.isEmpty() && advisor.equals("Select one...") && department.equals("Select one...") && faculty.equals("Select one...")) {
            return "SELECT * FROM Student WHERE name = '' AND lastname = '' AND gender = '' AND faculty = '' AND department = '' AND advisor = ''";
        }

        // Join conditions with 'AND'
        queryBuilder.append(String.join(" AND ", conditions));

        return queryBuilder.toString();
    }
    public String buildDeleteStudentQuery(String name, String lastname, String gender, String faculty, String department, String advisor) {
        StringBuilder queryBuilder = new StringBuilder("DELETE FROM Student WHERE ");

        List<String> conditions = new ArrayList<>();

        // Add conditions based on provided variables
        if (name != null && !name.isEmpty()) {
            conditions.add("name = '" + name + "'");
        }
        if (lastname != null && !lastname.isEmpty()) {
            conditions.add("lastname = '" + lastname + "'");
        }
        if (gender != null && !gender.isEmpty()) {
            conditions.add("gender = '" + gender + "'");
        }
        if (faculty != null && !faculty.isEmpty() && !faculty.equals("Select one...")) {
            conditions.add("faculty = '" + faculty + "'");
        }
        if (department != null && !department.isEmpty() && !department.equals("Select one...")) {
            conditions.add("department = '" + department + "'");
        }
        if (advisor != null && !advisor.isEmpty() && !advisor.equals("Select one...")) {
            conditions.add("advisor = '" + advisor + "'");
        }

        // If all conditions are empty or null, delete records with all five variables as empty
        if (name.isEmpty() && lastname.isEmpty() && gender.isEmpty() && faculty.isEmpty() && department.isEmpty() && advisor.isEmpty() && advisor.equals("Select one...") && department.equals("Select one...") && faculty.equals("Select one...")) {
            return "DELETE FROM Student WHERE name = '' AND lastname = '' AND gender = '' AND faculty = '' AND department = '' AND advisor = ''";
        }

        // Join conditions with 'AND'
        queryBuilder.append(String.join(" AND ", conditions));

        return queryBuilder.toString();
    }
}