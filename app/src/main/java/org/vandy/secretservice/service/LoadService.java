package org.vandy.secretservice.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.vandy.secretservice.datacontentprovider.DataContentProvider;

import java.util.Timer;
import java.util.TimerTask;

public class LoadService extends Service {

    int randomMissionLineNumber;
    public boolean isStarted;

    final String LOG_TAG = "myLogs";

    public LoadService() {
    }

    public void onCreate() {
        super.onCreate();
        isStarted = true;
        Log.d(LOG_TAG, "LoadService onCreate()");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(LOG_TAG, "LoadService onStartCommand()");

        getRandomMissionData();

        sendBroadcastIntent();

//        return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "LoadService onDestroy()");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TO DO: Return the communication channel to the service.
     //   throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    void sendBroadcastIntent() {
        // Send Intent to launch Broadcast Receiver

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // this code will be executed after 2 seconds
                Log.d(LOG_TAG, "sendBroadcastIntent() + delay 2 sec");
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("Load completed");
                sendBroadcast(broadcastIntent);
            }
        }, 2000);
    }

    public void getRandomMissionData(){
        String missionName;
        float missionLat;
        float missionLng;
        String missionAddress;
        String missionURLAddress;

        //get random number (from 0 to 4)
        randomMissionLineNumber = (int) (Math.random() * 4);

        Log.d(LOG_TAG, "randomMissionLineNumber " + randomMissionLineNumber);

        // get mission data from content provider according
        // random line number of data table
        Cursor cursor = getContentResolver().
                query(Uri.parse("content://org.vandy.secretservice.datacontentprovider/data"),
                        null, null,
                        null, null);

        assert cursor != null;
        if(cursor.moveToFirst()) {
            cursor.moveToPosition(randomMissionLineNumber);
            missionName = cursor.getString(1);
            missionLat = cursor.getFloat(2);
            missionLng = cursor.getFloat(3);
            missionAddress = cursor.getString(4);
            missionURLAddress = cursor.getString(5);

            // store random mission data in line 5 of data table
            DataContentProvider dcp = new DataContentProvider();
            cursor.moveToPosition(5);
            // class to add values in the database
            ContentValues values = new ContentValues();
            values.put(dcp.NAME, missionName);
            values.put(dcp.LAT, missionLat);
            values.put(dcp.LNG, missionLng);
            values.put(dcp.ADR, missionAddress);
            values.put(dcp.URLaddress, missionURLAddress);
            //inserting into database through content URI
            getContentResolver().insert(dcp.CONTENT_URI, values);
            cursor.close();
        }
        else {
            Toast.makeText(this, "Fatal error!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isStarted() {
        return true;
    }
}
