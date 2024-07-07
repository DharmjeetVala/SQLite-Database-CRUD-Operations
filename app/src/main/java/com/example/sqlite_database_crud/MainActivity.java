package com.example.sqlite_database_crud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private EditText name, contact;
    private TextView dob;
    private DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        contact = findViewById(R.id.contact);
        dob = findViewById(R.id.dob);
        Button insert = findViewById(R.id.btnInsert);
        Button update = findViewById(R.id.btnUpdate);
        Button delete = findViewById(R.id.btnDelete);
        Button view = findViewById(R.id.btnView);

        DB = new DBHelper(this);

        dob.setOnClickListener(v -> showDatePickerDialog());

        insert.setOnClickListener(v -> insertData());
        update.setOnClickListener(v -> updateData());
        delete.setOnClickListener(v -> deleteData());
        view.setOnClickListener(v -> viewData());
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    dob.setText(date);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void insertData() {
        String nameTXT = name.getText().toString();
        String contactTxt = contact.getText().toString();
        String dobTXT = dob.getText().toString();

        if (isValidInput(nameTXT, contactTxt, dobTXT)) {
            boolean isInserted = DB.insertuserdata(nameTXT, contactTxt, dobTXT);
            showToast(isInserted ? "New Entry Inserted" : "New Entry Not Inserted");
        }
    }

    private void updateData() {
        String nameTXT = name.getText().toString();
        String contactTxt = contact.getText().toString();
        String dobTXT = dob.getText().toString();

        if (isValidInput(nameTXT, contactTxt, dobTXT)) {
            boolean isUpdated = DB.updateuserdata(nameTXT, contactTxt, dobTXT);
            showToast(isUpdated ? "Entry Updated" : "Entry Not Updated");

        }
    }

    private void deleteData() {
        String nameTXT = name.getText().toString();

        if (!nameTXT.isEmpty()) {
            boolean isDeleted = DB.deletedata(nameTXT);
            showToast(isDeleted ? "Entry Deleted" : "Entry Not Deleted");

        } else {
            showToast("Name cannot be empty");
        }
    }

    private void viewData() {
        Cursor res = DB.getdata();
        if (res.getCount() == 0) {
            showToast("No Entry Exists");
            return;
        }

        StringBuilder buffer = new StringBuilder();
        while (res.moveToNext()) {
            buffer.append("Name :").append(res.getString(0)).append("\n");
            buffer.append("Contact :").append(res.getString(1)).append("\n");
            buffer.append("Date of Birth :").append(res.getString(2)).append("\n\n");
        }

        showAlertDialog("User Entries", buffer.toString());
    }

    private boolean isValidInput(String name, String contact, String dob) {
        if (name.isEmpty() || contact.isEmpty() || dob.isEmpty()) {
            showToast("All fields are required");
            return false;
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .show();
    }
}