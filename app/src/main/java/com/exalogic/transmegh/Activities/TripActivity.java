package com.exalogic.transmegh.Activities;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.exalogic.transmegh.API.RetrofitAPI;
import com.exalogic.transmegh.Database.DBHelper;
import com.exalogic.transmegh.Database.PreferencesManger;
import com.exalogic.transmegh.MapUtility.LatLngInterpolator;
import com.exalogic.transmegh.MapUtility.LatLongUpdateService;
import com.exalogic.transmegh.MapUtility.MapUtility;
import com.exalogic.transmegh.Models.database.BusStop;
import com.exalogic.transmegh.Models.database.MessageTemplate;
import com.exalogic.transmegh.Models.database.Student;
import com.exalogic.transmegh.Utility.Constants;
import com.exalogic.transmegh.Utility.UIUtil;
import com.exalogic.transmegh.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonObject;
import com.orm.SugarContext;
import com.splunk.mint.Mint;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TripActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {
    private GoogleMap mMap;

    private int shiftType = 0, currentSqu = 0, stopnow = 0;
    private ArrayList<BusStop> busStopArrayList;
    private String bus;
    private DBHelper dbHelper;
    private TextView tvNextStop, btnTripStatus, tvNavTo;
    private LinearLayout llNav;
    private ImageView ivNav, ivatt, ivstoptri, mess;
    private BusStop currentStop, nextStop, previousStop;
    private boolean isMorningShift, isTripRun = false, isLastStop, startTrip, mPermissionDenied = false, tripHistory = true, polyLineVisible, speedLimit;
    private boolean alterNetSpeedCheck = true, firstTime = true, autoUpdate = false;
    private double latitude, longitude;
    private double lat, longi;
    private String nextTopTime, address, curr;
    private Marker marker;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation, location;
    private final static int LOCATION_INTERVAL = 1000 * 2;
    private Dialog dialog;
    private double speed;
    String busno;
    private int tripId;
    private Date lastDate, currentDate;
    private boolean isMarkerRotating;
    private int busTripId = -1;
    private final int MAX_SPEED_LIMIT = 55;
    private final int MAX_DIST_FOR_AUTO_UPDATE = 5;
    String busid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        SugarContext.init(this);

        Mint.initAndStartSession(TripActivity.this, "9d9097da");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mess = (ImageView) findViewById(R.id.mess);
        btnTripStatus = (TextView) findViewById(R.id.btnTripStatus);
        tvNextStop = (TextView) findViewById(R.id.tvNextStop);
        tvNavTo = (TextView) findViewById(R.id.tvNavTo);
       /* llNav = (LinearLayout) findViewById(R.id.llNav);*/
        ivNav = (ImageView) findViewById(R.id.ivNav);
        ivatt = (ImageView) findViewById(R.id.ivatt);
        ivstoptri = (ImageView) findViewById(R.id.ivstoptri);

        Intent intent = getIntent();
        tripId = intent.getIntExtra(Constants.BUs_TRIP_ID, 0);
        busno = intent.getStringExtra("busno");
        mess.setOnClickListener(this);
        ivNav.setOnClickListener(this);
        ivatt.setOnClickListener(this);
        ivstoptri.setOnClickListener(this);
        btnTripStatus.setOnClickListener(this);
        currentStop = new BusStop();
        previousStop = new BusStop();
        nextStop = new BusStop();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        try {

            busTripId = getIntent().getIntExtra(Constants.BUs_TRIP_ID, -1);
            if (busTripId == -1) {
                busTripId = PreferencesManger.getIntFields(this, Constants.Pref.KEY_BUS_TRIP_ID);
            }

//            PreferencesManger.addIntFields(this, Constants.Pref.KEY_CURRENT_STOP_SQU, 0);
//            PreferencesManger.addBooleanFields(this, Constants.Pref.KEY_IS_TRIP_START, false);
            shiftType = PreferencesManger.getIntFields(this, Constants.Pref.KEY_SHIFT_TYPE);
            isTripRun = PreferencesManger.getBooleanFields(this, Constants.Pref.KEY_IS_TRIP_START);
            currentSqu = PreferencesManger.getIntFields(this, Constants.Pref.KEY_CURRENT_STOP_SQU);

            Log.e("isTripRun", "isTripRun --" + isTripRun);

        } catch (Exception e) {
            e.printStackTrace();
        }

        busStopArrayList = new ArrayList<>();
        checkForTripStatus();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        if (isTripRun) {
            getRoute();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForLocationEnable(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTripStatus:
                showSubmitConfirmation();
                break;

            case R.id.mess:
                openMessageSelector();
                break;
            case R.id.ivstoptri:
                stopnow = 1;
                showSubmitConfirmation();
                break;

            case R.id.ivatt:
                if (currentSqu <= 1) {
                    Toast.makeText(TripActivity.this, "Attendance can be marked once bus reaches the bus stop", Toast.LENGTH_LONG).show();
                } else {
                    PreferencesManger.addBooleanFields(getApplicationContext(), Constants.Pref.KEY_IS_CURRENT, true);
                    Intent intd = new Intent(TripActivity.this, StudentListActivity.class);
                    intd.putExtra(Constants.BUS_STOP_NAME, previousStop.getStopName());
                    intd.putExtra(Constants.BUS_STOP_Address, previousStop.getAddress());
                    intd.putExtra(Constants.BUS_RUNNING_ID, previousStop.getBusAssignId());
                    intd.putExtra(Constants.BUs_TRIP_ID, tripId);
                    startActivity(intd);
                }
                break;

            case R.id.ivNav:
                String str = "google.navigation:q=";
                str = str + nextStop.getLatitude() + "," + nextStop.getLongitude();
                Uri gmmIntentUri = Uri.parse(str);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                break;
        }
    }

    private void openMessageSelector() {
        final ArrayList<MessageTemplate> list = new ArrayList<>();
        list.addAll(MessageTemplate.listAll(MessageTemplate.class));
        final String[] items = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            items[i] = list.get(i).getTemplate();
        }
        new AlertDialog.Builder(this)
                .setSingleChoiceItems(items, 0, null).setTitle("Select Message :")
                .setPositiveButton("send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();

                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        String mess = list.get(selectedPosition).getBody();
                        sentMessage(mess);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void sentMessage(String message) {
        try {
            if (UIUtil.isInternetAvailable(this)) {
                UIUtil.startProgressDialog(this, "sending message...");
                final JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("message", message);
                jsonObject.addProperty("bus_trip_id", tripId);
                jsonObject.addProperty("branch_id", PreferencesManger.getStringFields(getApplicationContext(), Constants.Pref.KEY_BRANCH_ID));
                Log.e("jsonObject", jsonObject.toString());

                RetrofitAPI.getInstance(this).getApi().setMessageToCompleteBusTrip(jsonObject, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject object, Response response) {
                        UIUtil.stopProgressDialog(getApplicationContext());
                        try {
                            Log.e("jsonObject", "object -- : " + object.toString());
                            if (object == null) {
                                Toast.makeText(TripActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (object.get("status").getAsInt() == Constants.SUCCESS) {
                                Toast.makeText(TripActivity.this, object.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(TripActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        UIUtil.stopProgressDialog(getApplicationContext());
                        Toast.makeText(TripActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(TripActivity.this, "No internet", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        if (location.getAccuracy() < 50) {

            Log.e("isTripRun", "isTripRun ------------------- " + isTripRun);
            if (isTripRun) {
                checkForAutoCall();
            }
            alterNetSpeedCheck = !alterNetSpeedCheck;

            this.location = location;
            currentDate = new Date();
            Log.e("Date", "date ::: " + currentDate.toString());

            if (firstTime) {
                firstTime = false;
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                mLastLocation = location;
                lastDate = new Date();
                Log.e("Date", "lastDate ::: " + lastDate.toString());
            }

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            if (alterNetSpeedCheck) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                if (isTripRun)
                    checkForSpeed();
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                mLastLocation = location;
                lastDate = new Date();
            }
            Log.e("Location", "Location  bearing :  " + location.getBearing() + " , Acuricy : " + location.getAccuracy());
            if (marker == null) {
                marker = mMap.addMarker(new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_icon)).rotation(location.getBearing()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(17));

            }

            animateMarker(location, marker, mMap);
            rotateMarker(marker, location.getBearing());

            if (!polyLineVisible)
                getRoute();
        } else {
            try {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (firstTime) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            if (marker == null) {
                marker = mMap.addMarker(new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_icon)).rotation(location.getBearing()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
            }

            CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
            mMap.moveCamera(center);
            mMap.animateCamera(zoom);
            animateMarker(location, marker, mMap);
        }

    }

    private void drawMarker(LatLng point) {
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        markerOptions.position(point);

        // Adding marker on the Google Map
        mMap.addMarker(markerOptions);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode != Constants.LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mGoogleApiClient.connect();
        } else {
            finish();
            Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        startLocationUpdates();
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
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        Log.e("Location", "startLocationUpdates ----- startLocationUpdates");
        createLocationRequest(LOCATION_INTERVAL);
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void checkForTripStatus() {
        try {
            isTripRun = PreferencesManger.getBooleanFields(this, Constants.Pref.KEY_IS_TRIP_START);
            currentSqu = PreferencesManger.getIntFields(this, Constants.Pref.KEY_CURRENT_STOP_SQU);


            Log.e("isTripRun", "isTripRun ------ " + isTripRun);
            Log.e("currentSqu", "currentSqu -------- " + currentSqu);
      /*  if (isTripRun) {
            tvNavTo.setVisibility(View.VISIBLE);
        } else {
            tvNavTo.setVisibility(View.GONE);
        }*/

            busStopArrayList.clear();
            busStopArrayList.addAll(BusStop.find(BusStop.class, "bus_trip_id = ? and busno = ? ", new String[]{String.valueOf(busTripId), busno}));

            if (isTripRun) {
                previousStop = getStopFromSqu(currentSqu - 1);
                currentStop = getStopFromSqu(currentSqu);
                nextStop = getStopFromSqu(currentSqu + 1);

                if (currentSqu >= busStopArrayList.size()) {
                    Log.e("currentSqu", currentSqu + "");
                    Log.e("busStopArrayList", busStopArrayList.size() + "");
                    isLastStop = true;
                    tvNextStop.setText("Last Stop : " + currentStop.getStopName());
                    btnTripStatus.setText("End Trip at " + currentStop.getStopName());
                    tvNavTo.setVisibility(View.VISIBLE);
                    ivstoptri.setVisibility(View.VISIBLE);
                    tvNavTo.setText(address);
                } else {

                    Log.e("currentSqu", currentSqu + "");
                    Log.e("busStopArrayList", busStopArrayList.size() + "");
                    isLastStop = false;
                    tvNextStop.setText(currentStop.getStopName());
                    btnTripStatus.setText("Reached at " + currentStop.getStopName());
                    tvNavTo.setVisibility(View.VISIBLE);
                    ivstoptri.setVisibility(View.VISIBLE);
                    tvNavTo.setText(address);

                }
                autoUpdate = true;

            } else {

                Log.e("Stop", "Trip is Not running " + currentSqu);
                currentStop = getStopFromSqu(1);
                nextStop = getStopFromSqu(2);
                tvNextStop.setText("Trip not Started yet!");
                btnTripStatus.setText("Start Trip");
                tvNavTo.setVisibility(View.GONE);
                ivstoptri.setVisibility(View.GONE);
            }

            Log.e("Stop", "Current Squ - " + currentSqu);
            Log.e("Stop", "list  - " + busStopArrayList.toString());
            Log.e("Stop", "Current stop - " + currentStop.toString());
            Log.e("Stop", "Next stop - " + nextStop.toString());

            if (isTripRun) {
                getRoute();
                Intent intentS = new Intent(this, LatLongUpdateService.class);
                intentS.putExtra(Constants.CITY, "START");
                intentS.putExtra(Constants.LAT, currentStop.getLatitude());
                intentS.putExtra(Constants.LNG, currentStop.getLongitude());

                startService(intentS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showSubmitConfirmation() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String str, stopName = "";
        if (stopnow == 0) {
            if (isTripRun) {
                startTrip = false;
                stopName = currentStop.getStopName();
                str = "Are you sure";
            } else {
                startTrip = true;
                str = "Do you want to start the trip";
            }
            if (isLastStop) {
                str = "Do you want to end the trip";
            }
        } else {
            str = "Are you sure";
            startTrip = false;
            isLastStop = true;
            tvNavTo.setVisibility(View.GONE);
        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_AppBarOverlay);

        builder.setTitle("Confirmation");
        String message = str + " ?";
        builder.setMessage(message);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                getNextStopTime();
                getBusAssignIds();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (stopnow == 1) {
                    startTrip = true;
                    isLastStop = false;
                    stopnow = 0;
                    checkForTripStatus();
                }
                dialog.dismiss();
            }
        });
        builder.show();

    }

    private void updateTripStatus() {
        try {
            if (UIUtil.isInternetAvailable(this)) {
                UIUtil.startProgressDialog(this, "Getting trip data...");
                try {
                    final JsonObject jsonObject = new JsonObject();
                    final JsonObject jsonObject1 = new JsonObject();
                    if (startTrip) {
                        jsonObject.addProperty("flag", Constants.START);
                        jsonObject.addProperty("next_stop_id", currentStop.getStopId());
                        jsonObject.addProperty("bus_assign_id", busid);
                    } else {
                        jsonObject.addProperty("trip_id", PreferencesManger.getIntFields(this, Constants.Pref.KEY_TRIP_ID));
                        jsonObject.addProperty("current_stop_id", currentStop.getStopId());
                        jsonObject.addProperty("next_stop_id", nextStop.getStopId());
                        jsonObject.addProperty("next_bus_assign_id", nextStop.getBusAssignId());
                        jsonObject.addProperty("flag", Constants.RUNNING);
                        jsonObject.addProperty("bus_assign_id", busid);
                    }
                    if (isLastStop) {
                        jsonObject.addProperty("flag", Constants.END);
                        jsonObject.addProperty("current_stop_id", currentStop.getStopId());
                    }
                    jsonObject.addProperty("latitude", location.getLatitude());
                    jsonObject.addProperty("longitude", location.getLongitude());
                    jsonObject.addProperty("speed", location.getSpeed());
                    jsonObject.addProperty("bearing", location.getBearing());
                    jsonObject.addProperty("bus_trip_id", busTripId);
                    jsonObject1.addProperty("branch_id", PreferencesManger.getStringFields(getApplicationContext(), Constants.Pref.KEY_BRANCH_ID));
                    jsonObject1.add("name_value_pairs", jsonObject);
                    Log.e("Json", "json ----" + jsonObject.toString());

                    Log.e("start_trip", "-------before----------------------------RetrofitAPI");

                    RetrofitAPI.getInstance(this).getApi().updateTripStatus(jsonObject1, new Callback<JsonObject>() {
                        @Override
                        public void success(JsonObject jsonResponse, Response response) {
                            UIUtil.stopProgressDialog(getApplicationContext());
                            Log.e("start_trip", "-----------------------------------RetrofitAPI.success");
                            try {
                                if (jsonResponse.get("status").getAsInt() == Constants.SUCCESS) {
                                    if (!startTrip) {
                                        currentStop.setReached(true);
                                        currentStop.save();
                                    }
                                    Log.e("start_trip", "-----------------------------------RetrofitAPI.success ---status succ");
                                    PreferencesManger.addIntFields(getApplicationContext(), Constants.Pref.KEY_TRIP_ID, jsonResponse.get("trip_id").getAsInt());
                                    if (isLastStop) {

                                        Log.e("start_trip", "-----------------------------------RetrofitAPI.success ---isLastStop");
                                        stopTrip();
                                        return;
                                    }
                                    if (startTrip) {
                                        startTrip = false;
                                        Log.e("start_trip", "-----------------------------------RetrofitAPI.success ---startTrip");
                                        updateTripStart();
                                        autoUpdate = true;
                                    } else {
                                        Log.e("start_trip", "-----------------------------------RetrofitAPI.success ---updateTripRunningStatus");
                                        updateTripRunningStatus();
                                        autoUpdate = true;
                                        Log.e("start_trip", " -------------------------------- " + autoUpdate);
                                    }
//                                Toast.makeText(getApplicationContext(), "" + jsonResponse.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e("start_trip", "-----------------------------------RetrofitAPI.success ---status errror");
                                    autoUpdate = true;
                                    Toast.makeText(getApplicationContext(), "" + jsonResponse.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                autoUpdate = true;
                                Log.e("start_trip", "-----------------------------------RetrofitAPI.success ---status Exception");
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e("start_trip", "-----------------------------------RetrofitAPI.failure");
                            autoUpdate = true;
                            UIUtil.stopProgressDialog(getApplicationContext());
                            Toast.makeText(getApplicationContext(), "Server is not responding, Try after some time.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    autoUpdate = true;
                    UIUtil.stopProgressDialog(getApplicationContext());
                    Toast.makeText(getApplicationContext(), "Something went wrong, Please restart Application.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void markAttendanceForStudent() {
        try {
            List<Student> list = new ArrayList<>();
            list.addAll(Student.find(Student.class, "bus_trip_id = ?", new String[]{String.valueOf(busTripId)}));

            if (list.size() > 0) {
                String studentIds = "";
                for (int i = 0; i < list.size(); i++) {
                    studentIds = studentIds + list.get(i).getStudentId() + ",";
                }
                if (UIUtil.isInternetAvailable(this)) {
                    UIUtil.startProgressDialog(this, "Marking attendance...");
                    final JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("student_id", studentIds);
                    jsonObject.addProperty("trip_id", PreferencesManger.getIntFields(this, Constants.Pref.KEY_TRIP_ID));

                    JsonObject object = new JsonObject();
                    object.addProperty("check_in", true);

                    jsonObject.add("attendance", object);
                    jsonObject.addProperty("branch_id", PreferencesManger.getStringFields(getApplicationContext(), Constants.Pref.KEY_BRANCH_ID));
                    Log.e("jsonObject", jsonObject.toString());

                    RetrofitAPI.getInstance(this).getApi().markAttendance(jsonObject, new Callback<JsonObject>() {
                        @Override
                        public void success(JsonObject object, Response response) {
                            UIUtil.stopProgressDialog(getApplicationContext());
                            try {
                                Log.e("jsonObject", "object -- : " + object.toString());
                                if (object == null) {
                                    Toast.makeText(TripActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (object.get("status").getAsInt() == Constants.SUCCESS) {
                                    Toast.makeText(TripActivity.this, object.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(TripActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            UIUtil.stopProgressDialog(getApplicationContext());
                            Toast.makeText(TripActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(TripActivity.this, "No internet", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getBusAssignIds() {
        for (int i = 0; i < busStopArrayList.size(); i++) {
            busid = busid + busStopArrayList.get(i).getBusAssignId().toString() + ",";
        }
        updateTripStatus();
    }
/*
    private JSONArray getBusAssignIds() {
        JSONArray array = new JSONArray();
        for (int i = 0; i < busStopArrayList.size(); i++) {
            array.put(busStopArrayList.get(i).getBusAssignId());
        }
        return array;
    }
*/

    private void updateTripStart() {
        try {
            PreferencesManger.addBooleanFields(this, Constants.Pref.KEY_IS_TRIP_START, true);
            PreferencesManger.addIntFields(this, Constants.Pref.KEY_CURRENT_STOP_SQU, 1);
            markAttendanceForStudent();
            checkForTripStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void updateTripRunningStatus() {
        try {
            int currentSqu = PreferencesManger.getIntFields(this, Constants.Pref.KEY_CURRENT_STOP_SQU) + 1;
            PreferencesManger.addIntFields(this, Constants.Pref.KEY_CURRENT_STOP_SQU, currentSqu);
            Log.e("start_trip", "-----------------------------------updateTripRunningStatus ---currentSqu : " + currentSqu);
            checkForTripStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopTrip() {
        try {
            PreferencesManger.addBooleanFields(this, Constants.Pref.KEY_IS_TRIP_START, false);
            PreferencesManger.addIntFields(this, Constants.Pref.KEY_CURRENT_STOP_SQU, 0);

            Intent intentS = new Intent(this, LatLongUpdateService.class);
            intentS.putExtra(Constants.CITY, "STOP");
            intentS.putExtra(Constants.LAT, currentStop.getLatitude());
            intentS.putExtra(Constants.LNG, currentStop.getLongitude());
            startService(intentS);

            for (int i = 0; i < busStopArrayList.size(); i++) {

                BusStop busStop = busStopArrayList.get(i);
                busStop.setReached(false);
                busStop.save();
            }
            Toast.makeText(TripActivity.this, "Trip Stopped", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private BusStop getStopFromSqu(int id) {
        for (int i = 0; i < busStopArrayList.size(); i++) {
            if (id == busStopArrayList.get(i).getPriority()) {
                return busStopArrayList.get(i);
            }
        }
        return new BusStop();
    }


    private void getRoute() {
        if (PreferencesManger.getBooleanFields(this, Constants.Pref.KEY_IS_TRIP_START)) {
            String url;
            url = "http://maps.googleapis.com/maps/api/directions/json?origin=" + latitude + "," + longitude + "&destination="
                    + currentStop.getLatitude() + "," + currentStop.getLongitude();
            Log.e("Url", "Url --- " + url);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(com.android.volley.Request.Method.GET,
                    url, null,
                    new com.android.volley.Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Success", response.toString());
                            bindPolyLine(response);
                        }

                    }, new com.android.volley.Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.e("Error", " E -----------" + error.getMessage());
                    VolleyLog.d("", "Error: " + error.getMessage());
                }
            });
            Volley.newRequestQueue(getApplicationContext()).add(jsonObjReq);
        }
    }

    private void bindPolyLine(JSONObject jsonObject) {

        try {

            JSONArray routeArray = jsonObject.getJSONArray("routes");
            JSONObject firstRouteObject = routeArray.getJSONObject(0).getJSONArray("legs").getJSONObject(0);
            String poliline = routeArray.getJSONObject(0).getJSONObject("overview_polyline").getString("points");
            String totalDistanc = firstRouteObject.getJSONObject("distance").getString("text");
            nextTopTime = firstRouteObject.getJSONObject("duration").getString("text");
            address = firstRouteObject.getString("end_address");
            tvNavTo.setText(address);

            Log.e("Lat", "totalDistanc --: " + totalDistanc + "  totalTime --:  " + nextTopTime + "  Address --:  " + address);

            mMap.clear();
            marker = null;
            onLocationChanged(location);

            List<LatLng> polyz = MapUtility.decodePolyLat(poliline);

            for (int i = 0; i < polyz.size() - 1; i++) {

                LatLng src = polyz.get(i);
                LatLng dest = polyz.get(i + 1);

                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(src.latitude, src.longitude),
                                new LatLng(dest.latitude, dest.longitude))
                        .width(4).color(getResources().getColor(R.color.colorPrimary)).geodesic(true));

            }
            for (int i = 0; i < busStopArrayList.size(); i++) {
                BusStop lat = busStopArrayList.get(i);
              /*  BusStop lon=busStopArrayList.get(i+1);*/
                drawMarker(new LatLng(Double.parseDouble(String.valueOf(lat.getLatitude())), Double.parseDouble(String.valueOf(lat.getLongitude()))));
            }
            polyLineVisible = true;
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static final void animateMarker(final Location destination, final Marker marker, final GoogleMap googleMap) {
        if (marker != null) {
            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = new LatLng(destination.getLatitude(), destination.getLongitude());
//            final LatLng endPosition = new LatLng(12.911978, 77.644168);

            final float startRotation = marker.getRotation();

            final LatLngInterpolator latLngInterpolator = new LatLngInterpolator.LinearFixed();
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(5000); // duration 5 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                        marker.setPosition(newPosition);
//                        marker.setRotation(destination.getBearing());
//                        MapUtility.rotateMarker(marker, destination.getBearing(), googleMap);
//                        marker.setRotation(MapUtility.computeRotation(v, destination.getBearing(), 180));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            valueAnimator.start();
        }
    }

    private void rotateMarker(final Marker marker, final float toRotation) {
        Log.e("Rotation", " Rotation :: " + toRotation);
//        if (!isMarkerRotating) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final float startRotation = marker.getRotation();
        final long duration = 2000;
        marker.setAnchor(0.5f, 0.5f);
        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                isMarkerRotating = true;

                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);

                float rot = t * toRotation + (1 - t) * startRotation;

                marker.setRotation(rot > 180 ? rot / 2 : rot);
//                    marker.setRotation(rot);
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    isMarkerRotating = false;
                }
            }
        });
//        }
    }


    public void showDialog() {
        dialog = new Dialog(this, R.style.AppTheme_AppBarOverlay);
        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alert_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.llAlert);

        linearLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink));
        TextView dialogButton = (TextView) dialog.findViewById(R.id.tvAlertName);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                checkForSpeed();
            }
        });

        dialog.show();

    }

    private void checkForSpeed() {
        double kilometers = MapUtility.distance(latitude, longitude, location.getLatitude(), location.getLongitude());
        Log.e("speed", "kilometer  :  " + kilometers);
        double hours = (6d / 3600d);
        Log.e("speed", "hours  :  " + hours);
        speed = kilometers / hours;
        PreferencesManger.addStringFields(getApplicationContext(), Constants.Pref.SPEE, String.valueOf(speed));

        double dist = location.distanceTo(mLastLocation) * 0.001;
        double time = MapUtility.getTimeDiff(currentDate, lastDate);
        double speedTo = (dist / time) + 5;
        location.setSpeed((float) speedTo);

        Log.e("speed", "speed --- = " + speed);
//        Toast.makeText(this, "Speed : " + speedTo + "\n bearing : " + location.getBearing() + "\n Accuracy :  " + location.getAccuracy(), Toast.LENGTH_SHORT).show();

        if (speedTo > MAX_SPEED_LIMIT) {
            if (!speedLimit) {
                showDialog();
                speedLimit = true;
                speedLimitexceeded(speedTo);
            }
        } else {
            if (dialog != null && dialog.isShowing()) {
                speedLimit = false;
                dialog.dismiss();
            }
        }

        Log.e("Dist ", "DDDDDDDDDDDDDDDDDD ----- : " + location.distanceTo(mLastLocation));

        Log.e("Dist ", "DDDDDDDDDDDDDDDDDD ----- time: " + MapUtility.getTimeDiff(currentDate, lastDate) * 3600);
        Log.e("Dist ", "DDDDDDDDDDDDDDDDDD ----- Speed: " + speedTo);

    }

    private void speedLimitexceeded(double speedTo) {
try{
        if (UIUtil.isInternetAvailable(this)) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("trip_id", PreferencesManger.getIntFields(this, Constants.Pref.KEY_TRIP_ID));
            jsonObject.addProperty("max_speed", speedTo);
            jsonObject.addProperty("branch_id", PreferencesManger.getStringFields(getApplicationContext(), Constants.Pref.KEY_BRANCH_ID));

            RetrofitAPI.getInstance(this).getApi().getmaxspeed(jsonObject, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject object, Response response) {
                    UIUtil.stopProgressDialog(getApplicationContext());
                    Log.e("jsonObject", "jsonObject --- " + object.toString());

                    try {
                        if (object == null) {
                            Toast.makeText(TripActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (object.get("status").getAsInt() == Constants.SUCCESS) {

                            Toast.makeText(TripActivity.this, object.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(TripActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    UIUtil.stopProgressDialog(getApplicationContext());
                }
            });
        } else {

            Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }}catch (Exception e){
    e.printStackTrace();
}
    }

    private void checkForAutoCall() {

        try {
            BusStop busStop = getStopFromSqu(currentSqu);
            Log.e("start_tripkk", "currentStop ----------------- " + busStop.toString());
            if ((!busStop.isReached()) && autoUpdate) {
                Log.e("start_tripkk", "currentStop ----------------- " + currentStop.toString());
                autoUpdate = false;

                double k = MapUtility.distance(Double.parseDouble(currentStop.getLatitude()), Double.parseDouble(currentStop.getLongitude()),
                        location.getLatitude(), location.getLongitude());

                k = k * 1000;

                Log.e("start_tripkk", " ----------------- Distaance  : " + k);

                if (k < MAX_DIST_FOR_AUTO_UPDATE) {
                    Log.e("start_tripkk", "----------------- MAX_DIST_FOR_AUTO_UPDATE  ");
                    updateTripStatus();
                } else {
                    Log.e("start_tripkk", " -----------------NNNNNot  MAX_DIST_FOR_AUTO_UPDATE");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    private boolean checkForLocationEnable(final Context context) {

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (!gps_enabled && !network_enabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("Alert");
            dialog.setMessage("Please enable GPS.");
            dialog.setPositiveButton("GPS", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                }
            });
            dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    finish();
                }
            });
            dialog.setCancelable(false);
            dialog.show();
        } else {
            checkForLocationPermission();
        }
        return gps_enabled;
    }


    private void checkForLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            mGoogleApiClient.connect();
        }
    }

}

