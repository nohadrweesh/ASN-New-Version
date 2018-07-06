//package com.example.a.myapplication;
//
//import android.content.Intent;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by Speed on 06/07/2018.
// */
//
//public class serviceCenters {
//
//    public void sendID() {
//        final int carId = SharedPrefManager.getInstance(this).getCarId();
//        StringRequest stringRequest = new StringRequest(Request.Method.POST,
//                Constants.URL_CENTER,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        try {
//                            JSONObject obj = new JSONObject(response);
//                            if (obj.getString("status").equals("exists")) {
//                                String centerID = obj.getString("id");
//                                String centerName = obj.getString("name");
//                                // new activity ..
//                                startActivity(new Intent(getApplicationContext(), CenterSelectionActivity.class));
//                                finish();
//                            } else if (obj.getString("status").equals("doesn't exist")) {
//                                startActivity(new Intent(getApplicationContext(), CenterSelectionActivity.class));
//                                finish();
//
//                            } else {
//                                Toast.makeText(
//                                        getApplicationContext(),
//                                        obj.getString("message"),
//                                        Toast.LENGTH_LONG
//                                ).show();
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(), Constants.URL_SEND_DATA + " " + error.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("carID", String.valueOf(carId));
//                return params;
//            }
//        };
//    }
//
//}
