package com.example.a.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class CBHome extends AppCompatActivity {
    public Button btn1;
    public Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbhome);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.display_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.sell:
                Intent int1 = new Intent(CBHome.this, CBcatalogue.class);
                Log.d("Home", "Starting Activity////////////////////////////////////");
                startActivity(int1);
                break;
            case R.id.viewtobesold:
                Log.d("Home", "Starting CB AVAILABLE Activity////////////////////////////////////");
                Intent int2 = new Intent(CBHome.this, CBMainActivity2.class);
                startActivity(int2);
                break;

            case R.id.logout:
                SharedPrefManager.getInstance(this).logout();
                finish();
                startActivity(new Intent(this, SignIn.class));
                break;
            case R.id.profile:
                startActivity(new Intent(this, CBProfileActivity.class));
                break;


        }
        return true;
    }
   /* public void Buy(View view){
        btn1 = (Button)findViewById(R.id.btnbuy);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int1 = new Intent(Home.this,MainActivity2.class);
                startActivity(int1);
            }
        });
    }
    public void Sell(View view){
        btn2 = (Button)findViewById(R.id.btnsell);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int1 = new Intent(Home.this,catalogue.class);
                Log.d("Home","Starting Activity////////////////////////////////////");
                startActivity(int1);
            }
        });
    }*/
}