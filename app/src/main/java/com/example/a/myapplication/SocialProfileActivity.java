package com.example.a.myapplication;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

public class SocialProfileActivity extends Fragment {


    ImageView ivProfilePic;
    TextView tvUsername,tvEmail,tvPhone,tvCarInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_social_profile,container,false);
        ivProfilePic=view.findViewById(R.id.ivProfilePic);

        tvUsername=view.findViewById(R.id.tvUsername);
        tvEmail=view.findViewById(R.id.tvEmail);
        tvPhone=view.findViewById(R.id.tvPhone);
        tvCarInfo=view.findViewById(R.id.tvCarInfo);

        tvUsername.setText(SharedPrefManager.getInstance(getContext()).getUsername());
        tvEmail.setText(SharedPrefManager.getInstance(getContext()).getUserEmail());
        tvPhone.setText(SharedPrefManager.getInstance(getContext()).getUserphone());
        tvCarInfo.setText(SharedPrefManager.getInstance(getContext()).getCarModel()+" , "+
                SharedPrefManager.getInstance(getContext()).getCarSerial());

        ImageRequest imageRequest=new ImageRequest(Constants.URL_PROFILE_PHOTO
                + SharedPrefManager.getInstance(getContext()).getImageName(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        ivProfilePic.setImageBitmap(bitmap);
                    }
                }
                ,0,0,ImageView.ScaleType.CENTER_CROP,null,

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }

        );
        RequestHandler.getInstance(getContext()).addToRequestQueue(imageRequest);


        return view;
    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_profile);

        ivProfilePic=findViewById(R.id.ivProfilePic);

        tvUsername=findViewById(R.id.tvUsername);
        tvEmail=findViewById(R.id.tvEmail);
        tvPhone=findViewById(R.id.tvPhone);
        tvCarInfo=findViewById(R.id.tvCarInfo);

        tvUsername.setText(SharedPrefManager.getInstance(this).getUsername());
        tvEmail.setText(SharedPrefManager.getInstance(this).getEmail());
        tvPhone.setText(SharedPrefManager.getInstance(this).getPhone());
        tvCarInfo.setText(SharedPrefManager.getInstance(this).getCarModel()+" , "+
                SharedPrefManager.getInstance(this).getCarSerial());

        ImageRequest imageRequest=new ImageRequest(Constants.URL_PROFILE_PHOTO
                +SharedPrefManager.getInstance(getApplicationContext()).getImageName(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        ivProfilePic.setImageBitmap(bitmap);
                    }
                }
                ,0,0,ImageView.ScaleType.CENTER_CROP,null,

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }

        );
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(imageRequest);

    }*/
}
