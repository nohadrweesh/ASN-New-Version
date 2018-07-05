package com.example.a.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import android.Manifest;
public class AcceptorInfo extends AppCompatActivity {

    ////////////////////////////////////
    /////// YOMNA HESHAM 19 APRIL //////
    ////////////////////////////////////
    private static final String TAG = "Acceptor Info";
    private static Context mContext;

    private static int acceptorID;
    private static String acceptorName;
    private static String acceptorPhone;
    private static double acceptorLng;
    private static double acceptorLat;
    private static double acceptorAtit;

    private TextView TextViewAcceptorName, TextViewAcceptorPhone,
            TextViewAcceptorLng, TextViewAcceptorLat, TextViewAcceptorAtit;
    ////////////////////////////////////
    ////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptor_info);

        ////////////////////////////////////
        /////// YOMNA HESHAM 19 APRIL //////
        ////////////////////////////////////
        Intent i = getIntent();
        acceptorID    = i.getIntExtra(   "acceptorID", -1);
        acceptorName  = i.getStringExtra("username");
        acceptorPhone = i.getStringExtra("phone"   );
        acceptorLng   = i.getDoubleExtra(   "lng" , -1);
        acceptorLat   = i.getDoubleExtra(   "lat" , -1);
        acceptorAtit  = i.getDoubleExtra(   "atit", -1);

        TextViewAcceptorName  = (TextView) findViewById(R.id.name);
        TextViewAcceptorPhone = (TextView) findViewById(R.id.phone);
        TextViewAcceptorLng   = (TextView) findViewById(R.id.lng);
        TextViewAcceptorLat   = (TextView) findViewById(R.id.lat);
        TextViewAcceptorAtit  = (TextView) findViewById(R.id.atit);

        TextViewAcceptorName.setText (acceptorName );
        TextViewAcceptorPhone.setText(acceptorPhone);
        TextViewAcceptorLng.setText  (String.valueOf(acceptorLng)  );
        TextViewAcceptorLat.setText  (String.valueOf(acceptorLat)  );
        TextViewAcceptorAtit.setText (String.valueOf(acceptorAtit) );

        ////////////////////////////////////
        ////////////////////////////////////

    }

    public void showLocation(View view) {
        //go to map
        //send the location of the pb person
        Intent i =new Intent(this,DriverMapActivity.class);
        i.putExtra("lat",acceptorLat);
        i.putExtra("long",acceptorLng);
        i.putExtra("name",acceptorName);
        startActivity(i);
    }

    public void call(View view){
        Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
        phoneCallIntent.setData(Uri.parse("tel:"+acceptorPhone));

        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED) {
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
