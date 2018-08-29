package com.example.a.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainHomeActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home2);
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
        Intent i = new Intent(this, FindTrackerActivity.class);
        startActivity(i);

    }

    public void tracking(View view) {
        Intent i = new Intent(this, TrackersListActivity.class);
        startActivity(i);

    }

    public void advertising(View view) {
        Intent i = new Intent(this, AdvertisementsActivity.class);
        startActivity(i);

    }

    public void maintaince(View view) {
       // Intent i = new Intent(this, Profile2Activity.class);
       // startActivity(i);

        final int carId = SharedPrefManager.getInstance(this).getCarId();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_CENTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d(TAG, "onResponse: centers with "+response);
                        //{"num_centers":2,"centers":{"center0":{"id":"1","name":"service center 1"},"center1":{"id":"2","name":"service center 2"}}}

                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getInt("num_centers")>0) {
                                /*String centerID = obj.getString("id");
                                String centerName = obj.getString("name");
                                // new activity ..
                                startActivity(new Intent(getApplicationContext(), CenterSelectionActivity.class));
                                finish();*/

                                Toast.makeText(
                                        getApplicationContext(),
                                        obj.getString("centers"),
                                        Toast.LENGTH_LONG
                                ).show();
                            } else if (obj.getInt("num_centers")==0) {
                                startActivity(new Intent(getApplicationContext(), CenterSelectionActivity.class));
                                finish();

                            } else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), Constants.URL_SEND_DATA + " " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("carID", String.valueOf(carId));
                return params;
            }

        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }


    public void carBusiness(View view) {
        Intent i = new Intent(this, CBHome.class);
        startActivity(i);
    }

    public  void myOffers (View view ){
        Intent i = new Intent(this, OffersActivity.class);
        startActivity(i);
    }





}
