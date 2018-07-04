package com.example.a.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
    }


    public void obd(View view) {
        Intent i = new Intent(this, ObdActivity.class);
        startActivity(i);

    }

    public void requests(View view) {
        Intent i = new Intent(this, ConnectionRequestsActivity.class);
        startActivity(i);

    }

    public void addTrackers(View view) {
        Intent i = new Intent(this, ConnectionRequestsActivity.class);
        startActivity(i);

    }

    public void tracking(View view) {
        Intent i = new Intent(this, TrackersListActivity.class);
        startActivity(i);

    }

    public void advertising(View view) {
        Intent i = new Intent(this, TrackersListActivity.class);
        startActivity(i);

    }

    public void maintaince(View view) {
        Intent i = new Intent(this, Profile2Activity.class);
        startActivity(i);

    }

    public void carBusiness(View view) {
        Intent i = new Intent(this, CBHome.class);
        startActivity(i);

    }





}
