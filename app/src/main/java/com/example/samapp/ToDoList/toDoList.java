package com.example.samapp.ToDoList;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samapp.R;

import java.util.List;

public class toDoList extends AppCompatActivity {
    private EditText etvItem;
    private TextView tvItemsAmount;
    private Button btnAdd;
    private SQLiteDatabase sqLiteDatabase;
    private ToDoListAdapter tdla;
    private SpeechRecognizer speechRecognizer;

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

        FloatingActionButton toDoListFab = findViewById(R.id.MicFab);
        toDoListFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM) ;
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                speechRecognizer.startListening(intent);
            }
        });
        startSpeechRecognizer();
    }

    private void removeItem(long tag) {
        sqLiteDatabase.delete(toDoList_db.toDoListEntry.TABLE_NAME,
                toDoList_db.toDoListEntry._ID + "= ?",new String[]{String.valueOf(tag) }
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

    private void startSpeechRecognizer() {
        try {
            boolean speechRecogniserAvalible = SpeechRecognizer.isRecognitionAvailable(getApplicationContext());
            if(speechRecogniserAvalible) {
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                speechRecognizer.setRecognitionListener(new RecognitionListener() {
                    @Override
                    public void onReadyForSpeech(Bundle params) {
                        // TODO: 20/03/2019 if needed
                    }

                    @Override
                    public void onBeginningOfSpeech() {
                        // TODO: 20/03/2019 if needed
                    }

                    @Override
                    public void onRmsChanged(float rmsdB) {
                        // TODO: 20/03/2019 if needed
                    }

                    @Override
                    public void onBufferReceived(byte[] buffer) {
                        // TODO: 20/03/2019 if needed
                    }

                    @Override
                    public void onEndOfSpeech() {
                        // TODO: 20/03/2019 if needed
                    }

                    @Override
                    public void onError(int error) {
                        // TODO: 20/03/2019 if needed
                    }

                    @Override
                    public void onResults(Bundle bundle) {
                        List<String> results = bundle.getStringArrayList(
                                SpeechRecognizer.RESULTS_RECOGNITION
                        );
                        processResult(results.get(0));
                    }

                    @Override
                    public void onPartialResults(Bundle partialResults) {
                        // TODO: 20/03/2019 if needed
                    }

                    @Override
                    public void onEvent(int eventType, Bundle params) {
                        // TODO: 20/03/2019 if needed
                    }
                });
            }
        }
        catch (Exception exception){
            Log.d("IO Exception", "Exception Found" + exception.getStackTrace());
        }
    }

    private void processResult(String userInput) {
        userInput = userInput.toLowerCase(); //simplify command processing
        Toast.makeText(getApplicationContext(),"User Input is :" + userInput,
                Toast.LENGTH_LONG).show();
        if(userInput.contains("task") && userInput.contains("add")){
            String userText = userInput.substring(8, userInput.length());
            etvItem.setText(userText);
            btnAdd.performClick();
        }
        else if(userInput.contains("task")){
            String userText = userInput.substring(4, userInput.length());
            etvItem.setText(userText);
        }
        if(userInput.contains("add")){
            btnAdd.performClick();
        }

    }
}
