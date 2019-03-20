package com.example.samapp;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoListViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    private final String TAG = "ToDoListAdapter";

    public ToDoListAdapter(Context context, Cursor curse){
        mContext = context;
        mCursor = curse;
    }
    public class ToDoListViewHolder extends RecyclerView.ViewHolder {
        public TextView itemText;
        //todo count text

        public ToDoListViewHolder(@NonNull View itemView) {
            super(itemView);

            itemText = (TextView) itemView.findViewById(R.id.tv_TODO_item);
            Log.d(TAG, "ToDoListViewHolder: "+ itemText);
        }
    }

    @Override
    public ToDoListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View v = layoutInflater.inflate(R.layout.todo_item, viewGroup,false);
        return new ToDoListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ToDoListViewHolder toDoListViewHolder, int i){
        if(!mCursor.moveToPosition(i)){
            return;
        }

        String item = mCursor.getString(mCursor.getColumnIndex(toDoList_db.toDoListEntry.COLUMN_ITEM));
        long id = mCursor.getLong(mCursor.getColumnIndex(toDoList_db.toDoListEntry._ID));
        Log.d(TAG, "onBindViewHolder: " + item);
        


        toDoListViewHolder.itemText.setText(item);
        toDoListViewHolder.itemView.setTag(id);
        // TODO: 20/03/2019 do counter
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCurse){
        if(mCursor != null){
            mCursor.close();
        }
        mCursor = newCurse;

        if(newCurse != null){
            //update recyclerview
            notifyDataSetChanged();
        }
    }
}