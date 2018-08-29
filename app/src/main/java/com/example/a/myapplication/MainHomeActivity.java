package com.example.a.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class MainHomeActivity extends Fragment {
    Button btnRequests,btnAddTrackers,btnTracking,bnAdvertising,btnCarBusiness,btnMyOffes,btnMaintenance;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_home2,container,false);
         btnRequests=view.findViewById(R.id.btnRequests);
         btnAddTrackers=view.findViewById(R.id.btnAddTrackers);
        btnTracking=view.findViewById(R.id.btnTracking);
        bnAdvertising=view.findViewById(R.id.btnAdvertising);
        btnCarBusiness=view.findViewById(R.id.btnCarBusiness);
        btnMyOffes=view.findViewById(R.id.btnMyOffers);
        btnMaintenance=view.findViewById(R.id.btnMaintenance);

        btnRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requests();
            }
        });
        btnAddTrackers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              addTrackers();
            }
        });
        btnTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracking();
            }
        });
        bnAdvertising.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                advertising();
            }
        });
        btnCarBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carBusiness();
            }
        });
        btnMyOffes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myOffers();
            }
        });
        btnMaintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maintaince();
            }
        });


        return  view;

    }


    public void requests() {
        Intent i = new Intent(getContext(), ConnectionRequestsActivity.class);
        startActivity(i);

    }

    public void addTrackers() {
        Intent i = new Intent(getContext(), FindTrackerActivity.class);
        startActivity(i);

    }

    public void tracking() {
        Intent i = new Intent(getContext(), TrackersListActivity.class);
        startActivity(i);

    }

    public void advertising() {
        Intent i = new Intent(getContext(), AdvertisementsActivity.class);
        startActivity(i);

    }

    public void maintaince() {
       // Intent i = new Intent(getContext(), Profile2Activity.class);
       // startActivity(i);

        final int carId = SharedPrefManager.getInstance(getContext()).getCarId();
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
                                        getContext(),
                                        obj.getString("centers"),
                                        Toast.LENGTH_LONG
                                ).show();
                            } else if (obj.getInt("num_centers")==0) {
                                startActivity(new Intent(getContext(), CenterSelectionActivity.class));
                                //finish();

                            } else {
                                Toast.makeText(
                                        getContext(),
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
                        Toast.makeText(getContext(), Constants.URL_SEND_DATA + " " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("carID", String.valueOf(carId));
                return params;
            }

        };

        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }


    public void carBusiness() {
        Intent i = new Intent(getContext(), CBHome.class);
        startActivity(i);
    }

    public  void myOffers ( ){
        Intent i = new Intent(getContext(), OffersActivity.class);
        startActivity(i);
    }





}
