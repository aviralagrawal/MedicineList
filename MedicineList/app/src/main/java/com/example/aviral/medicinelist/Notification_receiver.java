package com.example.aviral.medicinelist;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class Notification_receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent repeating_intent = new Intent(context,Repeating_activity.class);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String title = intent.getExtras().getString("data");
        String keyofitem = intent.getExtras().getString("keyofitem");
        repeating_intent.putExtra("key",keyofitem);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        int id=intent.getExtras().getInt("id");

        CharSequence name="Ragav";
        String desc="this is notific";
        int imp=NotificationManager.IMPORTANCE_HIGH;
        final String ChannelID="my_channel_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(ChannelID, name,
                    imp);
            mChannel.setDescription(desc);
            mChannel.setLightColor(Color.CYAN);
            mChannel.canShowBadge();
            mChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(mChannel);
        }


        PendingIntent pendingIntent = PendingIntent.getActivity(context,id,repeating_intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,ChannelID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentTitle(title)
                .setContentText("It is time to take your meds")
                .setSound(alarmSound)
                .setSmallIcon(R.mipmap.ic_launcher )
                .setAutoCancel(true);
        notificationManager.notify(id,builder.build());
    }
}
