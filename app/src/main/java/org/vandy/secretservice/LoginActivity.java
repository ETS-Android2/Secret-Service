package org.vandy.secretservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.vandy.secretservice.datacontentprovider.DataContentProvider;
import org.vandy.secretservice.loginDB.LoginDBHelper;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity
            implements View.OnClickListener {
    EditText et_name, et_password;
    Button btn_signup, btn_login;
    TextView tv_forgot;

    final String LOG_TAG = "myLogs";

    String name, password, dbName, dbPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Log.d(LOG_TAG, "LoginActivity onCreate");

        et_name = findViewById(R.id.et_name);
        et_password = findViewById(R.id.et_password);
        btn_signup = findViewById(R.id.btn_signup);
        btn_login = findViewById(R.id.btn_login);
        tv_forgot = findViewById(R.id.tv_forgot);

        btn_signup.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_forgot.setOnClickListener(this);

        // new instance of Login Data Base
        LoginDBHelper loginDBHelper = new LoginDBHelper(getApplicationContext());

        // getting access to LoginDB in 'read' mode
        SQLiteDatabase db = loginDBHelper.getReadableDatabase();

        Cursor c = db.query("loginTable", null,
                null, null, null,
                null, null);

        // set LoginActivity UI
        if (c.moveToFirst()){
            // if LoginDB is not empty - show LOGIN button
            btn_login.setVisibility(View.VISIBLE);
            btn_signup.setVisibility(View.INVISIBLE);
            tv_forgot.setVisibility(View.VISIBLE);
        }
        else {
            // in case LoginDB is empty (user enters app for the first time) -
            // show SIGN UP button
            btn_login.setVisibility(View.INVISIBLE);
            btn_signup.setVisibility(View.VISIBLE);
            tv_forgot.setVisibility(View.INVISIBLE);
        }

        // set portrait orientation of activity
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //free up cursor after use
        c.close();
    }

        // onClick listeners
        @Override
        public void onClick(View v) {

            // new object for login data
            ContentValues cv = new ContentValues();

            // get data from input fields
//            String name = et_name.getText().toString();
//            String password = et_password.getText().toString();

            name = et_name.getText().toString();
            password = et_password.getText().toString();

            // connect to loginDB in 'write' mode
            LoginDBHelper dbHelper = new LoginDBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            switch (v.getId()) {
                // button SIGN UP is pressed
                case R.id.btn_signup:
                    // check name and password for non-zero length
                    if (checkNonZeroLengthOfNameAndPassword ()){

                        // prepare data to insert into login table:
                        // column - value pairs
                        cv.put("name", name);
                        cv.put("password", password);

                        // insert name and password into login table
                        long rowID = db.insert("logintable",
                                null, cv);
                        Log.d(LOG_TAG, "row inserted, ID = " + rowID);

                        //relaunch this activity to enter 'login' mode
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(this, "Invalid name or password!",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;

                // button LOGIN pressed
                case R.id.btn_login:
           password = et_password.getText().toString();

                    //get login table cursor
                    Cursor c = db.query("logintable", null,
                            null, null, null,
                            null, null);

                    // get data from login table
                    c.moveToFirst();
                    dbName = c.getString(1);
                    dbPassword = c.getString(2);

                    // check name and password for non-zero length
                    if (checkNonZeroLengthOfNameAndPassword()){

                        // check name and password
                        if (checkCorrectNameAndPassword()) {

                            // load data of five different missions into
                            // DataContentProvider Data table
                            writeFiveMissionsData ();
                        }
                        else {
                            // case name or(and) password is not correct
                            Toast.makeText(this, R.string.wrong_name,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(this, R.string.invalid_name,
                                Toast.LENGTH_SHORT).show();
                    }
                    // closing Login table cursor
                    c.close();
                    break;

                    //touching 'forgot password' text
                    case R.id.tv_forgot:
                    Toast.makeText(this, "The real agent should remember " +
                                    "his password!",
                            Toast.LENGTH_SHORT).show();

                    //shutting down an app
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // this code will be executed after 2 seconds
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    }, 2000);
                    break;
             }
            // Close connection to loginDB
            dbHelper.close();
        }

        // load data of five different missions into
        // DataContentProvider Data table
        private void writeFiveMissionsData (){

            Cursor cursor = getContentResolver().
                    query(Uri.parse("content://org.vandy.secretservice.datacontentprovider/data"),
                            null, null,
                            null, null);
            // if data table in content provide is empty (user enters app first time) -
            // entering data into data table for five missions
            assert cursor != null;
            if(!cursor.moveToFirst()){
                DataContentProvider dcp = new DataContentProvider();
                // class to add values in the database
                ContentValues values = new ContentValues();
                values.put(dcp.NAME, "The White House");
                values.put(dcp.LAT, 38.8977);
                values.put(dcp.LNG, -77.0365);
                values.put(dcp.ADR, "1600 Pennsylvania Avenue NW Washington, D.C.");
                values.put(dcp.URLaddress,
                        "https://upload.wikimedia.org/wikipedia/commons/4/4d/White_House%2C_Blue_Sky.jpg");
                //inserting into database through content URI
                getContentResolver().insert(dcp.CONTENT_URI, values);

                values.put(dcp.NAME, "The Moscow Kremlin");
                values.put(dcp.LAT, 55.751667);
                values.put(dcp.LNG, 37.617778);
                values.put(dcp.ADR, "Moscow Kremlin, Moscow, Russian Federation");
                values.put(dcp.URLaddress,
                        "https://upload.wikimedia.org/wikipedia/commons/f/f5/Moscow_05-2012_Kremlin_22.jpg");
                // inserting into database through content URI
                getContentResolver().insert(dcp.CONTENT_URI, values);

                values.put(dcp.NAME, "The 10 Downing Street");
                values.put(dcp.LAT, 51.5033);
                values.put(dcp.LNG, -0.1275);
                values.put(dcp.ADR, "10, Downing Street, City of Westminster, London");
                values.put(dcp.URLaddress,
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/1/13/10_Downing_Street._MOD_45155532.jpg/1920px-10_Downing_Street._MOD_45155532.jpg");
                // inserting into database through content URI
                getContentResolver().insert(dcp.CONTENT_URI, values);

                values.put(dcp.NAME, "The Government House, Wellington");
                values.put(dcp.LAT, -41.306114);
                values.put(dcp.LNG, 174.781084);
                values.put(dcp.ADR, "Wellington Mail Centre, Lower Hutt 5045");
                values.put(dcp.URLaddress,
                        "https://upload.wikimedia.org/wikipedia/commons/b/b4/Government_House_front.jpg");
                // inserting into database through content URI
                getContentResolver().insert(dcp.CONTENT_URI, values);

                values.put(dcp.NAME, "The State House of Namibia");
                values.put(dcp.LAT, -22.5913);
                values.put(dcp.LNG, 17.1011);
                values.put(dcp.ADR, "Windhoek, Namibia");
                values.put(dcp.URLaddress,
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/9/97/Laika_ac_New_State_House_%288406704947%29.jpg/1280px-Laika_ac_New_State_House_%288406704947%29.jpg");
                // inserting into database through content URI
                getContentResolver().insert(dcp.CONTENT_URI, values);

                // displaying a toast message
                Toast.makeText(getBaseContext(), "New Record Inserted",
                        Toast.LENGTH_LONG).show();
            }


            // check whether the data table has line number 5 -
            // which contains the random mission data received
            // earlier

            if(cursor.moveToPosition(5)){
                // an explicit intent to start ViewActivity to view photo
                // or map
                Intent viewIntent =
                        new Intent(this, ViewActivity.class);
                startActivity(viewIntent);
                finish();
            }
            else {
                //an explicit intent to start  MainActivity
                Intent mainIntent =
                        new Intent(this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
            // closing DataTable cursor
            cursor.close();
        }

    @Override
    protected void onResume (){
            super.onResume();
            Log.d(LOG_TAG, "LoginActivity onResume()");
    }

    @Override
    protected void onPause (){
        super.onPause();
        Log.d(LOG_TAG, "LoginActivity onPause()");
    }

    @Override
    protected void onDestroy (){
        super.onDestroy();
        Log.d(LOG_TAG, "LoginActivity onDestroy()");
    }

    // check whether the length of the name and password is not zero
    protected boolean checkNonZeroLengthOfNameAndPassword (){
        return name.length() > 0 && password.length() > 0;
    }

    // check whether the name and password matches those in the loginTable
    protected boolean checkCorrectNameAndPassword (){
        return name.equals(dbName) && password.equals(dbPassword);
    }
}




