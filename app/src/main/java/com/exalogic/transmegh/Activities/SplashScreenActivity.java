package com.exalogic.transmegh.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.exalogic.transmegh.API.RetrofitAPI;
import com.exalogic.transmegh.Database.PreferencesManger;
import com.exalogic.transmegh.MapUtility.LatLongUpdateService;
import com.exalogic.transmegh.Models.VersionChecker;
import com.exalogic.transmegh.Models.database.BusStop;
import com.exalogic.transmegh.Utility.Constants;
import com.exalogic.transmegh.Utility.UIUtil;
import com.exalogic.transmegh.R;
import com.google.gson.JsonObject;
import com.orm.SchemaGenerator;
import com.orm.SugarContext;
import com.orm.SugarDb;
import com.splunk.mint.Mint;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SplashScreenActivity extends Activity {

    public static final int TIME_OUT = 2000;
    Handler handler;
    private TextView tvError;
    private Button btnRetry;
    private String playstoreVersion, currentAppVersionCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        SugarContext.init(this);

        Mint.initAndStartSession(SplashScreenActivity.this, "9d9097da");

        tvError = (TextView) findViewById(R.id.tvError);
        tvError.setVisibility(View.GONE);
        SugarContext.terminate();
        SchemaGenerator schemaGenerator = new SchemaGenerator(this);
        schemaGenerator.deleteTables(new SugarDb(this).getDB());
        SugarContext.init(this);
        schemaGenerator.createDatabase(new SugarDb(this).getDB());
        btnRetry = (Button) findViewById(R.id.btnRetry);
        btnRetry.setVisibility(View.GONE);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtil.startProgressDialog(SplashScreenActivity.this, "Please wait");
                checkForTripRunningStatus();
                btnRetry.setVisibility(View.GONE);
                tvError.setVisibility(View.GONE);
            }
        });

        VersionChecker versionChecker = new VersionChecker();
        try {
            playstoreVersion = versionChecker.execute().get();
            Log.e("play store versionCode", "==========" + playstoreVersion);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            currentAppVersionCode = String.valueOf(pInfo.versionName);
            Log.e("App versionCode", "==========" + currentAppVersionCode);
            //  Log.e("versionCode", "==========" + playStoreVersionCode);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (playstoreVersion.compareTo(currentAppVersionCode) > 0) {
//Show update popup or whatever best for you
                        startActivity(new Intent(getApplicationContext(), SecondActivity.class));

                    } else {
//                PreferencesManger.addBooleanFields(getApplicationContext(), Constants.Pref.KEY_IS_TRIP_START, false);
                        if (TextUtils.isEmpty(PreferencesManger.getStringFields(getApplicationContext(), Constants.Pref.KEY_TOKEN))) {
                            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                            overridePendingTransition(R.anim.right_to_center, R.anim.center_to_left);
                            finish();
                        } else {
                            checkForLocationEnable(SplashScreenActivity.this);
                            checkForTripRunningStatus();
                        }

                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }, TIME_OUT);
    }

    private void checkForTripRunningStatus() {
        try {
            if (UIUtil.isInternetAvailable(this)) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("branch_id", PreferencesManger.getStringFields(getApplicationContext(), Constants.Pref.KEY_BRANCH_ID));
                RetrofitAPI.getInstance(this).getApi().getRunningTripStatus(params, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        UIUtil.stopProgressDialog(SplashScreenActivity.this);
                        if (jsonObject == null) {
                            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                            return;
                        }

                        Log.e("jsonObject", "jsonObject ---- " + jsonObject.toString());
                        try {
                            if (jsonObject.get("status").getAsInt() == Constants.TRIP_RUNNING) {
                                PreferencesManger.addBooleanFields(SplashScreenActivity.this, Constants.Pref.KEY_IS_TRIP_START, true);

                                try {
                                    int busTripId = jsonObject.get("bus_trip_id").getAsInt();
                                    PreferencesManger.addIntFields(SplashScreenActivity.this, Constants.Pref.KEY_BUS_TRIP_ID, busTripId);
                                    int stopId = jsonObject.get("current_stop_id").getAsInt();
                                    int squ = BusStop.find(BusStop.class, "stop_id = ? and bus_trip_id = ? ", new String[]{String.valueOf(stopId), String.valueOf(busTripId)}).get(0).getPriority();

                                    Log.e("DEMO", "busTripId : " + busTripId + "  stopID  : " + stopId + "  squ  : " + squ);
                                    PreferencesManger.addIntFields(SplashScreenActivity.this, Constants.Pref.KEY_CURRENT_STOP_SQU, squ + 1);

                                } catch (Exception e) {
                                    PreferencesManger.addIntFields(SplashScreenActivity.this, Constants.Pref.KEY_CURRENT_STOP_SQU, 1);
                                    e.printStackTrace();
                                }

                                PreferencesManger.addIntFields(getApplicationContext(), Constants.Pref.KEY_TRIP_ID, jsonObject.get("trip_id").getAsInt());
                                //=================================

                                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));

                                //=================================

                                overridePendingTransition(R.anim.right_to_center, R.anim.center_to_left);
                                finish();
                            }

                            if (Constants.SUCCESS == jsonObject.get("status").getAsInt()) {
                                PreferencesManger.addBooleanFields(SplashScreenActivity.this, Constants.Pref.KEY_IS_TRIP_START, false);
                                PreferencesManger.addIntFields(SplashScreenActivity.this, Constants.Pref.KEY_CURRENT_STOP_SQU, 0);
                                Intent intentS = new Intent(SplashScreenActivity.this, LatLongUpdateService.class);
                                intentS.putExtra(Constants.CITY, "STOP");
                                startService(intentS);
                                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                                overridePendingTransition(R.anim.right_to_center, R.anim.center_to_left);
                                finish();
                            }

                            if (Constants.ERROR == jsonObject.get("status").getAsInt()) {
                                btnRetry.setVisibility(View.VISIBLE);
                                tvError.setVisibility(View.VISIBLE);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            btnRetry.setVisibility(View.VISIBLE);
                            tvError.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(SplashScreenActivity.this, "Check Internet connection", Toast.LENGTH_SHORT).show();
                        UIUtil.stopProgressDialog(SplashScreenActivity.this);
                        btnRetry.setVisibility(View.VISIBLE);
                        tvError.setVisibility(View.VISIBLE);
                        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                        overridePendingTransition(R.anim.right_to_center, R.anim.center_to_left);
                        finish();
                    }
                });
            } else {
                Toast.makeText(this, "Check Internet connection", Toast.LENGTH_SHORT).show();
                btnRetry.setVisibility(View.VISIBLE);
                tvError.setVisibility(View.VISIBLE);
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.right_to_center, R.anim.center_to_left);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //permission check

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
            checkForTripRunningStatus();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode != Constants.LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkForTripRunningStatus();
        } else {
            finish();
            Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
        }
    }

}
