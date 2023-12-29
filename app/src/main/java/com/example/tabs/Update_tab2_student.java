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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Update_tab2_student extends AppCompatActivity {
    private SQLiteDatabase database = null;
    Spinner spinner_faculty_update,spinner_department_update,spinner_Advisor_update;
    EditText update_page_name_tab2,update_page_lastname_tab2;
    TextView update_page_text_box_tab2;
    Button update_goBackButton_tab2,btnUpdate_update_page_tab2;
    RadioGroup tab2GenderRadioGroup_update;
    RadioButton tab2MaleRadioButton_update,tab2FemaleRadioButton_update;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tab2_student);

        spinner_faculty_update = findViewById(R.id.spinner_faculty_update);
        spinner_department_update = findViewById(R.id.spinner_department_update);
        spinner_Advisor_update = findViewById(R.id.spinner_Advisor_update);


        update_page_name_tab2 = findViewById(R.id.update_page_name_tab2);
        update_page_lastname_tab2 = findViewById(R.id.update_page_lastname_tab2);
        update_page_text_box_tab2 = findViewById(R.id.update_page_text_box_tab2);

        btnUpdate_update_page_tab2 = findViewById(R.id.btnUpdate_update_page_tab2);
        update_goBackButton_tab2 = findViewById(R.id.update_goBackButton_tab2);

        tab2GenderRadioGroup_update = findViewById(R.id.tab2GenderRadioGroup_update);
        tab2MaleRadioButton_update = findViewById(R.id.tab2MaleRadioButton_update);
        tab2FemaleRadioButton_update = findViewById(R.id.tab2FemaleRadioButton_update);

        File myDbPath = getApplication().getFilesDir();
        path = myDbPath + "/" + "finalProject"; //name of the database

        populateSpinners();

        // Retrieve data passed from MainActivity
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String lastname = intent.getStringExtra("lastname");
        String gender = intent.getStringExtra("gender");
        String faculty = intent.getStringExtra("faculty");
        String department = intent.getStringExtra("department");
        String advisor = intent.getStringExtra("advisor");

        update_page_text_box_tab2.setText("Name: "+name+"\n"+"Last name: "+
                lastname+"\n"+"Gender: "+gender+"\n"+
                "Faculty: "+faculty+"\n"+"Department: "+department+"\n"+"Advisor: "+advisor);

        update_goBackButton_tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnUpdate_update_page_tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Getting values from UI components
                    String newGender;
                    int chkId = tab2GenderRadioGroup_update.getCheckedRadioButtonId();
                    if (chkId == tab2MaleRadioButton_update.getId()) {
                        newGender = "Male";
                    } else if (chkId == tab2FemaleRadioButton_update.getId()) {
                        newGender = "Female";
                    }else{
                        newGender = null;
                    }

                    String newName= update_page_name_tab2.getText().toString().trim();
                    String newLastName = update_page_lastname_tab2.getText().toString().trim();
                    String newFaculty = spinner_faculty_update.getSelectedItem().toString().trim();
                    String newDepartment = spinner_department_update.getSelectedItem().toString().trim();
                    String newAdvisor = spinner_Advisor_update.getSelectedItem().toString().trim();

                    database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
                    database.execSQL(buildUpdateQuery(name, lastname, gender, faculty, department,advisor,newName,newLastName,newGender,newFaculty,newDepartment,newAdvisor));
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
    public String buildUpdateQuery(String name, String lastname, String gender, String faculty, String department, String advisor,
                                   String newName, String newLastName, String newGender, String newFaculty, String newDepartment, String newAdvisor) {
        StringBuilder queryBuilder = new StringBuilder("UPDATE Student SET ");

        List<String> updates = new ArrayList<>();

        // Add updates based on provided new values
        if (newName != null && !newName.isEmpty()) {
            updates.add("name = '" + newName + "'");
        }
        if (newLastName != null && !newLastName.isEmpty()) {
            updates.add("lastname = '" + newLastName + "'");
        }
        if (newGender != null && !newGender.isEmpty()) {
            updates.add("gender = '" + newGender + "'");
        }
        if (newFaculty != null && !newFaculty.isEmpty() && !newFaculty.equals("Select one...")) {
            updates.add("faculty = '" + newFaculty + "'");
        }
        if (newDepartment != null && !newDepartment.isEmpty() && !newDepartment.equals("Select one...")) {
            updates.add("department = '" + newDepartment + "'");
        }
        if (newAdvisor != null && !newAdvisor.isEmpty() && !newAdvisor.equals("Select one...")) {
            updates.add("advisor = '" + newAdvisor + "'");
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

        // If all conditions are empty or null, return an empty string (no WHERE clause)
        if (conditions.isEmpty()) {
            return "";
        }

        // Join conditions with 'AND'
        queryBuilder.append(String.join(" AND ", conditions));

        return queryBuilder.toString();
    }
    // Populate the Spinners with distinct values from the database
    private void populateSpinners() {
        List<String> facultyValues = getDistinctColumnValues("faculty");
        List<String> departmentValues = getDistinctColumnValues("department");
        List<String> advisorValues = getDistinctColumnValues("lecturer");

        // Create ArrayAdapter and set it to the respective Spinners
        ArrayAdapter<String> facultyAdapter = null;
        facultyAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.text_color2_layout, facultyValues);
        facultyAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        spinner_faculty_update.setAdapter(facultyAdapter);

        ArrayAdapter<String> departmentAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.text_color2_layout, departmentValues);
        departmentAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        spinner_department_update.setAdapter(departmentAdapter);

        ArrayAdapter<String> advisorAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.text_color2_layout, advisorValues);
        advisorAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        spinner_Advisor_update.setAdapter(advisorAdapter);
    }
    // Fetch distinct values from the specified column in the Administration table
    private List<String> getDistinctColumnValues(String columnName) {
        List<String> values = new ArrayList<>();
        // adding Select one to the beginning just to make it obvious that it's not text
        values.add("Select one...");
        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            Cursor cursor = database.rawQuery("SELECT DISTINCT " + columnName + " FROM Administration", null);

            while (cursor.moveToNext()) {
                String value = cursor.getString(cursor.getColumnIndexOrThrow(columnName));
                values.add(value);
            }

            cursor.close();
            database.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return values;
    }
}