package com.example.a.myapplication.sync;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.a.myapplication.HelpUtils;
import com.example.a.myapplication.SellUtils;
import com.example.a.myapplication.utils.NotificationUtils;

/**
 * Created by Speed on 14/04/2018.
 */

public class HelpTasks {
    public static final String ACTION_ACCEPT_HELP_REQUEST = "accept-help-request";


    public static final String ACTION_DISMISS_HELP_REQUEST = "dismiss-help-request";



    public static final String ACTION_ACCEPT_SELL_REQUEST = "accept-sell-request";

    public static final String ACTION_DISMISS_SELL_REQUEST = "dismiss-sell-request";


    private static final String TAG = "HelpTasks";

    public static void executeTask(Context context, String action) {
        if (ACTION_ACCEPT_HELP_REQUEST.equals(action)) {
            Log.d(TAG, "executeTask: action "+action);
            HelpUtils.getInstance(context).sendHelpTo(NotificationUtils.toDriverID,NotificationUtils.toCarID,NotificationUtils.problemID);


        } else if (ACTION_DISMISS_HELP_REQUEST.equals(action)) {
            Log.d(TAG, "executeTask: action "+action);
        }
        else if (ACTION_ACCEPT_SELL_REQUEST.equals(action)) {
            Log.d(TAG, "ACCEPT SELL"+action);

            SellUtils.getInstance(context).sendHelpTo(NotificationUtils.CBdriver,NotificationUtils.CBcar);
            Toast.makeText(context,"you have your car sold ,so your account is invalid ,if you will buy a new car " +
                    "you must register with a new account  ",Toast.LENGTH_LONG).show();


        } else if (ACTION_DISMISS_SELL_REQUEST.equals(action)) {
            Log.d(TAG, "executeTask: action "+action);
        }




    }
}
