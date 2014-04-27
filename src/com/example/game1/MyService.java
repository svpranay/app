package com.example.game1;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    
    private String getState() {    	
    	SharedPreferences prefs = this.getSharedPreferences("com.example.game1", Context.MODE_PRIVATE);
    	String vohKey = "com.example.game1.vohItemId";
    	String vodKey = "com.example.game1.vodItemId";
    	String vohItemId = prefs.getString(vohKey, "");
    	String vodItemId = prefs.getString(vodKey, "");
    	Log.d("Verify items", "Items in database " + vohItemId + " " + vodItemId);
    	return vohItemId + ":" + vodItemId;
    }    
    
    private void sendNotification(Context context) {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        NotificationManager notificationMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification =  new Notification(android.R.drawable.star_on, "Refresh", System.currentTimeMillis());
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.setLatestEventInfo(context, getState(), "Content", contentIntent);
        notificationMgr.notify(0, notification);
     }
}