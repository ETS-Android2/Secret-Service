package org.vandy.secretservice;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.vandy.secretservice.broadcastreceiver.ViewReceiver;
import org.vandy.secretservice.service.LoadService;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    TextView tv_firstLine, tv_secondLine;
    Button btn_no, btn_yes;

    ProgressBar progressBar;

    final String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        tv_firstLine = findViewById(R.id.tv_firstLine);
        tv_secondLine = findViewById(R.id.tv_secondLine);
        btn_no = findViewById(R.id.btn_no);
        btn_yes = findViewById(R.id.btn_yes);

        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);

        Log.d(LOG_TAG, "MainActivity onCreate()");

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_no.setVisibility(View.INVISIBLE);
                btn_yes.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                tv_firstLine.setText(R.string.wait);
                tv_secondLine.setText(R.string.loading_mission);

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // this code will be executed after 2 seconds

                        // send intent to Start Load Service
                        if(ifLoadServiceIntentIsSent()){
                            sendLoadServiceIntent();
                        }
                    }
                }, 2);
            }
        });

        // exit app
        btn_no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                ConstraintLayout layout =
                        findViewById(R.id.layout_main_activity);
                layout.setBackgroundColor(Color.BLACK);

                tv_firstLine.setText(R.string.good_luck);
                tv_firstLine.setTextSize(25.0f);
                tv_firstLine.setTextColor(Color.WHITE);

                tv_secondLine.setText(R.string.see_you);
                tv_secondLine.setTextSize(25.0f);
                tv_secondLine.setTextColor(Color.WHITE);

                btn_no.setVisibility(View.INVISIBLE);
                btn_yes.setVisibility(View.INVISIBLE);

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // this code will be executed after 2 seconds
                        finish();
                    }
                }, 2000);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "MainActivity onResume()");

        // register Broadcast Receiver
        ViewReceiver viewReceiver = new ViewReceiver();
        IntentFilter filter = new IntentFilter("Load completed");
        registerReceiver(viewReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "MainActivity onPause()");
        // destroy activity
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "MainActivity onStop()");
        // destroy activity
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "MainActivity onDestroy()");
    }

    // send intent to Start Load Service
    private void sendLoadServiceIntent(){
        Log.d(LOG_TAG, "MainActivity sendLoadServiceIntent()");
        // an intent to Start Load Service
        Intent loadServiceIntent =
                new Intent(MainActivity.this,
                        LoadService.class);
        startService(loadServiceIntent);
    }

    //method for unit testing of class
    boolean ifLoadServiceIntentIsSent(){
        return true;
    }
}


