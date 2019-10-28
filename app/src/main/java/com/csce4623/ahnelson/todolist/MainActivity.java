package com.csce4623.ahnelson.todolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "id ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
    }

    public void onClick(View v){
        Intent intent = new Intent(this, NoteActivity.class);
        String message = ((Button)v).getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);

    }

}
