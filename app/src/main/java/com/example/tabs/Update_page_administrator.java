package com.example.tabs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Update_page_administrator extends AppCompatActivity {
    private SQLiteDatabase database = null;
    private Button update_goBackButton,btnUpdate_update_page;
    private TextView update_page_faculty,update_page_lecturer,update_page_department,
            update_page_courseCode,update_page_courseName,update_page_text_box;
    private String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_page_administrator);

        update_page_faculty= findViewById(R.id.update_page_faculty);
        update_page_lecturer= findViewById(R.id.update_page_lecturer);
        update_page_department= findViewById(R.id.update_page_department);
        update_page_courseCode= findViewById(R.id.update_page_courseCode);
        update_page_courseName= findViewById(R.id.update_page_courseName);

        update_page_text_box= findViewById(R.id.update_page_text_box);

        update_goBackButton= findViewById(R.id.update_goBackButton);
        btnUpdate_update_page= findViewById(R.id.btnUpdate_update_page);


        File myDbPath = getApplication().getFilesDir();
        path = myDbPath + "/" + "finalProject"; //name of the database


        // Retrieve data passed from MainActivity
        Intent intent = getIntent();
        String faculty = intent.getStringExtra("faculty");
        String lecturer = intent.getStringExtra("lecturer");
        String department = intent.getStringExtra("department");
        String courseCode = intent.getStringExtra("courseCode");
        String courseName = intent.getStringExtra("courseName");

        update_page_text_box.setText("Faculty:"+faculty+"\n"+"Lecturer:"+
                lecturer+"\n"+"Department:"+department+"\n"+
                "CourseCode:"+courseCode+"\n"+"CourseName:"+courseName);


        update_goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnUpdate_update_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String newfaculty = update_page_faculty.getText().toString();
                    String newlecturer = update_page_lecturer.getText().toString();
                    String newdepartment = update_page_department.getText().toString();
                    String newcourseCode = update_page_courseCode.getText().toString();
                    String newcourseName = update_page_courseName.getText().toString();
                    database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
                    database.execSQL(buildUpdateQuery(faculty, lecturer, department, courseCode, courseName,newfaculty,newlecturer,newdepartment,newcourseCode,newcourseName));
                    Toast.makeText(getApplication(), "Data is Updated", Toast.LENGTH_LONG).show();
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
    public String buildUpdateQuery(String faculty, String lecturer, String department, String courseCode, String courseName,
                                   String newFaculty, String newLecturer, String newDepartment, String newCourseCode, String newCourseName) {
        StringBuilder queryBuilder = new StringBuilder("UPDATE Administration SET ");

        List<String> updates = new ArrayList<>();

        // Add updates based on provided new values
        if (newFaculty != null && !newFaculty.isEmpty()) {
            updates.add("faculty = '" + newFaculty + "'");
        }
        if (newLecturer != null && !newLecturer.isEmpty()) {
            updates.add("lecturer = '" + newLecturer + "'");
        }
        if (newDepartment != null && !newDepartment.isEmpty()) {
            updates.add("department = '" + newDepartment + "'");
        }
        if (newCourseCode != null && !newCourseCode.isEmpty()) {
            updates.add("courseCode = '" + newCourseCode + "'");
        }
        if (newCourseName != null && !newCourseName.isEmpty()) {
            updates.add("courseName = '" + newCourseName + "'");
        }

        // If all updates are empty or null, return an empty string (no updates)
        if (updates.isEmpty()) {
            return "";
        }

        // Join updates with ','
        queryBuilder.append(String.join(", ", updates));

        // Add conditions based on provided variables for the WHERE clause
        queryBuilder.append(" WHERE ");
        List<String> conditions = new ArrayList<>();

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

        // If all conditions are empty or null, return an empty string (no WHERE clause)
        if (conditions.isEmpty()) {
            return "";
        }

        // Join conditions with 'AND'
        queryBuilder.append(String.join(" AND ", conditions));

        return queryBuilder.toString();
    }

}