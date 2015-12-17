package com.example.mrk.databasetest;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private static String tag = "MainActivity";
    private MyOpenHelper helper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new MyOpenHelper(this);

        //set
        Button setButton = (Button) findViewById(R.id.setButton);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(tag, "set button clicked!");
                SQLiteDatabase db = null;
                try {
                    db = helper.getWritableDatabase();

                    EditText prefNameEdit = (EditText) findViewById(R.id.nameIn);
                    String name = ((SpannableStringBuilder) prefNameEdit.getText()).toString();

                    EditText prefValEdit = (EditText) findViewById(R.id.valIn);
                    String val = ((SpannableStringBuilder) prefValEdit.getText()).toString();

                    long id = insertString(db, name, val);
                    if (id > 0) {
                        Log.d(tag, "insert success id=" + Long.toString(id));
                    } else {
                        Log.e(tag, "ERROR: insert failed");
                    }
                } catch (SQLiteException e) {
                    Log.e(tag, e.toString());
                } finally {
                    if (db != null) {
                        db.close();
                    }
                }
            }
        });
    }

    long insertString(SQLiteDatabase db, String name, String val) {
        ContentValues insertValues = new ContentValues();
        insertValues.put("name", name);
        insertValues.put("val", val);
        return db.insert("person", null, insertValues);
    }

}

