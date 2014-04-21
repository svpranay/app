package com.example.game1;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class MyService extends IntentService {
    public MyService() {
       super("MyServiceName");
    }
    
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("MyService", "About to execute MyTask");
        new MyTask().execute();
        this.sendNotification(this);
    }
    private class MyTask extends AsyncTask<String, Void, Boolean> {
        @Override
         protected Boolean doInBackground(String... strings) {
                Log.d("MyService - MyTask", "Calling doInBackground within MyTask");
               return false;
        }
    }        
    
    private void sendNotification(Context context) {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        NotificationManager notificationMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification =  new Notification(android.R.drawable.star_on, "Refresh", System.currentTimeMillis());
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.setLatestEventInfo(context, "Title","Content", contentIntent);
        notificationMgr.notify(0, notification);
     }
}