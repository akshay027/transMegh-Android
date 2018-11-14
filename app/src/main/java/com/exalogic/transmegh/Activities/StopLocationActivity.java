package com.exalogic.transmegh.Activities;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.LinearInterpolator;

import com.exalogic.transmegh.MapUtility.LatLngInterpolator;
import com.exalogic.transmegh.Utility.Constants;
import com.exalogic.transmegh.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class StopLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double lat = 0, lng = 0;
    private String busStopName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_location);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        try {

            Intent intent = getIntent();
            lat = Double.parseDouble(intent.getStringExtra(Constants.LAT));
            lng = Double.parseDouble(intent.getStringExtra(Constants.LNG));
            busStopName = intent.getStringExtra(Constants.CITY);

            Log.e("LatLong", "Lat -- " + lat + "    Long  : " + lng);

        } catch (Exception e) {
            e.printStackTrace();
        }

//        lat = 12.908136;
//        lng = 77.647608;
//        busStopName = "Hsr Layout";

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(busStopName);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latLng = new LatLng(lat, lng);
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(busStopName));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));

//        Location location = new Location("pune");
//        location.setLatitude(12.911978);
//        location.setLatitude(77.644168);
//        location.setBearing(230);
//
//
//        animateMarker(location, marker);


    }


    public static final void animateMarker(final Location destination, final Marker marker) {
        if (marker != null) {
            final LatLng startPosition = marker.getPosition();
//            final LatLng endPosition = new LatLng(destination.getLatitude(), destination.getLongitude());
            final LatLng endPosition = new LatLng(12.911978, 77.644168);
            marker.setRotation(destination.getBearing());

            final float startRotation = marker.getRotation();

            final LatLngInterpolator latLngInterpolator = new LatLngInterpolator.LinearFixed();
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(3000); // duration 1 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            marker.setAnchor(0.5f, 0.5f);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                        marker.setPosition(newPosition);
//                        marker.setRotation(computeRotation(v, startRotation, 180));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        // I don't care atm..
                    }
                }
            });
            valueAnimator.start();
        }
    }


    private static float computeRotation(float fraction, float start, float end) {
        float normalizeEnd = end - start; // rotate start to 0
        float normalizedEndAbs = (normalizeEnd + 360) % 360;

        float direction = (normalizedEndAbs > 180) ? -1 : 1; // -1 = anticlockwise, 1 = clockwise
        float rotation;
        if (direction > 0) {
            rotation = normalizedEndAbs;
        } else {
            rotation = normalizedEndAbs - 360;
        }

        float result = fraction * rotation + start;
        return (result + 360) % 360;
    }

}
