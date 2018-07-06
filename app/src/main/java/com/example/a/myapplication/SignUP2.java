package com.example.a.myapplication;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
public class SignUP2 extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SignUP2";

    private EditText editTextUsername, editTextEmail, editTextPassword,editTextPhone,editTextCarID,editTextCarSerial;
    private Button buttonRegister,btnChoosePic;
    private ProgressDialog progressDialog;

    private TextView textViewLogin;
    ImageView ivProfilePic;
    Bitmap bitmap;

    final int CODE_GALLERY_REQUEST=999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, MainActivity.class));//////go to another screen
            return;
        }

        btnChoosePic=(Button)findViewById(R.id.btnChoosePhoto);
        ivProfilePic=(ImageView)findViewById(R.id.ivChosenImg);

        editTextEmail = (EditText) findViewById(R.id.editeTextEmailid);
        editTextUsername = (EditText) findViewById(R.id.editTextUsernameid);
        editTextPassword = (EditText) findViewById(R.id.editTextPasswordid);
        editTextPhone = (EditText) findViewById(R.id.editTextPhoneid);
        editTextCarID = (EditText) findViewById(R.id.editTextCarid);
        editTextCarSerial = (EditText) findViewById(R.id.editTextCarserial);
        // textViewLogin = (TextView) findViewById(R.id.textViewLogin);

        buttonRegister = (Button) findViewById(R.id.register);

        progressDialog = new ProgressDialog(this);

        buttonRegister.setOnClickListener(this);
        /// textViewLogin.setOnClickListener(this);

        btnChoosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(SignUP2.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},CODE_GALLERY_REQUEST);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==CODE_GALLERY_REQUEST){
            if(grantResults.length>0&& grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,CODE_GALLERY_REQUEST);

            }else{
                Toast.makeText(this,"You don'thave permissions",Toast.LENGTH_LONG).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==CODE_GALLERY_REQUEST && resultCode==RESULT_OK&&data!=null){
            Uri filepath=data.getData();
            try {
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                ivProfilePic.setImageBitmap(bitmap);
                //upload for demo only



            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void registerUser() {
        final String email = editTextEmail.getText().toString().trim();
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();
        final String carId = editTextCarID.getText().toString().trim();
        final String carSerial = editTextCarSerial.getText().toString().trim();
        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onResponse: with "+response);

                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error")){
                                SharedPrefManager.getInstance(getApplicationContext())
                                        .setUserInfo(
                                                obj.getInt("userID"),
                                                obj.getInt("carID")
                                        );
                                SharedPrefManager.getInstance(getApplicationContext())
                                        .setImageName(obj.getString("image_name"));
                                startActivity(new Intent(getApplicationContext(), SignIn.class));
                                finish();
                            }else{
                                Toast.makeText(
                                        getApplicationContext(),
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                            }

                            Log.d("Main Activity", "onResponse: "+response);

                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Main Activity", "onResponse: error "+response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), Constants.URL_REGISTER+" "+error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String encodedImg=imageToString(bitmap);
                params.put("image",encodedImg);

                params.put("userName", username);
                params.put("email", email);
                params.put("password", password);
                params.put("phonenumber", phone);
                params.put("CarModel", carId);
                params.put("CarSerial", carSerial);
                params.put("token",String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getToken()));
                Log.d("signup", "getParams: token is "+String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getToken()));


                return params;
            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);


    }

    @Override
    public void onClick(View view) {
        if (view == buttonRegister)
            registerUser();
        if(view == textViewLogin)
            startActivity(new Intent(this, SignIn.class));
    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[]imageBytes=outputStream.toByteArray();
        String encodedImg= Base64.encodeToString(imageBytes,Base64.DEFAULT);
        return  encodedImg;
    }
}
