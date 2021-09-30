package org.vandy.secretservice.loginDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LoginDBHelper extends SQLiteOpenHelper {

    final String LOG_TAG = "myLogs";

    public  LoginDBHelper(Context context) {
        // superclass constructor
        super(context, "myDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");

        // create login table
        db.execSQL("create table logintable ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "password text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //we are not going to upgrade DB
    }
}