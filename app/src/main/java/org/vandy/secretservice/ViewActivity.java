package org.vandy.secretservice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.viven.imagezoom.ImageZoomHelper;

import java.util.Timer;
import java.util.TimerTask;

public class ViewActivity extends AppCompatActivity {

    TextView tv_firstLine, tv_secondLine;
    TextView tv_thirdLine, tv_fourthLine;
    Button btn_photo, btn_map, btn_exit;
    ImageView imageView;

    final String LOG_TAG = "myLogs";

    ImageZoomHelper imageZoomHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view);

        Log.d(LOG_TAG, "ViewActivity onCreate()");

        tv_firstLine = findViewById(R.id.tv_firstLine);
        tv_secondLine = findViewById(R.id.tv_secondLine);
        tv_thirdLine = findViewById(R.id.tv_thirdLine);
        tv_fourthLine = findViewById(R.id.tv_fourthLine);

        imageView = findViewById(R.id.imageView);

        btn_photo = findViewById(R.id.btn_photo);
        btn_map = findViewById(R.id.btn_map);
        btn_exit = findViewById(R.id.btn_exit);

        imageView.setVisibility(View.GONE);

        // Create an ImageZoomHelper instance
        imageZoomHelper = new ImageZoomHelper(this);

        // Display the name of the place of mission in the second line of text
        Cursor cursor = getContentResolver().
                query(Uri.parse("content://org.vandy.secretservice.datacontentprovider/data"),
                        null, null,
                        null, null);

        // move cursor of the DataContentProvider table to row 5 with mission data
        assert cursor != null;
        if(cursor.moveToFirst()) {
            cursor.moveToPosition(5);
            tv_secondLine.setText(cursor.getString(1));
            cursor.close();
        }
        else {
            Toast.makeText(this, "Fatal error!", Toast.LENGTH_SHORT).show();
            finish();
        }

        //Button VIEW PHOTO onClick listener
        btn_photo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {

                Log.d(LOG_TAG, "ViewActivity btn_photo");

                tv_firstLine.setVisibility(View.GONE);
                tv_secondLine.setVisibility(View.VISIBLE);
                tv_thirdLine.setVisibility(View.GONE);
                tv_fourthLine.setVisibility(View.GONE);
                btn_photo.setVisibility(View.GONE);
                btn_map.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);

                // get the URL address of the photo of mission place from
                // line number 5 of the content provider Data table
                Cursor cursor = getContentResolver().
                        query(Uri.parse("content://org.vandy.secretservice.datacontentprovider/data"),
                                null, null,
                                null, null);

                // check whether the data table is not empty
                assert cursor != null;
                if(cursor.moveToFirst()) {
                   cursor.moveToPosition(5);
                   String missionURL = cursor.getString(5);
                   cursor.close();

                    // make imageView 'zoomable'

                    // set zoomable tag on view that is to be zoomed
                    ImageZoomHelper.setViewZoomable(imageView);

                    imageZoomHelper.addOnZoomListener(new ImageZoomHelper.OnZoomListener() {
                        @Override
                        public void onImageZoomStarted(final View view) {

                        }

                        @Override
                        public void onImageZoomEnded(View view) {

                        }
                    });

                    //loading photo of the place of mission into the ImageView
                    //using Google recommended Glide library
                    Glide.with(getApplicationContext())
                            .load(missionURL)
//                            .centerCrop()
                            .placeholder(getDrawable(R.drawable.glasses))
                            .error(getDrawable(R.drawable.glasses))
                            .into(imageView);
                }
            }
        });

        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "ViewActivity btn_map");
                imageView.setVisibility(View.INVISIBLE);
                tv_secondLine.setVisibility(View.INVISIBLE);
                tv_firstLine.setVisibility(View.INVISIBLE);
                tv_thirdLine.setVisibility(View.INVISIBLE);
                tv_fourthLine.setVisibility(View.INVISIBLE);
                btn_map.setVisibility(View.INVISIBLE);
                btn_photo.setVisibility(View.INVISIBLE);
                btn_exit.setVisibility(View.INVISIBLE);

                // sending an explicit intent to start Map Activity
                if(ifMapIntentIsSent()){
                    sendMapIntent();
                }
            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "ViewActivity btn_exit");

                ConstraintLayout layout =
                        findViewById(R.id.layout_view_activity);
                layout.setBackgroundColor(Color.BLACK);

                btn_photo.setVisibility(View.INVISIBLE);
                btn_map.setVisibility(View.INVISIBLE);
                btn_exit.setVisibility(View.INVISIBLE);
                tv_firstLine.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.INVISIBLE);

                tv_secondLine.setText(R.string.good_luck_agent);
                tv_secondLine.setTextSize(23.0f);
                tv_secondLine.setVisibility(View.VISIBLE);
                tv_secondLine.setTextColor(Color.WHITE);

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

    // method producing and sending an explicit intent to start Map Activity
    protected void sendMapIntent(){
        Log.d(LOG_TAG, "ViewActivity sendMapIntent()");
        Intent mapIntent = new Intent(getApplicationContext(),
                MapActivity.class);
        startActivity(mapIntent);
    }

    //method for unit testing of class
    boolean ifMapIntentIsSent(){
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return imageZoomHelper.onDispatchTouchEvent(ev) ||
                super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(LOG_TAG, "ViewActivity onResume()");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d(LOG_TAG, "ViewActivity onPause()");
        finish();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(LOG_TAG, "ViewActivity onDestroy()");
    }
}
