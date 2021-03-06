package com.happywannyan.PushNotification;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.happywannyan.Activities.BaseActivity;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.R;
import com.happywannyan.Utils.Loger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

//import com.firebase.jobdispatcher.FirebaseJobDispatcher;
//import com.firebase.jobdispatcher.GooglePlayDriver;
//import com.firebase.jobdispatcher.Job;

/**
 * Created by apple on 23/05/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        // [START_EXCLUDE]
//        // There are two types of messages data messages and notification messages. Data messages are handled
//        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
//        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
//        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
//        // When the user taps on the notification they are returned to the app. Messages containing both notification
//        // and data payloads are treated as notification messages. The Firebase console always sends notification
//        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
//        // [END_EXCLUDE]
//
//        // TODO(developer): Handle FCM messages here.
//        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//
//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//
//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }
//
//        }
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }
//
//        // Also if you intend on generating your own notifications as a result of a received FCM
//        // message, here is where that should be initiated. See sendNotification method below.

        Loger.MSG("@@@ PUSH", remoteMessage.getData().toString());
        try {
            JSONObject Object = new JSONObject(remoteMessage.getData().get("body"));

            SharedPreferences pref = getApplicationContext().getSharedPreferences("unread_msg_count", MODE_PRIVATE); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();

            editor.putInt("count", pref.getInt("count", 0) + 1);
            editor.apply();
            editor.commit();

            Loger.MSG("after_push_msg_count", "" + pref.getInt("count", 0));


            Log.d("PushResponse", "==" + Object.toString());
            if (Object.getString("type_notification").equals("message")) {
                Intent intent = new Intent("CONNECT_MESSAGE_LIVE");
                intent.putExtra("MSG_DATA", Object.toString());
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            } else {
                sendNotification(Object.getJSONObject("message_info"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (applicationInForeground()) {
            Loger.MSG("fore ground-->", "" + applicationInForeground());
        } else {
            Loger.MSG("fore ground-->", "" + applicationInForeground());
        }

        try {
            JSONObject Object = new JSONObject(remoteMessage.getData().get("body"));
            sendNotification(Object.getJSONObject("message_info"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    // [END receive_message]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob() {
        // [START dispatch_job]
//        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
//        Job myJob = dispatcher.newJobBuilder()
//                .setService(MyJobService.class)
//                .setTag("my-job-tag")
//                .build();
//        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(JSONObject messageBody) {

        try {
            Intent intent = new Intent(this, BaseActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            AppConstant.go_to="message_all";
            AppConstant.message_object_string=messageBody.toString();
            AppConstant.messageAndBookingConditionCheck=true;
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT
//                0
            );


            Bitmap icon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.logo_happywan);

            //Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//            if(defaultSoundUri == null){
//                defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//                if(defaultSoundUri == null){
//                    defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                }
//            }

            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            bigText.bigText(messageBody.getString("message_info"));
            bigText.setBigContentTitle(messageBody.getString("usersname"));


            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setLargeIcon(icon)
                    .setSmallIcon(R.drawable.logo_happywan)
                    .setContentTitle(messageBody.getString("usersname"))
                    .setContentText(messageBody.getString("message_info"))
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)

                    .setStyle(bigText)

//                     //Vibration
//                    .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
//
//                    //LED
//                    .setLights(Color.GREEN, 3000, 3000)

                     //Ton
                    //.setSound(Uri.parse("uri://sadfasdfasdf.mp3"))
                    //.setSound(defaultSoundUri)

                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean applicationInForeground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> services = activityManager.getRunningAppProcesses();
        boolean isActivityFound = false;

        if (services.get(0).processName.equalsIgnoreCase(getPackageName())) {
            isActivityFound = true;
        }
        return isActivityFound;
    }
}
