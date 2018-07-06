package com.example.a.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class SendHelp extends AppCompatActivity {
    private static final String TAG = "HelpUtils";

    private static int userID;
    private static String username;
    private static String Phone;
    private static double Lng;
    private static double Lat;
    private static double Atit;
    private static String ProblemType;
    private static String ProblemMessage,carNumber,phoneNumber;
    private TextView TextViewUserName,
            TextViewAcceptorLng, TextViewAcceptorLat, TextViewAcceptorAtit, TextViewPhone, TextViewProblemType, TextViewProblemMessage
            ,CarNumberTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_help);
        Intent i = getIntent();
        Log.d(TAG, "onCreate: starts from intent = "+i.getExtras().toString());
        userID    = i.getIntExtra(   "userID", -1);
        username  = i.getStringExtra("username");
        Phone = i.getStringExtra("phone"   );
        Lng   = i.getDoubleExtra(   "lng" , -1);
        Lat   = i.getDoubleExtra(   "lat" , -1);
        Atit  = i.getDoubleExtra(   "atit", -1);
        ProblemType = i.getStringExtra("pbType");
        ProblemMessage = i.getStringExtra("pbMsg");
        carNumber=i.getStringExtra("carNumber");
        phoneNumber=i.getStringExtra("phoneNumber");

        TextViewUserName  = (TextView) findViewById(R.id.name);
        TextViewPhone = (TextView) findViewById(R.id.phone);
        TextViewAcceptorLng   = (TextView) findViewById(R.id.lng);
        TextViewAcceptorLat   = (TextView) findViewById(R.id.lat);
        TextViewAcceptorAtit  = (TextView) findViewById(R.id.atit);
        TextViewProblemType = (TextView) findViewById(R.id.type);
        TextViewProblemMessage = (TextView) findViewById(R.id.msg);
        CarNumberTextView = (TextView) findViewById(R.id.carNumber);

        TextViewUserName.setText (username);
        TextViewPhone.setText(phoneNumber);
        TextViewAcceptorLng.setText  (String.valueOf(Lng) );
        TextViewAcceptorLat.setText  (String.valueOf(Lat)  );
        TextViewAcceptorAtit.setText (String.valueOf(Atit) );
        TextViewProblemType.setText(ProblemType);
        TextViewProblemMessage.setText(ProblemMessage);
        CarNumberTextView.setText(carNumber);
    }

    public void showLocation(View view) {
        //go to map
        //send the location of the pb person
        Intent i =new Intent(this,TabbedActivity.class);
        i.putExtra("lat",Lat);
        i.putExtra("long",Lng);
        i.putExtra("name",username);
        startActivity(i);
    }

    public void call(View view){
        Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
        phoneCallIntent.setData(Uri.parse("tel:"+phoneNumber));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.CALL_PHONE
                }, 50);
            }
        }
        else {
            startActivity(phoneCallIntent);
        }
    }
}
