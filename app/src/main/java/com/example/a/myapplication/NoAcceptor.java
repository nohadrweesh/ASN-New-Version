package com.example.a.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class NoAcceptor extends AppCompatActivity {

    HelpUtils mHelpUtils;
    private int problemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_acceptor);

        Intent i = getIntent();
        problemID = i.getIntExtra("prbID", -1);

    }

    public void tryAgain(View view){
        Intent i = new Intent(getApplicationContext(), Waiting.class);
        i.putExtra("problemID", problemID);
        startActivity(i);
        finish();
    }
}
