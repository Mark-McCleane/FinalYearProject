package com.example.samapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.samapp.toDoList_db.*;

public class toDoListDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "todolist.db";
    public static final int DATABASE_VERSION= 1;

    public toDoListDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODOLIST_TABLE = "CREATE TABLE " +
                toDoListEntry.TABLE_NAME + " (" +
                toDoListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                toDoListEntry.COLUMN_ITEM + " TEXT NOT NULL" +
                ");";
        db.execSQL(CREATE_TODOLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + toDoListEntry.TABLE_NAME);
        onCreate(db);
    }
}
