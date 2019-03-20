package com.example.samapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class toDoList extends AppCompatActivity {
    private EditText etvItem;
    private TextView tvItemsAmount;
    private Button btnAdd;
    private SQLiteDatabase sqLiteDatabase;
    private ToDoListAdapter tdla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        toDoListDBHelper dbHelper = new toDoListDBHelper(this);
        sqLiteDatabase = dbHelper.getWritableDatabase();

        etvItem = findViewById(R.id.etv_input_task);
        tvItemsAmount = findViewById(R.id.tvItemsCount);
        btnAdd = findViewById(R.id.button_add);

        RecyclerView todoListView = findViewById(R.id.toDoList);
        todoListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        tdla = new ToDoListAdapter(getApplicationContext(),getAllItems());
        todoListView.setAdapter(tdla);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
    }

    private void addItem() {
        if(etvItem.getText().toString().trim().length() == 0 ||
                etvItem.getText()== null){
            return;
        }
        String item = etvItem.getText().toString();
        ContentValues contentValues = new ContentValues();
        contentValues.put(toDoList_db.toDoListEntry.COLUMN_ITEM, item);

        sqLiteDatabase.insert(toDoList_db.toDoListEntry.TABLE_NAME,null,
                contentValues);
        tdla.swapCursor(getAllItems());
        etvItem.getText().clear();
    }

    private Cursor getAllItems() {
        return sqLiteDatabase.query(toDoList_db.toDoListEntry.TABLE_NAME,null,null,
                null,null,null,null);
    }
}
