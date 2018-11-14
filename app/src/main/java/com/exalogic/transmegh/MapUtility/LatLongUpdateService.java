package com.exalogic.transmegh.MapUtility;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.exalogic.transmegh.API.RetrofitAPI;
import com.exalogic.transmegh.Activities.SplashScreenActivity;
import com.exalogic.transmegh.Database.PreferencesManger;
import com.exalogic.transmegh.R;
import com.exalogic.transmegh.Utility.Constants;
import com.exalogic.transmegh.Utility.UIUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LatLongUpdateService extends Service implements LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final int ONGOING_NOTIFICATION_ID = 122211;
    private static final int TRIP_HISTORY_INTERVAL = 1000 * 2;
    private Notification notification;
    private final static int TRIP_LOCATION_INTERVAL = 1000 * 2;//1 second
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private String currentStopLatitude, currentStopLongitude;
    private boolean tripHistory;
    private String spp;


    public LatLongUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (UIUtil.isInternetAvailable(this)) {
            showNotification();

            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
                startRepeatingTask();
            }


            try {
                String s = intent.getStringExtra(Constants.CITY);
                ;
                Log.e("trip_status", "trip_status -- " + s + "\n lat = " + currentStopLatitude + "\n lont = " + currentStopLongitude);
                if (s.equalsIgnoreCase("STOP")) {
                    stopSelf();
                    onDestroy();
                    stopLocationUpdates();
                    stopRepeatingTask();
                    if (mGoogleApiClient.isConnected())
                        mGoogleApiClient.disconnect();
                } else {

                    if (!mGoogleApiClient.isConnected())
                        mGoogleApiClient.connect();

                }
                currentStopLatitude = intent.getStringExtra(Constants.LAT);
                currentStopLongitude = intent.getStringExtra(Constants.LNG);
            } catch (Exception e) {
                e.printStackTrace();
                stopSelf();
                stopRepeatingTask();
                stopLocationUpdates();
                if (mGoogleApiClient.isConnected())
                    mGoogleApiClient.disconnect();
            }


        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onLocationChanged(Location location) {
        if (PreferencesManger.getBooleanFields(getApplicationContext(), Constants.Pref.KEY_IS_TRIP_START))
            spp = PreferencesManger.getStringFields(getApplicationContext(), Constants.Pref.SPEE);
        updateLotLong(location);

//        if (distance(location.getLatitude(), location.getLongitude(), Double.parseDouble(currentStopLatitude), Double.parseDouble(currentStopLongitude)) < 0.1) { // if distance < 0.1 miles we take locations as equal
//            System.out.println(" In If");
//        } else {
//            System.out.println(" In else");
//        }
        Log.e("location", "location --- " + location.toString());
    }


    private void showNotification() {
        try {
            Intent notificationIntent = new Intent(this, SplashScreenActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

            notification = new NotificationCompat.Builder(this)
                    .setContentTitle("TransMegh")
                    .setTicker("TransMegh")
                    .setContentText("Your Trip is Running..")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .setContentIntent(pendingIntent)
                    .setOngoing(true).build();
            startForeground(ONGOING_NOTIFICATION_ID, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {

            startLocationUpdates();
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    protected void createLocationRequest(int interval) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(interval);
        mLocationRequest.setFastestInterval(interval);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        try {
            Log.e("Location", "startLocationUpdates ----- startLocationUpdates");
            createLocationRequest(TRIP_LOCATION_INTERVAL);
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected void stopLocationUpdates() {
        try {
            if (mGoogleApiClient.isConnected())
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
        stopRepeatingTask();
    }


    Handler mHandler = new Handler();

    Runnable mHandlerTask = new Runnable() {
        @Override
        public void run() {

            mHandler.postDelayed(mHandlerTask, TRIP_HISTORY_INTERVAL);
            Log.e("trip", "History_time");
            tripHistory = true;
        }
    };

    void startRepeatingTask() {
        mHandlerTask.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mHandlerTask);
    }


    private void updateLotLong(Location location) {
        try {
            JsonObject object = new JsonObject();

            object.addProperty("latitude", location.getLatitude());
            object.addProperty("longitude", location.getLongitude());
            object.addProperty("speed", spp);
            Log.e("hwdbghasdbhdbnmmmmmm :", spp + "");
            object.addProperty("bearing", location.getBearing());

            object.addProperty("trip_id", PreferencesManger.getIntFields(getApplicationContext(), Constants.Pref.KEY_TRIP_ID));
            object.addProperty("trip_history", tripHistory);
            object.addProperty("branch_id", PreferencesManger.getStringFields(getApplicationContext(), Constants.Pref.KEY_BRANCH_ID));
            Log.e("update Lat", "JSON -- " + object.toString());

            RetrofitAPI.getInstance(this).getApi().updateTripLatLong(object, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    tripHistory = false;
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
