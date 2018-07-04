package com.example.a.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TrackerProfileActivity extends AppCompatActivity {

    Tracker tracker;
    int currentUserID;

    TextView trackerNameTV, trackerNameTV2, trackerEmailTV, trackerMobileTV,trackerGenderTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker_profile);

        tracker= (Tracker) getIntent().getSerializableExtra("tracker");
        currentUserID = getIntent().getIntExtra("currentUserID",0);

        setViews();

    }

    private void setViews()
    {
        trackerNameTV = findViewById(R.id.t_name_tv);
        trackerNameTV2 = findViewById(R.id.t_name_tv2);
        trackerEmailTV = findViewById(R.id.t_email_tv);
        trackerMobileTV = findViewById(R.id.t_mobile_tv);
        trackerGenderTV = findViewById(R.id.t_gender_tv);

        trackerNameTV.setText(tracker.getUserName());
        trackerNameTV2.setText(tracker.getUserName());
        trackerEmailTV.setText(tracker.getEmail());
        trackerMobileTV.setText(tracker.getPhonenumber());
        trackerGenderTV.setText(tracker.getGender());
    }

    public void removeTracker(View v)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        String url= "http://asnasucse18.000webhostapp.com/RFTDA/RemoveConnection.php";
        RequestParams params= new RequestParams();
        params.put("senderID",tracker.getID());
        params.put("receiverID",currentUserID);
        client.get(url,params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try
                {
                    if(response.getBoolean("result")==true)
                    {
                        Button removeBtn = findViewById(R.id.removeTracker_btn);
                        removeBtn.setVisibility(View.GONE);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"something went wrong during processing your request",Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e)
                {
                    Toast.makeText(getApplicationContext(),"error while parsing the response",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(),"error with the GET request",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void goToTrackersListActivity(View v)
    {
        Intent i=new Intent(getApplicationContext(),TrackersListActivity.class);
        startActivity(i);
    }

}
