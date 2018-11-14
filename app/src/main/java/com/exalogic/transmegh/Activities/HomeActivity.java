/*
package com.exalogic.inmegh.driver.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.exalogic.inmegh.driver.API.RetrofitAPI;
import com.exalogic.inmegh.driver.Adapters.TripRecyclerAdapter;
import com.exalogic.inmegh.driver.Database.PreferencesManger;
import com.exalogic.inmegh.driver.MapUtility.LatLongUpdateService;
import com.exalogic.inmegh.driver.Models.MessageTemplateResponse;
import com.exalogic.inmegh.driver.Models.TripListResponse;
import com.exalogic.inmegh.driver.Models.database.BusStop;
import com.exalogic.inmegh.driver.Models.database.MessageTemplate;
import com.exalogic.inmegh.driver.Models.database.Trip;
import com.exalogic.inmegh.driver.R;
import com.exalogic.inmegh.driver.Utility.Constants;
import com.exalogic.inmegh.driver.Utility.UIUtil;
import com.google.gson.JsonObject;
import com.orm.SchemaGenerator;
import com.orm.SugarContext;
import com.orm.SugarDb;
import com.splunk.mint.Mint;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1222;
    private RecyclerView tripRecyclerView;
    private LinearLayout llTripRunning;
    private TextView tvTripRunning, tvPickup, tvDrop;
    private TripRecyclerAdapter adapter;
    private ArrayList<Trip> tripArrayList, allTripList;
    private ArrayList<MessageTemplate> messageTemplateArrayList;
    private boolean isPickup = true;
    private SwipeRefreshLayout swipeContainer;
    private int busTripId, TripId;
    private String busno;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Mint.initAndStartSession(HomeActivity.this, "9d9097da");
        SugarContext.init(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);

        ((TextView) view.findViewById(R.id.tvEmailaa)).setText(PreferencesManger.getStringFields(this, Constants.Pref.KEY_USER_EMAIL));
        ((TextView) view.findViewById(R.id.tvNameaa)).setText(PreferencesManger.getStringFields(this, Constants.Pref.KEY_USER_NAME));

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        SugarContext.terminate();
        SchemaGenerator schemaGenerator = new SchemaGenerator(getApplicationContext());
        schemaGenerator.deleteTables(new SugarDb(getApplicationContext()).getDB());
        SugarContext.init(getApplicationContext());
        schemaGenerator.createDatabase(new SugarDb(getApplicationContext()).getDB());

        tvPickup = (TextView) findViewById(R.id.tvPickup);
        tvDrop = (TextView) findViewById(R.id.tvDrop);
        tvPickup.setOnClickListener(this);
        tvDrop.setOnClickListener(this);

        llTripRunning = (LinearLayout) findViewById(R.id.llTripRunning);
        tvTripRunning = (TextView) findViewById(R.id.tvTripRunning);
        tvTripRunning.startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink));
        llTripRunning.setVisibility(View.GONE);

        tripRecyclerView = (RecyclerView) findViewById(R.id.recycleTrip);
        tripRecyclerView.setHasFixedSize(true);
        tripRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        tripArrayList = new ArrayList<>();
        allTripList = new ArrayList<>();
        adapter = new TripRecyclerAdapter(this, tripArrayList);
        tripRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        adapter.SetOnItemClickListener(new TripRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                pos = position;
                PreferencesManger.addBooleanFields(getApplicationContext(), Constants.Pref.KEY_IS_DROP, !isPickup);
                PreferencesManger.addBooleanFields(getApplicationContext(), Constants.Pref.KEY_IS_CURRENT, tripArrayList.get(position).isTripActive());
                Intent intent = new Intent(getApplicationContext(), StopListActivity.class);
                intent.putExtra(Constants.BUs_TRIP_ID, tripArrayList.get(position).getBusTripId());
                intent.putExtra(Constants.TRIP_NAME, tripArrayList.get(position).getBusTripName());
                intent.putExtra(Constants.TRIP_START_TIME, tripArrayList.get(position).getStartTime());
                intent.putExtra(Constants.TRIP_END_TIME, tripArrayList.get(position).getEndTime());
                intent.putExtra("busno", tripArrayList.get(position).getBusno());
                intent.putExtra("tripstatus", tripArrayList.get(position).getTripstatus());
                busno = tripArrayList.get(position).getBusno();

                startActivity(intent);
            }
        });


        updateTripListOnUI();


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTripDetails();
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        if (allTripList.size() < 1)
            getTripDetails();


        try {
            messageTemplateArrayList = new ArrayList<>();
            messageTemplateArrayList.addAll(MessageTemplate.listAll(MessageTemplate.class));
            if (!(messageTemplateArrayList.size() > 0)) {
                getMessageTemplate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForTripRunningStatus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPickup:
                tvPickup.setTextColor(getResources().getColor(R.color.white));
                tvDrop.setTextColor(getResources().getColor(R.color.white_light));
                isPickup = true;
                updateTripListOnUI();
                updateActiveTrip(busTripId, busno, TripId);
                break;
            case R.id.tvDrop:
                tvPickup.setTextColor(getResources().getColor(R.color.white_light));
                tvDrop.setTextColor(getResources().getColor(R.color.white));
                isPickup = false;
                updateTripListOnUI();
                updateActiveTrip(busTripId, busno, TripId);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            startActivity(new Intent(this, ProfileActivity.class));
        } else if (id == R.id.mybus) {
            startActivity(new Intent(this, BusActivity.class));
        } */
