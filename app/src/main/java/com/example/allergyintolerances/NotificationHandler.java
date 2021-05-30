package com.example.allergyintolerances;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {

    private final static String CHANNEL_ID = "shop_notification_channel";
    private NotificationManager mManager;
    private Context mContext;
    private final int NOTIFICATION_ID = 0;
    public NotificationHandler(Context context){
        this.mContext=context;
        this.mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createChannel();
    }

    private void createChannel(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return;
        }
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Allergy notification", NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(Color.BLUE);
        channel.setDescription("Notification from Allergy");
        this.mManager.createNotificationChannel(channel);
    }

    public void send(String msg){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID).setContentTitle("Allergy App").setContentText(msg).setSmallIcon(R.drawable.ic_medical);
        this.mManager.notify(NOTIFICATION_ID, builder.build());


    }
}
