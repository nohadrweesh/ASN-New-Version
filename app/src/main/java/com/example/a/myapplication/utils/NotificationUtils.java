package com.example.a.myapplication.utils;

/**
 * Created by ecs on 11/04/2018.
 */


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Action;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.a.myapplication.Main2Activity;
import com.example.a.myapplication.MainActivity;
import com.example.a.myapplication.R;
import com.example.a.myapplication.SecondActivity;
import com.example.a.myapplication.sync.HelpIntentService;
import com.example.a.myapplication.sync.HelpTasks;
import com.example.a.myapplication.vo.NotificationVo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class NotificationUtils {

    private static final String TAG = "NotificationUtils";

    private static final int NOTIFICATION_ID = 200;
    private static final String PUSH_NOTIFICATION = "pushNotification";
    private static final String CHANNEL_ID = "myChannel";
    private static final String URL = "url";
    private static final String ACTIVITY = "activity";
    Map<String, Class> activityMap = new HashMap<>();
    private Context mContext;

    private static final int ACTION_ACCEPT_HELP_PENDING_INTENT_ID = 1;
    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 14;

    private static final int HELP_PENDING_INTENT_ID = 3417;

    public static int toCarID;
    public  static int toDriverID;
    public static  int problemID;
public static int CBdriver;
public static int CBcar;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
        //Populate activity map
        activityMap.put("MainActivity", Main2Activity.class);
        activityMap.put("SecondActivity", SecondActivity.class);
    }




    /**
     * Downloads push notification image before displaying it in
     * the notification tray
     *
     * @param strURL : URL of the notification Image
     * @return : BitMap representation of notification Image
     */
    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Playing notification sound
     */
    public void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext.getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static Action ignoreAction(Context context,String notificationType) {

        Intent ignoreActionIntent = new Intent(context, HelpIntentService.class);

        ignoreActionIntent.setAction(HelpTasks.ACTION_DISMISS_HELP_REQUEST);

        PendingIntent ignoreReminderPendingIntent = PendingIntent.getService(
                context,
                ACTION_IGNORE_PENDING_INTENT_ID,
                ignoreActionIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        if(notificationType.equals("SA")){
            Action ignoreReminderAction = new Action(R.drawable.ic_cancel_black_24px,
                    "Remind me later",
                    ignoreReminderPendingIntent);

            return ignoreReminderAction;
        }
        else  if(notificationType.equals("CB")) {
            Intent ignoreActionIntent1 = new Intent(context, HelpIntentService.class);

            ignoreActionIntent1.setAction(HelpTasks.ACTION_DISMISS_SELL_REQUEST);

            PendingIntent ignoreReminderPendingIntent1 = PendingIntent.getService(
                    context,
                    ACTION_IGNORE_PENDING_INTENT_ID,
                    ignoreActionIntent1,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            Action ignoreReminderAction = new Action(R.drawable.ic_cancel_black_24px,
                    "Refuse",
                    ignoreReminderPendingIntent1);

            return ignoreReminderAction;
        }
else{
            Action ignoreReminderAction = new Action(R.drawable.ic_cancel_black_24px,
                    "No, Sorry I can't help.",
                    ignoreReminderPendingIntent);


            return ignoreReminderAction;
        }

    }


    private static Action acceptUserHelp(Context context,String notificationType) {

        Intent incrementWaterCountIntent = new Intent(context, HelpIntentService.class);

        incrementWaterCountIntent.setAction(HelpTasks.ACTION_ACCEPT_HELP_REQUEST);

        PendingIntent acceptHelpPendingIntent = PendingIntent.getService(
                context,
                ACTION_ACCEPT_HELP_PENDING_INTENT_ID,
                incrementWaterCountIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        if(notificationType.equals("SA")){
            Action acceptHelpAction = new Action(R.drawable.ic_local_drink_black_24px,
                    "Would you  like to get an appointment for checkup",
                    acceptHelpPendingIntent);

            return acceptHelpAction;
        }else if(notificationType.equals("SC-OFFER")){
            Action acceptHelpAction = new Action(R.drawable.ic_local_drink_black_24px,
                    "Want More Details ....",
                    acceptHelpPendingIntent);

            return acceptHelpAction;
        } else if(notificationType.equals("CB")){
            Intent incrementWaterCountIntent1 = new Intent(context, HelpIntentService.class);

            incrementWaterCountIntent1.setAction(HelpTasks.ACTION_ACCEPT_SELL_REQUEST);
            PendingIntent acceptHelpPendingIntent1 = PendingIntent.getService(
                    context,
                    ACTION_ACCEPT_HELP_PENDING_INTENT_ID,
                    incrementWaterCountIntent1,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            Action acceptHelpAction = new Action(R.drawable.ic_local_drink_black_24px,
                    "Accept Offer",
                    acceptHelpPendingIntent1);

            return acceptHelpAction;
        }

        else{

        Action acceptHelpAction = new Action(R.drawable.ic_local_drink_black_24px,
                "I can help him,Provide with more info",
                acceptHelpPendingIntent);

        return acceptHelpAction;
        }
    }


    public  void displayNotification(NotificationVo notificationVO, Intent resultIntent) {
        String message = notificationVO.getMessage();
        String title = notificationVO.getTitle();
        String iconUrl = notificationVO.getIconUrl();
        String action = notificationVO.getAction();
        String destination = notificationVO.getActionDestination();
        String notificationType = notificationVO.getNotificationType();
        toCarID=notificationVO.getToCarID();
        toDriverID=notificationVO.getToDriverID();
        problemID=notificationVO.getProblemID();//TODO:
        CBcar=notificationVO.getCBcar();
        CBdriver=notificationVO.getCBdriver();
        Log.d(TAG, "displayNotification: toDriverID "+String.valueOf(toDriverID));
        Log.d(TAG, "displayNotification: toCarID "+String.valueOf(toCarID));
        Bitmap iconBitMap = null;
        if (iconUrl != null) {
            iconBitMap = getBitmapFromURL(iconUrl);
        }
        final int icon = R.mipmap.ic_launcher;

        PendingIntent resultPendingIntent;

        if (URL.equals(action)) {
            Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(destination));

            resultPendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
        } else if (ACTIVITY.equals(action) && activityMap.containsKey(destination)) {
            resultIntent = new Intent(mContext, activityMap.get(destination));

            resultPendingIntent =
                    PendingIntent.getActivity(
                            mContext,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_CANCEL_CURRENT
                    );
        } else {
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            resultPendingIntent =
                    PendingIntent.getActivity(
                            mContext,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_CANCEL_CURRENT
                    );
        }

        NotificationManager notificationManager = (NotificationManager)
                mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "channel",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder notificationBuilder;

        if(notificationType.equals("ADV")) {//TODO:IF YOU WANT NO ACTION ADD YOUR TYPE HERE
            notificationBuilder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                    .setColor(ContextCompat.getColor(mContext, R.color.colorPrimary))

                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(
                            message))
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setContentIntent(resultPendingIntent)


                    .setAutoCancel(true);

        }else if(notificationType.equals("SC-OFFER")){//TODO:IF YOU WANT ONE ACTION  ONLY ADD YOUR TYPE HERE
            notificationBuilder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                    .setColor(ContextCompat.getColor(mContext, R.color.colorPrimary))

                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(
                            message))
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setContentIntent(resultPendingIntent)

                    .addAction(acceptUserHelp(mContext, notificationType))

                    .setAutoCancel(true);
        }
        else if(notificationType.equals("CB")&&(!message.equals("Accepted and contact seller")))
        {
            Log.d(TAG, "CB OFFFFFFER");
            notificationBuilder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                    .setColor(ContextCompat.getColor(mContext, R.color.colorPrimary))

                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(
                            message))
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setContentIntent(resultPendingIntent)

                    .addAction(acceptUserHelp(mContext, notificationType))
                    .addAction(ignoreAction(mContext, notificationType))
                    .setAutoCancel(true);




        }
        else if((notificationType.equals("CB")&&message.equals("Accepted and contact seller")))
        {
            Log.d(TAG, "CB OFFFFFFER Accepted");
                notificationBuilder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                        .setColor(ContextCompat.getColor(mContext, R.color.colorPrimary))

                        .setContentTitle(title)
                        .setContentText(message)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(
                                message))
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true);


        }


        else{
            notificationBuilder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                    .setColor(ContextCompat.getColor(mContext, R.color.colorPrimary))

                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(
                            message))
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setContentIntent(resultPendingIntent)

                    .addAction(acceptUserHelp(mContext, notificationType))
                    .addAction(ignoreAction(mContext, notificationType))
                    .setAutoCancel(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(
                context,
                HELP_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }


}
