package com.example.samapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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

        new ItemTouchHelper((new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){  //LEFT AND RIGHT SWIPE TO DEL

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                removeItem((long) viewHolder.itemView.getTag());
            }
        })).attachToRecyclerView(todoListView);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
        setItemCount();
    }

    private void removeItem(long tag) {
        sqLiteDatabase.delete(toDoList_db.toDoListEntry.TABLE_NAME,
                toDoList_db.toDoListEntry._ID + "= ?", new String[]{String.valueOf(tag) }
                );
        tdla.swapCursor(getAllItems());
        setItemCount();
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
        setItemCount();
    }

    private Cursor getAllItems() {
        return sqLiteDatabase.query(toDoList_db.toDoListEntry.TABLE_NAME,null,null,
                null,null,null,null);
    }

    private void setItemCount(){
        tvItemsAmount.setText(String.valueOf(tdla.getItemCount() ));
    }
}
