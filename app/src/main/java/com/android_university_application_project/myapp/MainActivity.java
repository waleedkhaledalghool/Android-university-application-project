package com.android_university_application_project.myapp;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TabHost tabhost;
    private static final int REQUEST_CODE = 1;


    EditText administration_page_faculty,
            administration_page_lecturer,
            administration_page_department,
            administration_page_courseCode,
            administration_page_courseName;

    Button administration_page_btnViewUnivesityTable, administration_page_btnAdd, administration_page_btnDelete, administration_page_btnUpdate, administration_page_btnSearch, administration_page_btnResettxt, administration_page_btnDropDb;

    private ListView listView,tab3_listview;
    private String path;
    private SQLiteDatabase database = null;
    private RadioButton tab2MaleRadioButton,tab2FemaleRadioButton;
    private RadioGroup tab2GenderRadioGroup;
    private CheckBox tab2AllInfoCheckBox;
    private Button tab2CancelButton,tab2RegisterButton,
            tab2UpdateButton,tab2SearchButton,tab2DeleteButton;
    private EditText tab2LastName,tab2Name,SearchBar_tab2;
    Spinner spinner_faculty,spinner_department,spinner_Advisor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.showOverflowMenu();

        // administration page initialization

        administration_page_faculty = findViewById(R.id.administration_page_faculty);
        administration_page_lecturer = findViewById(R.id.administration_page_lecturer);
        administration_page_department = findViewById(R.id.administration_page_department);
        administration_page_courseCode = findViewById(R.id.administration_page_courseCode);
        administration_page_courseName = findViewById(R.id.administration_page_courseName);



        administration_page_btnAdd = findViewById(R.id.administration_page_btnAdd);
        administration_page_btnDelete = findViewById(R.id.administration_page_btnDelete);
        administration_page_btnUpdate = findViewById(R.id.administration_page_btnUpdate);
        administration_page_btnSearch = findViewById(R.id.administration_page_btnSearch);
        administration_page_btnResettxt = findViewById(R.id.administration_page_btnResettxt);
        administration_page_btnDropDb = findViewById(R.id.administration_page_btnDropDb);
        administration_page_btnViewUnivesityTable = findViewById(R.id.administration_page_btnViewUnivesityTable);

        listView = findViewById(R.id.listview);

        administration_page_btnAdd.setOnClickListener(this);
        administration_page_btnUpdate.setOnClickListener(this);
        administration_page_btnDelete.setOnClickListener(this);
        administration_page_btnSearch.setOnClickListener(this);
        administration_page_btnResettxt.setOnClickListener(this);
        administration_page_btnDropDb.setOnClickListener(this);
        administration_page_btnViewUnivesityTable.setOnClickListener(this);


        File myDbPath = getApplication().getFilesDir();
        path = myDbPath + "/" + "finalProject"; //name of the database


        // create our database
        try {
            if (!databaseExist()) {
                // if we don't have a database
                database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
                Toast.makeText(getApplication(), "data base is created", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getApplication(), "we already have a database", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            // if we don't have a student table
            if (!tableExists(database,"Administration")){
                // Create table
                String Administration_table = "create table Administration ( recID integer PRIMARY KEY autoincrement, faculty text, lecturer text,department text, courseCode text,courseName text);";
                // execute the SQL script
                database.execSQL(Administration_table);
                Toast.makeText(getApplication(), "Administration table is created", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplication(), "we already have a Administration table", Toast.LENGTH_LONG).show();
            }


        } catch (Exception e) {
            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();

        }
        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            // if we don't have a student table
            if (!tableExists(database,"Student")){
                // Create table
                String Student_table = "create table Student (" +
                        "student_id INTEGER PRIMARY KEY, " +
                        "gender TEXT, " +
                        "name TEXT, " +
                        "lastname TEXT, " +
                        "faculty TEXT, " +
                        "department TEXT, " +
                        "advisor TEXT);";
                // execute the SQL script
                database.execSQL(Student_table);
                Toast.makeText(getApplication(), "Student table is created", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplication(), "we already have a Student table", Toast.LENGTH_LONG).show();
            }


        } catch (Exception e) {
            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();

        }
            database.close();
        // Set an OnItemClickListener for the ListView
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Get the selected item from the adapter
            String selectedItem = (String) parent.getItemAtPosition(position);

            // Extract values from the selected item
            String[] fields = selectedItem.split("\n");
            String faculty = fields[0].substring(fields[0].indexOf(":") + 2);  // Extract value after ":"
            String lecturer = fields[1].substring(fields[1].indexOf(":") + 2);
            String department = fields[2].substring(fields[2].indexOf(":") + 2);
            String courseCode = fields[3].substring(fields[3].indexOf(":") + 2);
            String courseName = fields[4].substring(fields[4].indexOf(":") + 2);

            // Set the text of the EditText fields
            administration_page_faculty.setText(faculty);
            administration_page_lecturer.setText(lecturer);
            administration_page_department.setText(department);
            administration_page_courseCode.setText(courseCode);
            administration_page_courseName.setText(courseName);

            // Show a Toast or perform any other action with the selected item
            Toast.makeText(getApplicationContext(), "Selected Item: " + selectedItem, Toast.LENGTH_SHORT).show();
        });
        search_database();



        // Student registration page initialization

        // Buttons
        tab2MaleRadioButton = findViewById(R.id.tab2MaleRadioButton);
        tab2FemaleRadioButton = findViewById(R.id.tab2FemaleRadioButton);
        tab2GenderRadioGroup = findViewById(R.id.tab2GenderRadioGroup);

        tab2RegisterButton = findViewById(R.id.tab2RegisterButton);
        tab2CancelButton = findViewById(R.id.tab2CancelButton);
        tab2UpdateButton = findViewById(R.id.tab2UpdateButton);
        tab2SearchButton = findViewById(R.id.tab2SearchButton);
        tab2DeleteButton = findViewById(R.id.tab2DeleteButton);

        tab2RegisterButton.setOnClickListener(this);
        tab2CancelButton.setOnClickListener(this);
        tab2UpdateButton.setOnClickListener(this);
        tab2SearchButton.setOnClickListener(this);
        tab2DeleteButton.setOnClickListener(this);

        // EditText
        tab2Name = findViewById(R.id.tab2Name);
        tab2LastName = findViewById(R.id.tab2LastName);
        SearchBar_tab2 = findViewById(R.id.SearchBar_tab2);


        // we populate the spinners with the records we have in administrator
        spinner_faculty = findViewById(R.id.spinner_faculty);
        spinner_department = findViewById(R.id.spinner_department);
        spinner_Advisor = findViewById(R.id.spinner_Advisor);
        populateSpinners();

        // we show the registered students in the third tab
        tab3_listview = findViewById(R.id.tab3_listview);

        // we initialize the checkbox
            tab2AllInfoCheckBox = findViewById(R.id.tab2AllInfoCheckBox);

        // open database
        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);

            // Build the SQL query based on the provided search term
            String query;

                query = "SELECT * FROM Student";

            // Execute the query and display the results
            Cursor cursor = database.rawQuery(query, null);
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
            tab3_listview.setAdapter(adapter);
            cursor.close();
            database.close();

        } catch (SQLiteException e) {
            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
        }



        // Set an OnItemClickListener for the ListView
        tab3_listview.setOnItemClickListener((parent, view, position, id) -> {
            // Get the selected item from the adapter
            String selectedItem = (String) parent.getItemAtPosition(position);

            // Extract values from the selected item

            String[] fields = selectedItem.split("\n");
            String student_id = fields[0].substring(fields[0].indexOf(":") + 2).trim();
            String name = fields[2].substring(fields[2].indexOf(":") + 2).trim();  // Extract value after ":"
            String lastname = fields[3].substring(fields[3].indexOf(":") + 2).trim();
            String gender = fields[1].substring(fields[1].indexOf(":") + 2).trim();
            String faculty = fields[4].substring(fields[4].indexOf(":") + 2).trim();
            String department = fields[5].substring(fields[5].indexOf(":") + 2).trim();
            String advisor = fields[6].substring(fields[6].indexOf(":") + 2);

            popoutDialog(student_id,name,lastname,gender,faculty,department,advisor);

            tab2Name.setText(name);
            tab2LastName.setText(lastname);

            setSpinner(faculty,spinner_faculty);
            setSpinner(department,spinner_department);
            setSpinner(advisor,spinner_Advisor);

            if (gender.equalsIgnoreCase("Male")) {
                tab2MaleRadioButton.setChecked(true);
            } else if (gender.equalsIgnoreCase("Female")) {
                tab2FemaleRadioButton.setChecked(true);
            }

        });


        // Initialize TabHost
        tabhost = (TabHost) findViewById(android.R.id.tabhost);
        tabhost.setup();


        TabHost.TabSpec tabspec;
        // Tab 1
        tabspec = tabhost.newTabSpec("Tab1");
        tabspec.setContent(R.id.tab1);  // Replace with the actual content of your first tab
        tabspec.setIndicator("Administration");
        tabhost.addTab(tabspec);

        // Tab 2
        TabHost.TabSpec tab2Spec = tabhost.newTabSpec("Tab2");
        tab2Spec.setContent(R.id.tab2);  // Replace with the actual content of your second tab
        tab2Spec.setIndicator("Registration");
        tabhost.addTab(tab2Spec);

        // Tab 3
        TabHost.TabSpec tab3Spec = tabhost.newTabSpec("Tab3");
        tab3Spec.setContent(R.id.tab3);  // Replace with the actual content of your third tab
        tab3Spec.setIndicator("Students");
        tabhost.addTab(tab3Spec);
        tabhost.setCurrentTab(0);

        // Get reference to the TabWidget
        TabWidget tabWidget = tabhost.getTabWidget();

        // Loop through each tab and set custom style
        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            View tabView = tabWidget.getChildTabViewAt(i);
            TextView tv = tabView.findViewById(android.R.id.title);

            // Set custom text size and color
            tv.setTextSize(15);  // Set your desired text size
            tv.setTextColor(Color.parseColor("#FFFFFF"));
            tv.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
            // Set all caps to false
            tv.setAllCaps(false);
            // Set properties for ellipsis and single line
            tv.setEllipsize(TextUtils.TruncateAt.END);
            tv.setSingleLine(true);
        }


    }


    private void setSpinner(String value,Spinner mySpinner) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) mySpinner.getAdapter();

        if (adapter != null) {
            int position = adapter.getPosition(value);

            if (position != -1) {
                // Value exists in the spinner, set the selection
                mySpinner.setSelection(position);
            }else{
                // Value doesn't exist, add it to the spinner and set the selection
                adapter.add(value);
                mySpinner.setSelection(adapter.getPosition(value));

                // Notify the adapter that the data set has changed
                adapter.notifyDataSetChanged();
            }
        }
        else {
            // Create a new adapter and set it to the spinner
            ArrayAdapter<String> newAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
            newAdapter.add(value);
            mySpinner.setAdapter(newAdapter);
            mySpinner.setSelection(0);  // Set the selection to the newly added value

            // No need to notify the adapter for the initial setup
        }
    }
    // Helper method to check if a table exists in the database
    private boolean tableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.query("sqlite_master", new String[]{"name"}, "type='table' AND name=?", new String[]{tableName}, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    // helper method to check if you have a database
    private boolean databaseExist() {
        File dbfile = new File(path);
        return dbfile.exists();
    }

    // method to insert information into the database
    public void insert_into_db() {
        //open database
        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            String faculty = administration_page_faculty.getText().toString().trim();
            String lecturer = administration_page_lecturer.getText().toString().trim();
            String department = administration_page_department.getText().toString().trim();
            String courseCode = administration_page_courseCode.getText().toString().trim();
            String courseName = administration_page_courseName.getText().toString().trim();

            String input =
                    "insert into Administration (faculty,lecturer,department,courseCode,courseName) values ('" + faculty + "','" + lecturer + "','" + department + "','" + courseCode + "','" + courseName + "')";
            database.execSQL(input);
            Toast.makeText(getApplication(), "data is inserted", Toast.LENGTH_LONG).show();


            // reset text fields
            administration_page_faculty.setText("");
            administration_page_lecturer.setText("");
            administration_page_department.setText("");
            administration_page_courseCode.setText("");
            administration_page_courseName.setText("");


            database.close();

        } catch (SQLiteException e) {
            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    // method to update information of the database
    public void update_database() {
        String faculty = administration_page_faculty.getText().toString().trim();
        String lecturer = administration_page_lecturer.getText().toString().trim();
        String department = administration_page_department.getText().toString().trim();
        String courseCode = administration_page_courseCode.getText().toString().trim();
        String courseName = administration_page_courseName.getText().toString().trim();

        // Search for there equivalent part
        String query = "SELECT * FROM Administration WHERE " +
                "faculty = '" + faculty + "' AND " +
                "lecturer ='" + lecturer + "' AND " +
                "department ='" + department + "' AND " +
                "courseCode ='" + courseCode + "' AND " +
                "courseName ='" + courseName + "'";


        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);

            // Execute the query and display the results
            Cursor cursor = database.rawQuery(query, null);
            ArrayList<String> searchResults = new ArrayList<>();

            while (cursor.moveToNext()) {
                String result = "Faculty: " + cursor.getString(cursor.getColumnIndexOrThrow("faculty")) +
                        "\nLecturer: " + cursor.getString(cursor.getColumnIndexOrThrow("lecturer")) +
                        "\nDepartment: " + cursor.getString(cursor.getColumnIndexOrThrow("department")) +
                        "\nCourse Code: " + cursor.getString(cursor.getColumnIndexOrThrow("courseCode")) +
                        "\nCourse Name: " + cursor.getString(cursor.getColumnIndexOrThrow("courseName"));

                searchResults.add(result);
            }
            // closing the database
            cursor.close();
            database.close();

            if (!searchResults.isEmpty()){
                // reset text fields
                administration_page_faculty.setText("");
                administration_page_lecturer.setText("");
                administration_page_department.setText("");
                administration_page_courseCode.setText("");
                administration_page_courseName.setText("");

                // Pass the data to the Update page activity
                Intent intent = new Intent(MainActivity.this, Update_page_administrator.class);
                intent.putExtra("faculty", faculty);
                intent.putExtra("lecturer", lecturer);
                intent.putExtra("department", department);
                intent.putExtra("courseCode", courseCode);
                intent.putExtra("courseName", courseName);
                startActivityForResult(intent, REQUEST_CODE);
            }else {
                Toast.makeText(this,"try again,didn't find a match",Toast.LENGTH_SHORT).show();
            }
        } catch (SQLiteException e) {
            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    // method to update information of the database
    public void search_database() {
        // open database
        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);

            String faculty = administration_page_faculty.getText().toString().trim();
            String lecturer = administration_page_lecturer.getText().toString().trim();
            String department = administration_page_department.getText().toString().trim();
            String courseCode = administration_page_courseCode.getText().toString().trim();
            String courseName = administration_page_courseName.getText().toString().trim();


            // Build the SQL query based on the provided search term
            String query;
            if (faculty.isEmpty() && lecturer.isEmpty() && department.isEmpty() && courseCode.isEmpty() && courseName.isEmpty()) {
                // If no search term is provided, select all records
                query = "SELECT * FROM Administration";
            } else {
                // Search for there equivalent part
                query = "SELECT * FROM Administration WHERE " +
                        "faculty = '" + faculty + "' OR " +
                        "lecturer ='" + lecturer + "' OR " +
                        "department ='" + department + "' OR " +
                        "courseCode ='" + courseCode + "' OR " +
                        "courseName ='" + courseName + "'";
            }

            // Execute the query and display the results
            Cursor cursor = database.rawQuery(query, null);
            ArrayList<String> searchResults = new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.text_color_layout, searchResults);

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
            cursor.close();
            database.close();

        } catch (SQLiteException e) {
            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }




    private void toggleFullscreen() {
        // Get data from the original ListView
        List<String> originalData = getDataFromListView();

        // Start the new activity
        Intent intent = new Intent(MainActivity.this, university_list.class);
        intent.putStringArrayListExtra("data", (ArrayList<String>) originalData);
        startActivity(intent);
    }

    // Helper method to get data from the original ListView
    private List<String> getDataFromListView() {
        List<String> originalData = new ArrayList<>();

        // Get the data from the original ListView
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) listView.getAdapter();
        int itemCount = adapter.getCount();

        for (int i = 0; i < itemCount; i++) {
            originalData.add(adapter.getItem(i));
        }

        return originalData;
    }

    public void delete_from_database() {
        String faculty = administration_page_faculty.getText().toString().trim();
        String lecturer = administration_page_lecturer.getText().toString().trim();
        String department = administration_page_department.getText().toString().trim();
        String courseCode = administration_page_courseCode.getText().toString().trim();
        String courseName = administration_page_courseName.getText().toString().trim();
        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);

            // Execute the query and display the results
            Cursor cursor = database.rawQuery(buildSelectQuery(faculty,lecturer,department,courseCode,courseName), null);
            ArrayList<String> searchResults = new ArrayList<>();

            while (cursor.moveToNext()) {
                String result = "Faculty: " + cursor.getString(cursor.getColumnIndexOrThrow("faculty")) +
                        "\nLecturer: " + cursor.getString(cursor.getColumnIndexOrThrow("lecturer")) +
                        "\nDepartment: " + cursor.getString(cursor.getColumnIndexOrThrow("department")) +
                        "\nCourse Code: " + cursor.getString(cursor.getColumnIndexOrThrow("courseCode")) +
                        "\nCourse Name: " + cursor.getString(cursor.getColumnIndexOrThrow("courseName"));

                searchResults.add(result);
            }
            // closing the database
            cursor.close();
            database.close();

            if (!searchResults.isEmpty()){
                // reset text fields
                administration_page_faculty.setText("");
                administration_page_lecturer.setText("");
                administration_page_department.setText("");
                administration_page_courseCode.setText("");
                administration_page_courseName.setText("");

                // Pass the data to the Delete_Alert activity
                Intent intent = new Intent(MainActivity.this, Delete_Alert.class);
                intent.putExtra("faculty", faculty);
                intent.putExtra("lecturer", lecturer);
                intent.putExtra("department", department);
                intent.putExtra("courseCode", courseCode);
                intent.putExtra("courseName", courseName);
                startActivityForResult(intent, REQUEST_CODE);

            }else {
                Toast.makeText(this,"try again,didn't find a match",Toast.LENGTH_SHORT).show();
            }
        } catch (SQLiteException e) {
            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refresh the data after deletion
                search_database();
                populateSpinners();
                refresh_tab3();
            }
        }

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
    // Populate the Spinners with distinct values from the database
    private void populateSpinners() {
        List<String> facultyValues = getDistinctColumnValues("faculty");
        List<String> departmentValues = getDistinctColumnValues("department");
        List<String> advisorValues = getDistinctColumnValues("lecturer");

        // Create ArrayAdapter and set it to the respective Spinners
        ArrayAdapter<String> facultyAdapter = null;
        facultyAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.text_color2_layout, facultyValues);
        facultyAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        spinner_faculty.setAdapter(facultyAdapter);

        ArrayAdapter<String> departmentAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.text_color2_layout, departmentValues);
        departmentAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        spinner_department.setAdapter(departmentAdapter);

        ArrayAdapter<String> advisorAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.text_color2_layout, advisorValues);
        advisorAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        spinner_Advisor.setAdapter(advisorAdapter);
    }

    private void insert_into_student() {
        // Open the database
        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);

            // Getting values from UI components
            String gender;
            int chkId = tab2GenderRadioGroup.getCheckedRadioButtonId();
            if (chkId == tab2MaleRadioButton.getId()) {
                gender = "Male";
            } else if (chkId == tab2FemaleRadioButton.getId()) {
                gender = "Female";
            } else {
                // Handle the case where no radio button is selected
                Toast.makeText(getApplication(), "Please select gender", Toast.LENGTH_LONG).show();
                return;
            }

            String name = tab2Name.getText().toString().trim();
            String lastName = tab2LastName.getText().toString().trim();
            String faculty = spinner_faculty.getSelectedItem().toString().trim();
            String department = spinner_department.getSelectedItem().toString().trim();
            String advisor = spinner_Advisor.getSelectedItem().toString().trim();

            // Generate a unique 10-digit random number for student_id
            int studentId = generateUniqueStudentId();

            // Insert values into the Student table
            String input = "INSERT INTO Student (student_id, gender, name, lastname, faculty, department, advisor) " +
                    "VALUES (" + studentId + ",'" + gender + "','" + name + "','" + lastName + "','" + faculty + "','" + department + "','" + advisor + "')";
            database.execSQL(input);

            Toast.makeText(getApplication(), "Student data is inserted", Toast.LENGTH_LONG).show();

            // Reset UI components
            tab2GenderRadioGroup.clearCheck();
            tab2Name.setText("");
            tab2LastName.setText("");
            spinner_faculty.setSelection(0);
            spinner_department.setSelection(0);
            spinner_Advisor.setSelection(0);

            // Close the database
            database.close();
        } catch (SQLiteException e) {
            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Helper method to generate a unique 10-digit random student ID
    private int generateUniqueStudentId() {
        Random random = new Random();
        int studentId;

        // Keep generating a random ID until it is unique
        do {
            studentId = random.nextInt(1000000000) + 1000000000; // Generate a 9-digit number
        } while (isStudentIdExists(studentId)); // Check if the ID already exists

        return studentId;
    }
    // Helper method to check if the generated student ID already exists in the database
    private boolean isStudentIdExists(long studentId) {
        Cursor cursor = database.rawQuery("SELECT * FROM Student WHERE student_id = " + studentId, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }
    // refreshing the student table list view
    private void refresh_tab3() {

        // open database
        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);

            // Build the SQL query based on the provided search term
            String query;

            query = "SELECT * FROM Student";

            // Execute the query and display the results
            Cursor cursor = database.rawQuery(query, null);
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
            tab3_listview.setAdapter(adapter);
            cursor.close();
            database.close();

        } catch (SQLiteException e) {
            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void Search_tab2() {
        // open database
        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);

            String searchTerm = SearchBar_tab2.getText().toString() ;

            // Build the SQL query based on the provided search term
            String query;
            if (searchTerm.isEmpty()) {
                // If no search term is provided, select all records
                query = "SELECT * FROM Student";
            } else {
                // Search for there equivalent part
                query = "SELECT * FROM Student WHERE " +
                        "student_id = '" + searchTerm + "' OR " +
                        "name = '" + searchTerm + "' OR " +
                        "lastname ='" + searchTerm + "' OR " +
                        "faculty ='" + searchTerm + "' OR " +
                        "department ='" + searchTerm + "' OR " +
                        "advisor ='" + searchTerm + "'";
            }

            // Execute the query and display the results
            Cursor cursor = database.rawQuery(query, null);
            ArrayList<String> searchResults = new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.text_color_layout, searchResults);

            while (cursor.moveToNext()) {
                String result =
                        "Student Id: " + cursor.getString(cursor.getColumnIndexOrThrow("student_id")) +
                                "\nGender: " + cursor.getString(cursor.getColumnIndexOrThrow("gender")) +
                                "\nName: " + cursor.getString(cursor.getColumnIndexOrThrow("name")) +
                                "\nLastname: " + cursor.getString(cursor.getColumnIndexOrThrow("lastname")) +
                                "\nFaculty: " + cursor.getString(cursor.getColumnIndexOrThrow("faculty"))+
                                "\nDepartment: " + cursor.getString(cursor.getColumnIndexOrThrow("department"))+
                                "\nAdvisor: " + cursor.getString(cursor.getColumnIndexOrThrow("advisor"));

                searchResults.add(result);
            }

            // Set the adapter to the ListView to display the search results
            tab3_listview.setAdapter(adapter);
            cursor.close();
            database.close();

        } catch (SQLiteException e) {
            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            // Open the AboutActivity when the "About" menu item is clicked
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == tab2UpdateButton) {
            tab_2_Udate();
        }else if (v == tab2DeleteButton) {
            tab_2_Delete();
        }else if (v == tab2SearchButton) {
            Search_tab2();
        }
        else if (v == tab2RegisterButton) {
                if (tab2AllInfoCheckBox.isChecked()){
                insert_into_student();
                refresh_tab3();
            }else {
                    Toast.makeText(this,"press the check box down below",Toast.LENGTH_LONG).show();
                }
        }
         else if (v == tab2CancelButton) {
            tab2GenderRadioGroup.clearCheck();
            tab2Name.setText("");
            tab2LastName.setText("");
            spinner_faculty.setSelection(0);
            spinner_department.setSelection(0);
            spinner_Advisor.setSelection(0);
        } else if (v == administration_page_btnAdd) {
            insert_into_db();
            search_database();
            populateSpinners();
        } else if (v == administration_page_btnViewUnivesityTable) {
            search_database();
            toggleFullscreen();

        } else if (v == administration_page_btnUpdate) {
            update_database();

        } else if (v == administration_page_btnDelete) {

            delete_from_database();


        } else if (v == administration_page_btnSearch) {
            search_database();

        } else if (v == administration_page_btnResettxt) {

            administration_page_courseName.setText("");
            administration_page_courseCode.setText("");
            administration_page_lecturer.setText("");
            administration_page_department.setText("");
            administration_page_faculty.setText("");

        } else if (v == administration_page_btnDropDb) {
            try {
                // Check if the database exists
                if (databaseExist()) {
                    // Delete the database
                    this.deleteDatabase(path);
                    Toast.makeText(getApplication(), "Database dropped successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplication(), "Database does not exist", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void tab_2_Delete() {

        // Getting values from UI components
        String gender;
        int chkId = tab2GenderRadioGroup.getCheckedRadioButtonId();
        if (chkId == tab2MaleRadioButton.getId()) {
            gender = "Male";
        } else if (chkId == tab2FemaleRadioButton.getId()) {
            gender = "Female";
        } else {
            gender = "";
        }
        String name = tab2Name.getText().toString().trim();
        String lastname = tab2LastName.getText().toString().trim();
        String faculty = spinner_faculty.getSelectedItem().toString().trim();
        String department = spinner_department.getSelectedItem().toString().trim();
        String advisor = spinner_Advisor.getSelectedItem().toString().trim();
        //
        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);

            // Execute the query and display the results, String name, String lastname, String gender, String faculty, String department, String advisor

            Cursor cursor = database.rawQuery(buildSelectStudentQuery(name, lastname,gender, faculty, department, advisor), null);
            ArrayList<String> searchResults = new ArrayList<>();

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
            if (!searchResults.isEmpty()){
            // closing the database
            cursor.close();
            database.close();

                // reset text fields
                tab2GenderRadioGroup.clearCheck();
                tab2Name.setText("");
                tab2LastName.setText("");
                spinner_faculty.setSelection(0);
                spinner_department.setSelection(0);
                spinner_Advisor.setSelection(0);

                // Pass the data to the Delete_tab2_student_Alert activity
                Intent intent = new Intent(MainActivity.this, Delete_tab2_student_Alert.class);
                intent.putExtra("name", name);
                intent.putExtra("lastname", lastname);
                intent.putExtra("gender", gender);
                intent.putExtra("faculty", faculty);
                intent.putExtra("department", department);
                intent.putExtra("advisor", advisor);
                startActivityForResult(intent, REQUEST_CODE);

        }else {
            Toast.makeText(this,"try again,didn't find a match",Toast.LENGTH_SHORT).show();
        }
        } catch (SQLiteException e) {
            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
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

    private void tab_2_Udate() {

        // Getting values from UI components
        String gender;
        int chkId = tab2GenderRadioGroup.getCheckedRadioButtonId();
        if (chkId == tab2MaleRadioButton.getId()) {
            gender = "Male";
        } else if (chkId == tab2FemaleRadioButton.getId()) {
            gender = "Female";
        } else {
            gender = null;
        }
        String name = tab2Name.getText().toString().trim();
        String lastname = tab2LastName.getText().toString().trim();
        String faculty = spinner_faculty.getSelectedItem().toString().trim();
        String department = spinner_department.getSelectedItem().toString().trim();
        String advisor = spinner_Advisor.getSelectedItem().toString().trim();
        //
        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);

            // Search for there equivalent part
            String query = "SELECT * FROM Student WHERE " +
                    "name = '" + name + "' AND " +
                    "lastname ='" + lastname + "' AND " +
                    "gender ='" + gender + "' AND " +
                    "faculty ='" + faculty + "' AND " +
                    "department ='" + department + "' AND " +
                    "advisor ='" + advisor + "'";
            // Execute the query


            Cursor cursor = database.rawQuery(query, null);
            ArrayList<String> searchResults = new ArrayList<>();

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
            if (!searchResults.isEmpty()){
                // closing the database
                cursor.close();
                database.close();

                // reset text fields
                tab2GenderRadioGroup.clearCheck();
                tab2Name.setText("");
                tab2LastName.setText("");
                spinner_faculty.setSelection(0);
                spinner_department.setSelection(0);
                spinner_Advisor.setSelection(0);

                // Pass the data to the Delete_tab2_student_Alert activity
                Intent intent = new Intent(MainActivity.this, Update_tab2_student.class);
                intent.putExtra("name", name);
                intent.putExtra("lastname", lastname);
                intent.putExtra("gender", gender);
                intent.putExtra("faculty", faculty);
                intent.putExtra("department", department);
                intent.putExtra("advisor", advisor);
                startActivityForResult(intent, REQUEST_CODE);

            }else {
                Toast.makeText(this,"try again,didn't find a match",Toast.LENGTH_SHORT).show();
            }
        } catch (SQLiteException e) {
            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void popoutDialog(String student_id,String name,String lastname
            ,String gender,String faculty,String department, String advisor) {
        final Dialog customDialog = new Dialog(this);
        customDialog.setTitle("ID query");

        // match customDialog with custom dialog layout
        customDialog.setContentView(R.layout.dialoge_popup_box);

        TextView dialogpoptxt = customDialog.findViewById(R.id.dialogpoptxt);
        dialogpoptxt.setText(
                        "Student id: "+student_id+"\n"+
                        "Name: "+name+"\n"+
                        "Lastname: "+lastname+"\n"+
                        "Gender: "+gender+"\n"+
                        "Faculty: "+faculty+"\n"+
                        "Department: "+department+"\n"+
                        "Advisor: "+advisor);

        customDialog.show();
    }
}