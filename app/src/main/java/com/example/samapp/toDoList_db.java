package com.example.samapp;

import android.provider.BaseColumns;

public class toDoList_db {
    private toDoList_db() {}

    public static final class toDoListEntry implements BaseColumns {
        public static final String TABLE_NAME = "ToDoList";
        public static final String COLUMN_ITEM = "item";
    }
}
