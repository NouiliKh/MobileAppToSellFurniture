package com.marketingservice.gomni.furnituremarketingservice.activities;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.marketingservice.gomni.furnituremarketingservice.R;
import com.marketingservice.gomni.furnituremarketingservice.sql.SqliteHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Broadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        switch (intent.getAction()) {

            case Intent.ACTION_BOOT_COMPLETED:
                HashMap<String, String> history = new SqliteHelper(context).getDBHistory();

                SqliteHelper sql = new SqliteHelper(context);
                String message = history.get("LAST_READ") + " " + history.get("LAST_UPDATE") + " " + history.get("LAST_DELETE");
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "DEFAULT")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("Database Access History").setContentText(message)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(message))
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setDefaults(Notification.DEFAULT_ALL);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(1, builder.build());
                break;

            case Intent.ACTION_TIME_CHANGED:
            case Intent.ACTION_TIMEZONE_CHANGED:
                HashMap<String, String> history1 = new SqliteHelper(context).getDBHistory();

                Date lastRead = new Date();
                Date lastUpdate = new Date();
                Date lastDelete = new Date();
                Date maxDate;
                Date currentTime = new Date();


                try {
                    lastRead = new SimpleDateFormat("dd-MM-yy:HH:mm", Locale.getDefault()).parse(history1.get("LAST_READ"));
                    lastUpdate = new SimpleDateFormat("dd-MM-yy:HH:mm", Locale.getDefault()).parse(history1.get("LAST_UPDATE"));
                    lastDelete = new SimpleDateFormat("dd-MM-yy:HH:mm", Locale.getDefault()).parse(history1.get("LAST_DELETE"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                maxDate = lastDelete.after(lastRead) ? lastDelete : lastRead;
                maxDate = maxDate.after(lastUpdate) ? maxDate : lastUpdate;

                if (maxDate.after(currentTime)) {
                    String notifMessage = "System Time has changed prior to last time the database was accessed";

                    NotificationCompat.Builder builder2 = new NotificationCompat.Builder(context, "Time")
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle("Warning").setContentText(notifMessage)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(notifMessage))
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setDefaults(Notification.DEFAULT_ALL);

                    NotificationManagerCompat notificationManager2 = NotificationManagerCompat.from(context);

                    notificationManager2.notify(1, builder2.build());
                }

                break;
            default:
                break;

        }


    }
}
