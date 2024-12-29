package com.example.crud_app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.crud_app.DatabaseHelper;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    EditText etUserName, etPassword, etCurrentName, etNewName, etDeleteName;
    Button btnAddData, btnViewData, btnUpdateName, btnDeleteName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        // Initialize views
        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        etCurrentName = findViewById(R.id.etCurrentName);
        etNewName = findViewById(R.id.etNewName);
        etDeleteName = findViewById(R.id.etDeleteName);
        btnAddData = findViewById(R.id.btnAddData);
        btnViewData = findViewById(R.id.viewDataBtn); // Use the correct ID defined in XML
        btnUpdateName = findViewById(R.id.btnUpdateName);
        btnDeleteName = findViewById(R.id.btnDeleteName);

        // Add data
        btnAddData.setOnClickListener(v -> {
            String userName = etUserName.getText().toString();
            String password = etPassword.getText().toString();

            if (!userName.isEmpty() && !password.isEmpty()) {
                ContentValues values = new ContentValues();
                values.put("name", userName);
                values.put("password", password);

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                long result = db.insert("users", null, values);
                db.close();

                if (result != -1) {
                    Toast.makeText(this, "Data Added Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to Add Data", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            }
        });

        // View data
        btnViewData.setOnClickListener(v -> {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query("users", null, null, null, null, null, null);

            StringBuilder data = new StringBuilder();  // To collect all the data to show in a single Toast
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String password = cursor.getString(cursor.getColumnIndex("password"));
                    data.append("Name: ").append(name).append(", Password: ").append(password).append("\n");
                } while (cursor.moveToNext());
            } else {
                data.append("No Data Found");
            }

            cursor.close();
            db.close();

            // Show the data in a Toast at the bottom
            Toast.makeText(this, data.toString(), Toast.LENGTH_LONG).show();
        });


        // Update name
        btnUpdateName.setOnClickListener(v -> {
            String currentName = etCurrentName.getText().toString();
            String newName = etNewName.getText().toString();

            if (!currentName.isEmpty() && !newName.isEmpty()) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name", newName);

                int rows = db.update("users", values, "name = ?", new String[]{currentName});
                db.close();

                if (rows > 0) {
                    Toast.makeText(this, "Name Updated Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Name Not Found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            }
        });

        // Delete name
        btnDeleteName.setOnClickListener(v -> {
            String nameToDelete = etDeleteName.getText().toString();

            if (!nameToDelete.isEmpty()) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                int rows = db.delete("users", "name = ?", new String[]{nameToDelete});
                db.close();

                if (rows > 0) {
                    Toast.makeText(this, "Name Deleted Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Name Not Found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter a name to delete", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
