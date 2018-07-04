package com.example.a.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;


public class FindTrackerActivity extends AppCompatActivity {

    EditText emailEditText ;
    TextView resultTextView;
    ProgressBar progressBar;
    static String trackerEmail;
    static Tracker matchedTracker;
    static Driver currentDriver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_tracker);

        setViews();
        progressBar.setVisibility(View.GONE);
        Log.d("FindTrackersActivity","onCreate ");
    }

    private void setViews()
    {
        emailEditText =findViewById(R.id.email_et);
        resultTextView = findViewById(R.id.tracker_tv);
        progressBar = findViewById(R.id.progressBar);
    }


    //when search button is clicked
    public void searchTrackers(View view)
    {
        Log.d("FindTrackersActivity ", "inside searchDrivers");
        trackerEmail = emailEditText.getText().toString();
        Log.d("FindTrackersActivity ", "matchedDriver email is "+trackerEmail);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams parameters= new RequestParams();
        parameters.put("email",trackerEmail);

        httpClient.get("http://asnasucse18.000webhostapp.com/RFTDA/FetchTracker.php",parameters,new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressBar.setVisibility(View.GONE);
                Log.d("FindTrackersActivity ", "inside searchDrivers onSuccess");
                Log.d("FindTrackersActivity ", "inside searchDrivers onSuccess response is "+response.toString());
                try
                {
                    matchedTracker = getTrackerFromJSON(response);
                    resultTextView.setText(matchedTracker.getUserName());
                }
                catch (JSONException e)
                {
                    resultTextView.setText("error in parsing matchedDriver information");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressBar.setVisibility(View.GONE);
                Log.d("FindsFriendsActivity ", "inside searchTrackers onFailure");
                Log.d("FindsFriendsActivity ", "inside searchTrackers onFailure response is "+errorResponse.toString());
                resultTextView.setText(" Failure in HTTP request");
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private Tracker getTrackerFromJSON(JSONObject response) throws JSONException
    {
        int trackerID=response.getInt("trackerID");
        String trackerName=response.getString("trackerUserName");
        String trackerPhonenumber = response.getString("trackerPhonenumber");
        String trackerStatus = response.getString("trackerStatus");
        String trackerGender = response.getString("trackerGender");
        Tracker tracker=new Tracker(trackerID,trackerName,trackerPhonenumber,trackerStatus,trackerGender,trackerEmail);
        Log.d("FindTrackersActivity", "tracker info is "+tracker.toString());

        return tracker;
    }

    //when a matchedDriver is selected
    public void DisplayNonTrackerProfile(View view)
    {
        //TODO: least important: check first if they are connected => display FriendProfileActivity , else=> display NonFriendActivity
        Intent i = new Intent(getApplicationContext(),NonTrackerProfileActivity.class);
        i.putExtra("matchedTracker", matchedTracker);
        if(getIntent().hasExtra("currentUser"))
        {
            currentDriver= (Driver) getIntent().getSerializableExtra("currentUser");
            i.putExtra("currentUser", currentDriver);
        }
        startActivity(i);
    }
}
