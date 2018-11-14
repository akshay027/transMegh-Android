package com.exalogic.transmegh.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.exalogic.transmegh.Activities.StopListActivity;
import com.exalogic.transmegh.Database.PreferencesManger;
import com.exalogic.transmegh.Models.database.Chat;
import com.exalogic.transmegh.Utility.Constants;
import com.exalogic.transmegh.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.orm.SugarContext;

import java.util.Map;

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
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Log.e(TAG, "From: " + remoteMessage.toString());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
            try {
                SugarContext.init(getApplicationContext());
                Chat chat = new Chat();
                Map<String, String> stringStringMap = remoteMessage.getData();
                chat.setMessage(stringStringMap.get("body"));
                chat.setMessageId(Integer.parseInt(stringStringMap.get("message_id")));
                chat.setDate(stringStringMap.get("date"));
                chat.setTime(stringStringMap.get("time"));
                chat.setUserId(Integer.parseInt(stringStringMap.get("send_by")));
                chat.setType(1);
                chat.save();
                if (PreferencesManger.getBooleanFields(getApplicationContext(), Constants.Pref.KEY_CHAT_OPEN)) {
                    sendMessage(chat.getMessageId());
                } else {
                    sendNotification(stringStringMap.get("name"), chat.getMessage(), chat.getMessageId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
//        sendNotification(remoteMessage.getNotification().getBody());
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String name, String messageBody, int id) {
        Intent intent = new Intent(this, StopListActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("name", name);
        intent.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_add_alert_black_24dp)
                .setContentTitle(name)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
    }

    private void sendMessage(int msg) {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("chat");
        intent.putExtra("message", msg);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }
}
