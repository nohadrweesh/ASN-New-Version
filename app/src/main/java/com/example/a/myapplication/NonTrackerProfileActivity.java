package com.example.a.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class NonTrackerProfileActivity extends AppCompatActivity {

    Tracker profileOwner;
    Driver currentDriver;
    int currentDriverID;

    Button addTrackerBtn;
    TextView poNameTV, poNameTV2, poEmailTV, poGenderTV;
    ImageView poProfilePicture;

    boolean cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_tracker_profile);

        profileOwner = (Tracker) getIntent().getSerializableExtra("matchedTracker");
        currentDriverID = SharedPrefManager.getInstance(getApplicationContext()).getUserId();

        setViews();

        cancel= false;
        if(getIntent().hasExtra("cancel")==true)
        {
            addTrackerBtn.setText("Cancel Request");
            cancel=true;
        }
    }

    public void addTracker(View v)
    {
        if(!cancel)
        {
            AsyncHttpClient httpClient = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("senderID",currentDriverID);
            params.put("receiverID",profileOwner.getID());
            Log.d("NnTrackerProfActivity","senderID "+currentDriverID+" receiverID  "+profileOwner.getID());
            httpClient.get("http://asnasucse18.000webhostapp.com/RFTDA/SendFriendRequestToTracker.php",params, new JsonHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                {
                    addTrackerBtn.setText("Cancel Request");
                    cancel=true;
                    Log.d("NnFriendProfileActivity","addFriend connection request is sent successfuly");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("NnFriendProfileActivity","SendFriendRequestToTracker onFailure");
                }
            });
        }
        else
        {
            AsyncHttpClient httpClient = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("senderID",currentDriverID);
            params.put("receiverID",profileOwner.getID());
            Log.d("NnTrackerProfActivity","senderID "+currentDriverID+" receiverID  "+profileOwner.getID());
            final boolean[] requestSuccess = {false};
            httpClient.get("http://asnasucse18.000webhostapp.com/RFTDA/DeleteFriendRequestToTracker.php",params, new JsonHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                {
                    addTrackerBtn.setText("Add");
                    cancel=false;
                    Log.d("NnFriendProfileActivity","delete connection request is sent successfuly");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("NnFriendProfileActivity","DeleteFriendRequestToTracker onFailure");
                }
            });
        }
    }

    private void setViews()
    {
        poNameTV = findViewById(R.id.nt__name_tv);
        poNameTV2 = findViewById(R.id.nt__name_tv2);
        poEmailTV = findViewById(R.id.nt__email_tv);
        poGenderTV = findViewById(R.id.nt__gender_tv);
        poProfilePicture = findViewById(R.id.nt_profile_picture_iv);
        addTrackerBtn = findViewById(R.id.addTracker_btn);

        poNameTV.setText(profileOwner.getUserName());
        poNameTV2.setText(profileOwner.getUserName());
        poEmailTV.setText(profileOwner.getEmail());
        poGenderTV.setText(profileOwner.getGender());
    }


}
