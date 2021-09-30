package org.vandy.secretservice.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.vandy.secretservice.ViewActivity;

public class ViewReceiver extends BroadcastReceiver {

    final String LOG_TAG = "myLogs";

    @Override
    public void onReceive(Context context, Intent intent) {

        // This method is called when the BroadcastReceiver is receiving
        // a broadcast.

        Log.d(LOG_TAG, "ViewReceiver onReceive()");

        Intent viewIntent = new Intent(context, ViewActivity.class);
        context.startActivity(viewIntent);
    }
}
