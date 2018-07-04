package com.example.a.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import static com.android.volley.VolleyLog.TAG;

public class CBProfileActivity extends AppCompatActivity {


    private TextView textViewUsername,
            textViewUserEmail, textViewPhone,textViewModel,textViewSerial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbprofile);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {


            textViewUsername = (TextView) findViewById(R.id.nametext);
            textViewUserEmail = (TextView) findViewById(R.id.emailtext);
            textViewModel=(TextView)findViewById(R.id.carmodel);
            textViewSerial=(TextView)findViewById(R.id.carserial) ;
            textViewPhone = (TextView) findViewById(R.id.phonetext);

            textViewUserEmail.setText(Html.fromHtml("<b>"+"User Email: " + "</b> " + SharedPrefManager.getInstance(this).getUserEmail()));
            textViewUsername.setText(Html.fromHtml("<b>"+"User Name: " + "</b> " + SharedPrefManager.getInstance(this).getUsername()));
            textViewPhone.setText(Html.fromHtml("<b>"+"User Phone: " + "</b> " + SharedPrefManager.getInstance(this).getUserphone()));
            textViewModel.setText(Html.fromHtml("<b>"+"Car Model: " + "</b> " +SharedPrefManager.getInstance(this).getCarModel()));

            textViewSerial.setText(Html.fromHtml("<b>"+"Car Serial: " + "</b> " +SharedPrefManager.getInstance(this).getCarSerial()));





        }

    }
}
