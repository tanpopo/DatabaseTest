package com.example.mrk.databasetest;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

/**
 * Created by 0025110012 on 2015/12/17.
 */
public class MyOpenHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "database_1";
    private static String tag = "MyOpenHelper";

    private String[][] INITIAL_VALUES = {
            {"apple", "red"},
            {"lemmmon", "yellow"},
    };

    public MyOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            SQLiteStatement stmt;
            db.execSQL("create table table1(name primary key, val text not null);");
            stmt = db.compileStatement("insert into table1 values(?, ?);");
            for (String[] capital : INITIAL_VALUES) {
                stmt.bindString(1, capital[0]);
                stmt.bindString(2, capital[1]);
                stmt.executeInsert();
            }
            db.setTransactionSuccessful();
            Log.e(tag, "created successfully!");
        } catch (SQLException e) {
            Log.e(tag, e.toString());
        } finally {
            db.endTransaction();
        }
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(tag, "ERROR: Not Implemented!");
    }
}

