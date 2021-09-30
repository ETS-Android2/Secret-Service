package org.vandy.secretservice;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    final String LOG_TAG = "myLogs";

    Button btn_back1, btn_exit1;

    float lat;
    float lng;
    String title;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the content view that renders the map.
        setContentView(R.layout.map);

        Log.d(LOG_TAG, "MapActivity onCreate()");

        btn_back1 = findViewById(R.id.btn_back1);
        btn_exit1 = findViewById(R.id.btn_exit1);

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        final SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        btn_back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ifViewIntentIsSent()){
                    sendViewIntent();
                }
                finish();
            }
        });

        btn_exit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConstraintLayout layout1 = (ConstraintLayout)
                        findViewById(R.id.layout1);
                layout1.setBackgroundColor(Color.BLACK);
                
                btn_exit1.setVisibility(View.INVISIBLE);
                btn_back1.setVisibility(View.INVISIBLE);

                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

       getMissionGeoCoordinates();

        //LatLng sydney = new LatLng(-33.852, 151.211);
        LatLng mission = new LatLng(lat, lng);

        // Add a marker in place of mission,
        // and move the map's camera to the same location.

        googleMap.addMarker(new MarkerOptions()
                .position(mission)
                .title(title)
                .snippet("This is your mission, Agent!"));
        // [START_EXCLUDE silent]
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(mission));
        // place ZOOM buttons
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        // ZOOM level
        googleMap.animateCamera( CameraUpdateFactory.zoomTo( 15 ) );
    }

    private void getMissionGeoCoordinates(){

        //Display the name of the place of mission in the second line of text
        Cursor cursor = getContentResolver().
                query(Uri.parse("content://org.vandy.secretservice.datacontentprovider/data"),
                        null, null,
                        null, null);

        // iteration of the cursor to print whole table
        assert cursor != null;
        if(cursor.moveToFirst()) {
            cursor.moveToPosition(5);
            lat = cursor.getFloat(2);
            lng = cursor.getFloat(3);
            title = cursor.getString(1);
            cursor.close();
        }
        else {
            Toast.makeText(this, "Fatal error!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    protected void sendViewIntent(){
        Intent newViewActivityIntent =
                new Intent(getApplicationContext(), ViewActivity.class);
        startActivity(newViewActivityIntent);
    }

    protected boolean ifViewIntentIsSent(){
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "MapActivity onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "MapActivity onPause()");
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "MapActivity onDestroy()");
    }
}
