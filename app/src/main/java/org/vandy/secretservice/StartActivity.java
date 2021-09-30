package org.vandy.secretservice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        Log.d(LOG_TAG, "StartActivity onCreate()");

        if (ifLoginIntentIsSent()){
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    // this code will be executed after 2 seconds

                    // send an explicit intent to launch LoginActivity
                    sendLoginIntent();
                }
            }, 2000);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(LOG_TAG, "StartActivity onResume()");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d(LOG_TAG, "StartActivity onPause()");
        // destroy this activity after launching the next one
        finish();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(LOG_TAG, "StartActivity onDestroy()");
    }

    // method to send an explicit intent to start Login Activity
    protected void sendLoginIntent(){
        Log.d(LOG_TAG, "StartActivity sendLoginIntent()");
        Intent loginIntent =
                new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginIntent);
    }

    //method for unit testing of class
    boolean ifLoginIntentIsSent(){
        return true;
    }
}