/*else if (id == R.id.logout) {
            showLogoutConfirmation();
        }*//*
*/
/* else if (id == R.id.chat) {
            Toast.makeText(this, "Coming Soon..!", Toast.LENGTH_SHORT).show();
        }*//*
 else if (id == R.id.parent) {
            if (!PreferencesManger.getBooleanFields(this, Constants.Pref.KEY_IS_TRIP_START)) {
                startActivity(new Intent(this, ParentListActivity.class));
            } else {
                showTripRunningForChat();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void getTripDetails() {
        if (UIUtil.isInternetAvailable(this)) {
            UIUtil.startProgressDialog(this, "Setting up your trips for today...");

            RetrofitAPI.getInstance(this).getApi().getTripList(new Callback<TripListResponse>() {
                @Override
                public void success(TripListResponse tripListResponse, Response response) {
                    UIUtil.stopProgressDialog(getApplicationContext());
                    if (tripListResponse == null)
                        return;
                    if (tripListResponse.getStatus() == Constants.SUCCESS) {
                        bindData(tripListResponse);
                    } else {
                        Toast.makeText(getApplicationContext(), "" + tripListResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    checkForTripRunningStatus();
                }

                @Override
                public void failure(RetrofitError error) {
                    UIUtil.stopProgressDialog(getApplicationContext());
                    checkForTripRunningStatus();
                    Toast.makeText(getApplicationContext(), "Server is not responding, Try after some time.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void bindData(TripListResponse response) {
        allTripList.clear();
        allTripList.addAll(response.getTrips());
        Trip.deleteAll(Trip.class);
        BusStop.deleteAll(BusStop.class);
        Trip.saveInTx(allTripList);
        for (int i = 0; i < allTripList.size(); i++) {
            BusStop.saveInTx(allTripList.get(i).getBusStop());
        }
        if (allTripList.size() == 0) {
            Toast.makeText(getApplicationContext(), "No trip for today", Toast.LENGTH_LONG).show();
        }
        updateTripListOnUI();
    }


    private void getMessageTemplate() {
        if (UIUtil.isInternetAvailable(this)) {
            UIUtil.startProgressDialog(this, "Getting SMS templates...");

            RetrofitAPI.getInstance(this).getApi().getMessageTemplate(new Callback<MessageTemplateResponse>() {
                @Override
                public void success(MessageTemplateResponse messageTemplateResponse, Response response) {
                    UIUtil.stopProgressDialog(getApplicationContext());
                    if (messageTemplateResponse == null)
                        return;
                    if (messageTemplateResponse.getStatus() == Constants.SUCCESS) {
                        MessageTemplate.deleteAll(MessageTemplate.class);
                        MessageTemplate.saveInTx(messageTemplateResponse.getTemplate());
                        // Toast.makeText(getApplicationContext(), "" + messageTemplateResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "" + messageTemplateResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    UIUtil.stopProgressDialog(getApplicationContext());
                    checkForTripRunningStatus();
                    Toast.makeText(getApplicationContext(), "Server is not responding, Try after some time.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }
    }


    private void showLogoutConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

//        AlertDialog.Builder builder =
//                new AlertDialog.Builder(this, R.style.AppTheme_AppBarOverlay);

        builder.setTitle("Confirmation");
        String message = "Do you want to logout?";
        builder.setMessage(message);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });
        builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    private void AlertForTeacher() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

//        AlertDialog.Builder builder =
//                new AlertDialog.Builder(this, R.style.AppTheme_AppBarOverlay);

        builder.setTitle("Alert");
        String message = "You do not have Permission to mark Attendance.";
        builder.setMessage(message);
        builder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }


    private void logout() {
        if (UIUtil.isInternetAvailable(this)) {
            UIUtil.startProgressDialog(this, "Please Wait.. logging out..");

            RetrofitAPI.getInstance(this).getApi().logout(new Callback<JSONObject>() {
                @Override
                public void success(JSONObject object, Response response) {
                    UIUtil.stopProgressDialog(getApplicationContext());
                    Log.e("API", "logout-" + object.toString());
                    Toast.makeText(getApplicationContext(), "Logout successfully..", Toast.LENGTH_SHORT).show();
                    PreferencesManger.clearPreferences(getApplicationContext());
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    overridePendingTransition(R.anim.right_to_center, R.anim.center_to_left);
                    finish();
                }

                @Override
                public void failure(RetrofitError error) {
                    PreferencesManger.clearPreferences(getApplicationContext());
                    UIUtil.stopProgressDialog(getApplicationContext());
                    Toast.makeText(getApplicationContext(), "Logout successfully..", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    overridePendingTransition(R.anim.right_to_center, R.anim.center_to_left);
                    finish();
                }
            });
        } else {
            Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

    }

    private void checkForTripRunningStatus() {
        if (UIUtil.isInternetAvailable(this)) {
            RetrofitAPI.getInstance(this).getApi().getRunningTripStatus(new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    UIUtil.stopProgressDialog(HomeActivity.this);
                    if (jsonObject == null) {
                        return;
                    }
                    Log.e("jsonObject", "jsonObject ---- " + jsonObject.toString());
                    try {
                        if (jsonObject.get("status").getAsInt() == Constants.TRIP_RUNNING) {
                            PreferencesManger.addBooleanFields(HomeActivity.this, Constants.Pref.KEY_IS_TRIP_START, true);

                            try {
                                busTripId = jsonObject.get("bus_trip_id").getAsInt();
                                busno = jsonObject.get("bus_no").getAsString();
                                TripId = jsonObject.get("trip_id").getAsInt();
                                updateActiveTrip(busTripId, busno, TripId);

                                PreferencesManger.addIntFields(HomeActivity.this, Constants.Pref.TRIP_ID, TripId);
                                PreferencesManger.addIntFields(HomeActivity.this, Constants.Pref.KEY_BUS_TRIP_ID, busTripId);
                                int stopId = jsonObject.get("current_stop_id").getAsInt();
                                int squ = BusStop.find(BusStop.class, "stop_id = ? and bus_trip_id = ? ", new String[]{String.valueOf(stopId), String.valueOf(busTripId)}).get(0).getPriority();

                                Log.e("DEMO", "busTripId : " + busTripId + "  stopID  : " + stopId + "  squ  : " + squ);
                                PreferencesManger.addIntFields(HomeActivity.this, Constants.Pref.KEY_CURRENT_STOP_SQU, squ + 1);

                            } catch (Exception e) {
                                PreferencesManger.addIntFields(HomeActivity.this, Constants.Pref.KEY_CURRENT_STOP_SQU, 1);
                                e.printStackTrace();
                            }

                            PreferencesManger.addIntFields(getApplicationContext(), Constants.Pref.KEY_TRIP_ID, jsonObject.get("trip_id").getAsInt());
//                            startActivity(new Intent(HomeActivity.this, TripActivity.class));
//                            overridePendingTransition(R.anim.right_to_center, R.anim.center_to_left);
//                            finish();
                            llTripRunning.setVisibility(View.VISIBLE);
                        } else {
                            llTripRunning.setVisibility(View.GONE);
                        }
                        if (Constants.SUCCESS == jsonObject.get("status").getAsInt()) {
                            PreferencesManger.addBooleanFields(HomeActivity.this, Constants.Pref.KEY_IS_TRIP_START, false);

                            Intent intentS = new Intent(HomeActivity.this, LatLongUpdateService.class);
                            intentS.putExtra(Constants.CITY, "STOP");
                            startService(intentS);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(HomeActivity.this, "Server not responding, Please try after some time ", Toast.LENGTH_SHORT).show();
                    UIUtil.stopProgressDialog(HomeActivity.this);
                }
            });
        } else {
            Toast.makeText(this, "Check Internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateActiveTrip(int busTripId, String busno, int tripId) {
        Log.e("bustripid", "id ::  " + busTripId);
        Log.e("tripid", "id ::  " + tripId);
        Log.e("busno", "id ::  " + busno);
        for (int i = 0; i < tripArrayList.size(); i++) {
            Trip trip = tripArrayList.get(i);
            Log.e("bustripid", "Trip ::  " + trip);
            Log.e("bustripno", "Trip ::  " + tripArrayList.get(i).getBusno());
            if (tripArrayList.get(i).getBusTripId() == busTripId && busno.equalsIgnoreCase(tripArrayList.get(i).getBusno())) {
                trip.setTripActive(true);
                tripArrayList.set(i, trip);
            } else {
                trip.setTripActive(false);
                tripArrayList.set(i, trip);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void showTripRunningForChat() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

//        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_AppBarOverlay);

        builder.setTitle("Alert");
        String message = "Trip is running, You can't chat with Parent while trip is running.. ";
        builder.setMessage(message);
        builder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private void updateTripListOnUI() {
        tripArrayList.clear();
        allTripList.clear();
        allTripList.addAll(Trip.listAll(Trip.class));
        for (int i = 0; i < allTripList.size(); i++) {
            if (isPickup) {
                if (allTripList.get(i).getPickupType())
                    tripArrayList.add(allTripList.get(i));
            } else {
                if (!allTripList.get(i).getPickupType())
                    tripArrayList.add(allTripList.get(i));
            }
        }
        adapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }

}
*/
