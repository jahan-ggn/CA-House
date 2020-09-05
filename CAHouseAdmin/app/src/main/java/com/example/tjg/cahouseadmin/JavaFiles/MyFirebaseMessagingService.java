package com.example.tjg.cahouseadmin.JavaFiles;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.example.tjg.cahouseadmin.Activities.FetchDataActivity;
import com.example.tjg.cahouseadmin.Activities.RequestActivity;
import com.example.tjg.cahouseadmin.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService{

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        try {
            sendnotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
        catch (Exception e){
            sendnotification(remoteMessage.getData().get("title").toString(), remoteMessage.getData().get("body").toString());
        }
    }

    public void sendnotification(String title, String messageBody){
        Intent intent = null;
        if(messageBody.equals("Request For Documents")){
            intent = new Intent(this, RequestActivity.class);
        }
        else if(messageBody.equals("Documents Uploaded")){
            intent = new Intent(this,FetchDataActivity.class);
        }
        intent.putExtra("key", messageBody);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
    }
}
