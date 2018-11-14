

package com.exalogic.transmegh.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.exalogic.transmegh.API.RetrofitAPI;
import com.exalogic.transmegh.Adapters.TripRecyclerAdapter;
import com.exalogic.transmegh.Database.PreferencesManger;
import com.exalogic.transmegh.MapUtility.LatLongUpdateService;
import com.exalogic.transmegh.Models.MessageTemplateResponse;
import com.exalogic.transmegh.Models.TripListResponse;
import com.exalogic.transmegh.Models.database.BusStop;
import com.exalogic.transmegh.Models.database.MessageTemplate;
import com.exalogic.transmegh.Models.database.Trip;
import com.exalogic.transmegh.Utility.Constants;
import com.exalogic.transmegh.Utility.UIUtil;
import com.exalogic.transmegh.R;
import com.google.gson.JsonObject;
import com.orm.SchemaGenerator;
import com.orm.SugarContext;
import com.orm.SugarDb;
import com.splunk.mint.Mint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeFragment extends Fragment {
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

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_one, container, false);

        Mint.initAndStartSession(getActivity(), "9d9097da");
        SugarContext.init(getActivity());

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        SugarContext.terminate();
        SchemaGenerator schemaGenerator = new SchemaGenerator(getActivity());
        schemaGenerator.deleteTables(new SugarDb(getActivity()).getDB());
        SugarContext.init(getActivity());
        schemaGenerator.createDatabase(new SugarDb(getActivity()).getDB());

        tvPickup = (TextView) view.findViewById(R.id.tvPickup);
        tvDrop = (TextView) view.findViewById(R.id.tvDrop);


        tvDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvPickup.setTextColor(getResources().getColor(R.color.textchange));
                tvDrop.setTextColor(getResources().getColor(R.color.white));
                tvDrop.setBackground(getResources().getDrawable(R.color.textchange));
                tvPickup.setBackground(getResources().getDrawable(R.color.white));
                isPickup = false;
                updateTripListOnUI();
                updateActiveTrip(busTripId, busno, TripId);
            }
        });
        tvPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvPickup.setTextColor(getResources().getColor(R.color.white));
                tvDrop.setTextColor(getResources().getColor(R.color.textchange));
                tvPickup.setBackground(getResources().getDrawable(R.color.textchange));
                tvDrop.setBackground(getResources().getDrawable(R.color.white));
                isPickup = true;
                updateTripListOnUI();
                updateActiveTrip(busTripId, busno, TripId);
            }
        });


        llTripRunning = (LinearLayout) view.findViewById(R.id.llTripRunning);
        tvTripRunning = (TextView) view.findViewById(R.id.tvTripRunning);
        tvTripRunning.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.blink));
        llTripRunning.setVisibility(View.GONE);

        tripRecyclerView = (RecyclerView) view.findViewById(R.id.recycleTrip);
        tripRecyclerView.setHasFixedSize(true);
        tripRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        tripArrayList = new ArrayList<>();
        allTripList = new ArrayList<>();
        adapter = new TripRecyclerAdapter(getActivity(), tripArrayList);
        tripRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        adapter.SetOnItemClickListener(new TripRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                pos = position;
                Log.e("Home fragment ... ... ... ...  ", String.valueOf(!isPickup));
                PreferencesManger.addBooleanFields(getActivity(), Constants.Pref.KEY_IS_DROP, !isPickup);
                PreferencesManger.addBooleanFields(getActivity(), Constants.Pref.KEY_IS_CURRENT, tripArrayList.get(position).isTripActive());
                Intent intent = new Intent(getActivity(), StopListActivity.class);
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
        checkForTripRunningStatus();
        return view;
    }


    private void getTripDetails() {
        try {
            if (UIUtil.isInternetAvailable(getActivity())) {
                UIUtil.startProgressDialog(getActivity(), "Setting up your trips for today...");
                Map<String, String> params = new HashMap<String, String>();
                params.put("branch_id", PreferencesManger.getStringFields(getActivity(), Constants.Pref.KEY_BRANCH_ID));
                RetrofitAPI.getInstance(getActivity()).getApi().getTripList(params, new Callback<TripListResponse>() {
                    @Override
                    public void success(TripListResponse tripListResponse, Response response) {
                        try {
                            UIUtil.stopProgressDialog(getActivity());
                            if (tripListResponse == null)
                                return;
                            if (tripListResponse.getStatus() == Constants.SUCCESS) {
                                bindData(tripListResponse);
                            } else {
                                Toast.makeText(getActivity(), "" + tripListResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            checkForTripRunningStatus();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        UIUtil.stopProgressDialog(getActivity());
                        checkForTripRunningStatus();
                        Toast.makeText(getActivity(), "Server is not responding, Try after some time.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(getActivity(), "Please Connect to Internet", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindData(TripListResponse response) {
        try {
            allTripList.clear();
            allTripList.addAll(response.getTrips());
            Trip.deleteAll(Trip.class);
            BusStop.deleteAll(BusStop.class);
            Trip.saveInTx(allTripList);
            for (int i = 0; i < allTripList.size(); i++) {
                BusStop.saveInTx(allTripList.get(i).getBusStop());
            }
            if (allTripList.size() == 0) {
                Toast.makeText(getActivity(), "No trip for today", Toast.LENGTH_LONG).show();
            }
            updateTripListOnUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getMessageTemplate() {
        try {
            if (UIUtil.isInternetAvailable(getActivity())) {
                UIUtil.startProgressDialog(getActivity(), "Getting SMS templates...");
                Map<String, String> params = new HashMap<String, String>();
                params.put("branch_id", PreferencesManger.getStringFields(getActivity(), Constants.Pref.KEY_BRANCH_ID));
                RetrofitAPI.getInstance(getActivity()).getApi().getMessageTemplate(params, new Callback<MessageTemplateResponse>() {
                    @Override
                    public void success(MessageTemplateResponse messageTemplateResponse, Response response) {
                        try {
                            UIUtil.stopProgressDialog(getActivity());
                            if (messageTemplateResponse == null)
                                return;
                            if (messageTemplateResponse.getStatus() == Constants.SUCCESS) {
                                MessageTemplate.deleteAll(MessageTemplate.class);
                                MessageTemplate.saveInTx(messageTemplateResponse.getTemplate());
                                // Toast.makeText(getActivity(), "" + messageTemplateResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "" + messageTemplateResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        UIUtil.stopProgressDialog(getActivity());
                        checkForTripRunningStatus();
                        Toast.makeText(getActivity(), "Server is not responding, Try after some time.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(getActivity(), "Please Connect to Internet", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void AlertForTeacher() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

//        AlertDialog.Builder builder =
//                new AlertDialog.Builder(getActivity(), R.style.AppTheme_AppBarOverlay);

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

    private void checkForTripRunningStatus() {
        try {
            if (UIUtil.isInternetAvailable(getActivity())) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("branch_id", PreferencesManger.getStringFields(getActivity(), Constants.Pref.KEY_BRANCH_ID));
                RetrofitAPI.getInstance(getActivity()).getApi().getRunningTripStatus(params, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        UIUtil.stopProgressDialog(getActivity());
                        if (jsonObject == null) {
                            return;
                        }
                        Log.e("jsonObject", "jsonObject ---- " + jsonObject.toString());
                        try {
                            if (jsonObject.get("status").getAsInt() == Constants.TRIP_RUNNING) {
                                PreferencesManger.addBooleanFields(getActivity(), Constants.Pref.KEY_IS_TRIP_START, true);

                                try {
                                    busTripId = jsonObject.get("bus_trip_id").getAsInt();
                                    busno = jsonObject.get("bus_no").getAsString();
                                    TripId = jsonObject.get("trip_id").getAsInt();
                                    updateActiveTrip(busTripId, busno, TripId);

                                    PreferencesManger.addIntFields(getActivity(), Constants.Pref.TRIP_ID, TripId);
                                    PreferencesManger.addIntFields(getActivity(), Constants.Pref.KEY_BUS_TRIP_ID, busTripId);
                                    int stopId = jsonObject.get("current_stop_id").getAsInt();
                                    int squ = BusStop.find(BusStop.class, "stop_id = ? and bus_trip_id = ? ", new String[]{String.valueOf(stopId), String.valueOf(busTripId)}).get(0).getPriority();

                                    Log.e("DEMO", "busTripId : " + busTripId + "  stopID  : " + stopId + "  squ  : " + squ);
                                    PreferencesManger.addIntFields(getActivity(), Constants.Pref.KEY_CURRENT_STOP_SQU, squ + 1);

                                } catch (Exception e) {
                                    PreferencesManger.addIntFields(getActivity(), Constants.Pref.KEY_CURRENT_STOP_SQU, 1);
                                    e.printStackTrace();
                                }

                                PreferencesManger.addIntFields(getActivity(), Constants.Pref.KEY_TRIP_ID, jsonObject.get("trip_id").getAsInt());
//                            startActivity(new Intent(ItemOneFragment.getActivity(), TripActivity.class));
//                            overridePendingTransition(R.anim.right_to_center, R.anim.center_to_left);
//                            finish();
                                llTripRunning.setVisibility(View.VISIBLE);
                            } else {
                                llTripRunning.setVisibility(View.GONE);
                            }
                            if (Constants.SUCCESS == jsonObject.get("status").getAsInt()) {
                                PreferencesManger.addBooleanFields(getActivity(), Constants.Pref.KEY_IS_TRIP_START, false);

                                Intent intentS = new Intent(getActivity(), LatLongUpdateService.class);
                                intentS.putExtra(Constants.CITY, "STOP");
                                startActivity(intentS);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getActivity(), "Server not responding, Please try after some time ", Toast.LENGTH_SHORT).show();
                        UIUtil.stopProgressDialog(getActivity());
                    }
                });
            } else {
                Toast.makeText(getActivity(), "Check Internet connection", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateActiveTrip(int busTripId, String busno, int tripId) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showTripRunningForChat() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_AppBarOverlay);

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
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
