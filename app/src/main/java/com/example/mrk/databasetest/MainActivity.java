package com.example.mrk.databasetest;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private static String tag = "MainActivity";
    private SQLiteDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyOpenHelper helper = new MyOpenHelper(this);
        db = helper.getWritableDatabase();

        //set
        Button setButton = (Button) findViewById(R.id.setButton);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(tag, "set button clicked!");

                EditText prefNameEdit = (EditText) findViewById(R.id.nameIn);
                String name = ((SpannableStringBuilder) prefNameEdit.getText()).toString();

                EditText prefValEdit = (EditText) findViewById(R.id.valIn);
                String val = ((SpannableStringBuilder) prefValEdit.getText()).toString();

                long id = insertString(name, val);

            }
        });
    }

    long insertString(String name, String val) {
        ContentValues insertValues = new ContentValues();
        insertValues.put("name", name);
        insertValues.put("val", val);
        return db.insert("person", name, insertValues);
    }

}

