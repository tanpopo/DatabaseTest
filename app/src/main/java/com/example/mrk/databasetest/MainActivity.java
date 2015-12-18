package com.example.mrk.databasetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//ref: https://github.com/abikounso/AndroidSQLiteSample01/blob/master/src/com/example/android/db/Main.java

public class MainActivity extends AppCompatActivity {
    private static String tag = "MainActivity";
    private MyOpenHelper helper = null;

    private static String DATABASE_NAME = "database_1";
    private static String TABLE_DEFAULT = "table_1";
    private static String COL_NAME = "f_name";
    private static String COL_COLOR = "f_color";

    private static String[][] INITIAL_VALUES = {
            {"apple", "red"},
            {"lemmmon", "yellow"},
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(tag, "onCreate()");
        helper = new MyOpenHelper(this);

        //create
        Button createButton = (Button) findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(tag, "init button clicked!");
                SQLiteDatabase db = null;
                try {
                    db = helper.getReadableDatabase();
                } catch (SQLiteException e) {
                    Log.e(tag, e.toString());
                } finally {
                    if (db != null) {
                        db.close();
                    }
                }
            }
        });

        //delete
        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = deleteDatabase(helper.getDatabaseName());
                 Log.d(tag, "delete database result=" + result);
            }
        });

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

        //addTable
        Button addTableButton = (Button) findViewById(R.id.addTableButton);
        addTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(tag, "add table button clicked!");

                EditText prefNameEdit = (EditText) findViewById(R.id.tableName);
                String table_name = ((SpannableStringBuilder) prefNameEdit.getText()).toString();

                createTable(helper.getWritableDatabase(), table_name);
            }
        });

    }
    private static class MyOpenHelper extends SQLiteOpenHelper {
        private static String tag = "MyOpenHelper";

        public MyOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }
        public void onCreate(SQLiteDatabase db) {
            Log.e(tag, "onCreate");
            try {
                db.execSQL("create table " + TABLE_DEFAULT + "(" + COL_NAME + " primary key, "//
                        + COL_COLOR + " text not null);");
                Log.d(tag, "create database: success!");
            } catch (SQLException e) {
                Log.e(tag, e.toString());
            } finally {
            }
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.e(tag, "ERROR: Not Implemented!");
        }
    }

    long insertString(SQLiteDatabase db, String name, String val) {
        ContentValues insertValues = new ContentValues();
        insertValues.put(COL_NAME, name);
        insertValues.put(COL_COLOR, val);
        return db.insert(TABLE_DEFAULT, null, insertValues);
    }
    boolean createTable(SQLiteDatabase db, String table_name) {
        boolean result = false;
        db.beginTransaction();
        try {
            SQLiteStatement stmt;
            db.execSQL("create table " + table_name + "(" + COL_NAME + " primary key, "//
                    + COL_COLOR + " text not null);");
            stmt = db.compileStatement("insert into " + table_name + " values(?, ?);");
            for (String[] capital : INITIAL_VALUES) {
                stmt.bindString(1, capital[0]);
                stmt.bindString(2, capital[1]);
                stmt.executeInsert();
            }
            db.setTransactionSuccessful();
            Log.d(tag, "create table: success");
        } catch (SQLException e) {
            Log.e(tag, e.toString());
            result = false;
        } finally {
            db.endTransaction();
        }
        return result;
    }
}

