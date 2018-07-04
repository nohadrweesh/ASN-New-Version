package com.example.a.myapplication;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class CBcatalogue extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextModel, editTextStatus, editTextPrice;
    private Button buttonAdvertise;
    private ProgressDialog progressDialog;

    private TextView textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbcatalogue);

       /* if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, catalogue.class));//////go to another screen
            return;
        }*/

        editTextPrice = (EditText) findViewById(R.id.editTextPrice);





        buttonAdvertise = (Button) findViewById(R.id.btnAdvertise);

        progressDialog = new ProgressDialog(this);
        buttonAdvertise.setOnClickListener(this);

    }

    private void registerCar() {
        //  final String carModel = editTextModel.getText().toString().trim();
        final String carPrice = editTextPrice.getText().toString().trim();
        // final String carStatus = editTextStatus.getText().toString().trim();

        progressDialog.setMessage("Advertising Car...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_ADVERTISECAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error")){
                               /* SharedPrefManager.getInstance(getApplicationContext())
                                        .setUserInfo(
                                                obj.getInt("personID")
                                        );*/
                                Toast.makeText(
                                        getApplicationContext(),
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                                startActivity(new Intent(getApplicationContext(),CBMainActivity2.class));
                                finish();
                            }else{
                                Toast.makeText(
                                        getApplicationContext(),
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                            }

                            Log.d("Catalogue", "onResponse: "+response);

                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Catalogue", "onResponse: error "+response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), Constants.URL_ADVERTISECAR+" "+error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ownerID" , Integer.toString(SharedPrefManager.getInstance(getApplicationContext())
                        .getUserId()));

                Log.d("User id from CB ", Integer.toString(SharedPrefManager.getInstance(getApplicationContext())
                        .getUserId()));

                //  params.put("model", carModel);

                params.put("price", carPrice);


                // params.put("token",String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getToken()));
                Log.d("catalogue", "getParams: token is "+String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getToken()));


                return params;
            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onClick(View view) {
        if (view == buttonAdvertise)
            registerCar();

    }
}
