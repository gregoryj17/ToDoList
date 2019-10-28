package com.csce4623.ahnelson.todolist;

import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

//Create HomeActivity and implement the OnClick listener
public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initializeComponents();
        ((RecyclerView) findViewById(R.id.recyclerView)).setLayoutManager(new LinearLayoutManager(this));
        String[] projection = {
                ToDoProvider.TODO_TABLE_COL_ID,
                ToDoProvider.TODO_TABLE_COL_TITLE,
                ToDoProvider.TODO_TABLE_COL_CONTENT,
                ToDoProvider.TODO_TABLE_COL_DATE,
                ToDoProvider.TODO_TABLE_COL_DONE};
        Cursor myCursor = getContentResolver().query(ToDoProvider.CONTENT_URI,projection,null,null,null);
        ((RecyclerView) findViewById(R.id.recyclerView)).setAdapter(new ToDoAdapter(myCursor));
        ConnectivityReceiver cr = new ConnectivityReceiver();
        registerReceiver(cr, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
    //Set the OnClick Listener for buttons
    void initializeComponents(){
        findViewById(R.id.btnNewNote).setOnClickListener(this);
        findViewById(R.id.btnDeleteNote).setOnClickListener(this);


    }

    //Handles buttons being pressed
    @Override
    public void onClick(View v){
        switch (v.getId()){
            //If new Note, call createNewNote()
            case R.id.btnNewNote:
                createNewNote();
                break;
            //If delete note, call deleteNewestNote()
            case R.id.btnDeleteNote:
                deleteNewestNote();
                break;
            /*case R.id.button:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;*/
            //This shouldn't happen
            default:
                Intent intent = new Intent(this, NoteActivity.class);
                String message = ((Button)v).getText().toString();
                intent.putExtra(MainActivity.EXTRA_MESSAGE, message);
                startActivity(intent);
                break;
        }
        updateList();
    }

    //Updates the list
    void updateList(){
       String[] projection = {
                ToDoProvider.TODO_TABLE_COL_ID,
                ToDoProvider.TODO_TABLE_COL_TITLE,
                ToDoProvider.TODO_TABLE_COL_CONTENT,
                ToDoProvider.TODO_TABLE_COL_DATE,
                ToDoProvider.TODO_TABLE_COL_DONE};
        Cursor myCursor = getContentResolver().query(ToDoProvider.CONTENT_URI,projection,null,null,null);
        ((RecyclerView) findViewById(R.id.recyclerView)).setAdapter(new ToDoAdapter(myCursor));
    }

    //Goes to note creation screen
    void createNewNote(){
        /*
        //Create a ContentValues object
        ContentValues myCV = new ContentValues();
        //Put key_value pairs based on the column names, and the values
        myCV.put(ToDoProvider.TODO_TABLE_COL_TITLE,"New Note");
        myCV.put(ToDoProvider.TODO_TABLE_COL_CONTENT,"Note Content");
        //Perform the insert function using the ContentProvider
        getContentResolver().insert(ToDoProvider.CONTENT_URI,myCV);
        //Set the projection for the columns to be returned
        String[] projection = {
                ToDoProvider.TODO_TABLE_COL_ID,
                ToDoProvider.TODO_TABLE_COL_TITLE,
                ToDoProvider.TODO_TABLE_COL_CONTENT};
        //Perform a query to get all rows in the DB
        Cursor myCursor = getContentResolver().query(ToDoProvider.CONTENT_URI,projection,null,null,null);
        //Create a toast message which states the number of rows currently in the database
        Toast.makeText(getApplicationContext(),Integer.toString(myCursor.getCount()),Toast.LENGTH_LONG).show();
        */

        Intent intent = new Intent(this, NoteActivity.class);
        startActivity(intent);


    }

    //Delete the newest note placed into the database
    void deleteNewestNote(){
        //Create the projection for the query
       String[] projection = {
                ToDoProvider.TODO_TABLE_COL_ID,
                ToDoProvider.TODO_TABLE_COL_TITLE,
                ToDoProvider.TODO_TABLE_COL_CONTENT,
                ToDoProvider.TODO_TABLE_COL_DATE,
                ToDoProvider.TODO_TABLE_COL_DONE};

        //Perform the query, with ID Descending
        Cursor myCursor = getContentResolver().query(ToDoProvider.CONTENT_URI, projection,null,null,"_ID DESC");
        if(myCursor != null & myCursor.getCount() > 0) {
            //Move the cursor to the beginning
            myCursor.moveToFirst();
            //Get the ID (int) of the newest note (column 0)
            int newestId = myCursor.getInt(0);
            //Delete the note
            int didWork = getContentResolver().delete(Uri.parse(ToDoProvider.CONTENT_URI + "/" + newestId), null, null);
            //If deleted, didWork returns the number of rows deleted (should be 1)
            if (didWork == 1) {
                //If it didWork, then create a Toast Message saying that the note was deleted
                Toast.makeText(getApplicationContext(), "Deleted Note " + newestId, Toast.LENGTH_LONG).show();
            }
        } else{
            Toast.makeText(getApplicationContext(), "No Note to delete!", Toast.LENGTH_LONG).show();

        }


    }

}

//A View Adapter
class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.TodoViewHolder>{

    Cursor cursor;

    //Creates an adapter with the given cursor
    public ToDoAdapter(Cursor c){
        cursor = c;
    }

    //Creates a new row of data
    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_main, viewGroup, false);
        return new TodoViewHolder(view);
    }

    //Binds the data to a row in the recycler view
    @Override
    public void onBindViewHolder(TodoViewHolder viewHolder, int i) {
        cursor.moveToPosition(i);
        viewHolder.textView.setText(cursor.getString(0)+" "+cursor.getString(1)+"\n"+cursor.getString(2)+"\n"+cursor.getString(3)+" "+(cursor.getString(4).equals("1")?"Complete":"Incomplete"));
    }

    //Returns the number of items in the adapter
    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    //A personalized ViewHolder
    class TodoViewHolder extends RecyclerView.ViewHolder{
        Button textView;

        public TodoViewHolder(View itemView){
            super(itemView);
            textView = (Button) itemView.findViewById(R.id.text_view);

        }

    }

}


