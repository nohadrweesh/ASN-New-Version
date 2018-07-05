package com.example.a.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.a.myapplication.utils.NotificationUtils;

public class ProblemDetailsActivity extends AppCompatActivity {

    private String title, msg;
    private TextView prbTitle, prbMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_details);

        Intent i = getIntent();
        title = i.getStringExtra("title");
        msg = i.getStringExtra("msg");

        prbTitle = (TextView) findViewById(R.id.type);
        prbMsg = (TextView) findViewById(R.id.msg);

        prbTitle.setText(title);
        prbMsg.setText(msg);

    }

    public void accept(View view){
        HelpUtils.getInstance(this).sendHelpTo(NotificationUtils.toDriverID,NotificationUtils.toCarID,NotificationUtils.problemID);
        finish();
    }

    public void reject(View view){
        startActivity(new Intent(this, DriverMapActivity.class));
        finish();
    }
}
