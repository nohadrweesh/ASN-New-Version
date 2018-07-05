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
    int currentUserID;
    String currentUserEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_tracker);

        setViews();
        progressBar.setVisibility(View.GONE);
        Log.d("FindTrackersActivity","onCreate ");

        currentUserID=SharedPrefManager.getInstance(getApplicationContext()).getUserId();
        currentUserEmail = SharedPrefManager.getInstance(getApplicationContext()).getUserEmail();
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
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://asnasucse18.000webhostapp.com/RFTDA/FetchTrackers.php";
        RequestParams params = new RequestParams();
        Log.d("TrackersListActivity","current user ID "+currentUserID);
        params.put("receiverID",currentUserID);
        client.get(url,params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try
                {
                    if(isConnection(response))
                    {
                        Intent i = new Intent(getApplicationContext(),TrackerProfileActivity.class);
                        i.putExtra("tracker", matchedTracker);
                        i.putExtra("currentUserID",currentUserID );

                        startActivity(i);
                    }

                    else
                    {
                        //ashof el nas elli ba3ten ll tracker da , ashof hal ana mn dmn elli ba3tnlo
                        AsyncHttpClient client = new AsyncHttpClient();
                        String url = "http://asnasucse18.000webhostapp.com/RFTDA/SeeRequestsToTracker.php";
                        RequestParams params = new RequestParams();
                        Log.d("FindTrackerActivity","current user ID "+currentUserID);
                        params.put("receiverID",matchedTracker.getID());
                        client.get(url,params, new JsonHttpResponseHandler()
                        {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response1) {
                                try
                                {
                                    if(friendRequestPreviouslySentToTracker(response1))
                                    {
                                        Log.d("FindTrackerActivity","request previously sent To tracker");
                                        Intent i = new Intent(getApplicationContext(),NonTrackerProfileActivity.class);
                                        i.putExtra("matchedTracker", matchedTracker);
                                        i.putExtra("cancel",true);

                                        startActivity(i);
                                    }
                                    else
                                    {

                                        AsyncHttpClient client = new AsyncHttpClient();
                                        String url = "http://asnasucse18.000webhostapp.com/RFTDA/SeeRequests.php";
                                        RequestParams params = new RequestParams();
                                        Log.d("FindTrackerActivity","current user ID "+currentUserID);
                                        params.put("receiverID",currentUserID);
                                        client.get(url,params, new JsonHttpResponseHandler()
                                        {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, JSONObject response2)
                                            {
                                                try {
                                                    if(friendRequestPreviouslySentToDriver(response2))
                                                    {
                                                        Log.d("FindTrackerActivity","request previously sent to Driver");
                                                        Intent i = new Intent(getApplicationContext(),ConnectionSenderProfileActivity.class);
                                                        i.putExtra("sender", matchedTracker);
                                                        startActivity(i);
                                                    }
                                                    else
                                                    {
                                                        Log.d("FindTrackerActivity","no request previously sent");
                                                        Intent i = new Intent(getApplicationContext(),NonTrackerProfileActivity.class);
                                                        i.putExtra("matchedTracker", matchedTracker);
                                                        startActivity(i);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
                                            {
                                            }
                                        });

                                    }
                                }
                                catch (JSONException e)
                                {
                                    Log.d("FindTrackerActivity","onSuccess json error2");
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                Log.d("FindTrackerActivity","onFailure");
                            }
                        });

                    }
                }
                catch (JSONException e)
                {
                    Log.d("FindTrackerActivity","onSuccess json error1");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("FindTrackerActivity","onFailure");
            }
        });


    }

    private boolean isConnection(JSONObject response) throws JSONException {
        Log.d("TrackersListActivity","handleResponse response is "+response);
        int trackersNumber = response.getJSONObject("result").getInt("TrackersNumber");
        if(trackersNumber == 0)
            return false;
        else
        {
            for (int i=0;i<trackersNumber;i++)
            {
                String key = "tracker"+Integer.toString(i+1);
                String trackerEmail2 = response.getJSONObject("result").getJSONObject(key).getString("trackerEmail");

                if(trackerEmail.equals(trackerEmail2))
                    return true;
                Log.d("FindTrackerActivity","isConnection ");
            }
        }
        return false;

    }

    private boolean friendRequestPreviouslySentToTracker(JSONObject response)throws JSONException
    {
        int requestsNumber = response.getJSONObject("result").getInt("ConnectionRequestsNumber");
        if(requestsNumber == 0)
            return false;
        for (int i=0;i<requestsNumber;i++)
        {
            String key = "sender"+Integer.toString(i+1);
            String senderEmail = response.getJSONObject("result").getJSONObject(key).getString("senderEmail");
            if(currentUserEmail.equals(senderEmail))
            {

                Log.d("FindTrackerActivity","request previously sent To Tracker ");
                return true;
            }
        }

        return false;
    }

    private boolean friendRequestPreviouslySentToDriver(JSONObject response)throws JSONException
    {
        int requestsNumber = response.getJSONObject("result").getInt("ConnectionRequestsNumber");
        if(requestsNumber == 0)
            return false;
        for (int i=0;i<requestsNumber;i++)
        {
            String key = "sender"+Integer.toString(i+1);
            String senderEmail = response.getJSONObject("result").getJSONObject(key).getString("senderEmail");
            if(trackerEmail.equals(senderEmail))
            {

                Log.d("FindTrackerActivity","request previously sent to Driver");
                return true;
            }
        }

        return false;
    }
}

