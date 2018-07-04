package com.example.a.myapplication;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Speed on 13/04/2018.
 */

public class SellUtils {
    private static final String TAG = "HelpUtils";
    private static Context mContext;
    private static SellUtils mInstance;

    private  SellUtils(Context context) {
        mContext = context;
    }

    public static  SellUtils getInstance(Context context) {
        if (mInstance == null)
            mInstance = new  SellUtils(context);
        return mInstance;
    }

    public void sendbuy(final Product p, final String message ) {

        Log.d(TAG, "sendbuy: starts");


        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_SENDBUY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "SendBUYonResponse: starts with response " + response);
                        //tx.setText(response);

                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.d(TAG, "onResponse: " + response);
                            if (!obj.getBoolean("error")) {
                                Log.d(TAG, "onResponse: sendbuy  sent");

                            } else {
                                Log.d(TAG, "onResponse: error " + obj.getString("message"));

                            }


                        } catch (JSONException e) {
                            Log.d(TAG, "onResponse: error" + response);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: starts error " + error.toString());

                    }

                }
        ) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {



                Map<String, String> params = new HashMap<>();

                //  params.put("time", currentTime);
                params.put("carid", p.getCarID());
                params.put("userid", String.valueOf(SharedPrefManager.getInstance(mContext).getUserId()));
                Log.d("SELLUTILS",String.valueOf(SharedPrefManager.getInstance(mContext).getUserId()));
                params.put("message", message);
                params.put("title", String.valueOf(SharedPrefManager.getInstance(mContext).getUsername()));

                // params.put("token",SharedPrefManager.getInstance(mContext).getToken());



                return params;
            }

        };
        RequestHandler.getInstance(mContext).addToRequestQueue(stringRequest);

    }


    public void sendHelpTo(final int clientID,final int carID){
        Log.d(TAG, "accept offer: starts");

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_ACCEPTOFFER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponseACCEPT_OFFER: starts with response " + response);


                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.d(TAG, "onResponse: " + response);
                            if (!obj.getBoolean("error")) {
                                Log.d(TAG, "onResponse: sentHelp sent");

                            } else {
                                Log.d(TAG, "onResponse: error " + obj.getString("message"));

                            }


                        } catch (JSONException e) {
                            Log.d(TAG, "onResponse: error" + response);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: starts error " + error.toString());

                    }

                }
        ) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                // final String currentTime = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
                //  Log.d(TAG, "getParams: starts with " + currentTime);
                Map<String, String> params = new HashMap<>();

                params.put("buyerID",String.valueOf(clientID));
                params.put("message","Accepted and contact seller");
                params.put("title",SharedPrefManager.getInstance(mContext).getUsername());
                params.put("carid",String.valueOf(carID));

                //   params.put("token",SharedPrefManager.getInstance(mContext).getToken());
                // params.put("sellerID", String.valueOf(SharedPrefManager.getInstance(mContext).getUserId()));



                return params;
            }

        };
        RequestHandler.getInstance(mContext).addToRequestQueue(stringRequest);

    }

}
