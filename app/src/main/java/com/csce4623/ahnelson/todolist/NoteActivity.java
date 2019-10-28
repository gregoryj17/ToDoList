package com.csce4623.ahnelson.todolist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class NoteActivity extends AppCompatActivity implements View.OnClickListener{

    public int ID = -1;
    public String title = "";
    public String mess = "";
    public String date = "";
    public int complete = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity);
        initializeComponents();
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        try {
            ID = Integer.parseInt(message.substring(0,message.indexOf(' ')));
            title = message.substring(message.indexOf(' ')+1,message.indexOf("\n"));
            mess = message.substring(message.indexOf("\n")+1, message.lastIndexOf("\n"));
            date = message.substring(message.lastIndexOf("\n")+1,message.lastIndexOf(' '));
            if(message.substring(message.lastIndexOf(' ')+1).equals("Complete"))complete = 1;
        } catch (Exception e){

        }

        //Toast.makeText(getApplicationContext(),Integer.toString(ID),Toast.LENGTH_LONG).show();

        ((EditText)findViewById(R.id.etNoteTitle)).setText(title);
        ((EditText)findViewById(R.id.etNoteContent)).setText(mess);
        ((EditText)findViewById(R.id.etDatePicker)).setText(date);
        ((Switch)findViewById(R.id.switchComplete)).setChecked(complete==1);

    }
    //Set the OnClick Listener for buttons
    void initializeComponents(){
        findViewById(R.id.btnSave).setOnClickListener(this);
        findViewById(R.id.btnDelete).setOnClickListener(this);


    }

    //Listens for saving/deleting
    @Override
    public void onClick(View v){
        switch (v.getId()){
            //If new Note, call createNewNote()
            case R.id.btnSave:
                createNewNote();
                break;
            //If delete note, call deleteNote()
            case R.id.btnDelete:
                deleteNote(ID);
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                break;
            //This shouldn't happen
            default:
                break;
        }
    }


    //Create a new note with info from fields
    void createNewNote(){
        //Create a ContentValues object
        ContentValues myCV = new ContentValues();
        //Put key_value pairs based on the column names, and the values
        myCV.put(ToDoProvider.TODO_TABLE_COL_TITLE,((TextView)findViewById(R.id.etNoteTitle)).getText().toString());
        myCV.put(ToDoProvider.TODO_TABLE_COL_CONTENT,((TextView)findViewById(R.id.etNoteContent)).getText().toString());
        myCV.put(ToDoProvider.TODO_TABLE_COL_DATE,((TextView)findViewById(R.id.etDatePicker)).getText().toString());
        if(((Switch)findViewById(R.id.switchComplete)).isChecked())complete = 1;
        else complete = 0;
        myCV.put(ToDoProvider.TODO_TABLE_COL_DONE, complete);
        //Perform the insert function using the ContentProvider
        deleteNote(ID);
        getContentResolver().insert(ToDoProvider.CONTENT_URI,myCV);
        //Set the projection for the columns to be returned
        String[] projection = {
                ToDoProvider.TODO_TABLE_COL_ID,
                ToDoProvider.TODO_TABLE_COL_TITLE,
                ToDoProvider.TODO_TABLE_COL_CONTENT,
                ToDoProvider.TODO_TABLE_COL_DATE,
                ToDoProvider.TODO_TABLE_COL_DONE};
        //Perform a query to get all rows in the DB
        Cursor myCursor = getContentResolver().query(ToDoProvider.CONTENT_URI, projection,null,null,null);
        //Create a toast message which states the number of rows currently in the database
        //Toast.makeText(getApplicationContext(),Integer.toString(myCursor.getCount()),Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

    }

    //Deletes a specific note
    boolean deleteNote(int i){
        if (i==-1)return false;
        //Create the projection for the query
        String[] projection = {
                ToDoProvider.TODO_TABLE_COL_ID,
                ToDoProvider.TODO_TABLE_COL_TITLE,
                ToDoProvider.TODO_TABLE_COL_CONTENT,
                ToDoProvider.TODO_TABLE_COL_DATE,
                ToDoProvider.TODO_TABLE_COL_DONE};

        //Perform the query, with ID Descending
        Cursor myCursor = getContentResolver().query(ToDoProvider.CONTENT_URI,projection,null,null,"_ID DESC");
        if(myCursor != null & myCursor.getCount() > 0) {
            int didWork = getContentResolver().delete(Uri.parse(ToDoProvider.CONTENT_URI + "/" + i), null, null);
            //If deleted, didWork returns the number of rows deleted (should be 1)
            if (didWork == 1) {
                //If it didWork, then create a Toast Message saying that the note was deleted
                //Toast.makeText(getApplicationContext(), "Deleted Note " + i, Toast.LENGTH_LONG).show();
                return true;
            }
            return false;
        } else {
            //Toast.makeText(getApplicationContext(), "No Note to delete!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

}
