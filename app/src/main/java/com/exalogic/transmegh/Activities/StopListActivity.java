package com.exalogic.transmegh.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.exalogic.transmegh.API.RetrofitAPI;
import com.exalogic.transmegh.Adapters.BusStopRecyclerAdapter;
import com.exalogic.transmegh.Database.PreferencesManger;
import com.exalogic.transmegh.Models.database.BusStop;
import com.exalogic.transmegh.Models.database.MessageTemplate;
import com.exalogic.transmegh.Utility.Constants;
import com.exalogic.transmegh.Utility.UIUtil;
import com.exalogic.transmegh.R;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class StopListActivity extends AppCompatActivity implements View.OnClickListener {

    private int tripId;
    String busno;
    int tripstatus;
    private RecyclerView stopRecyclerView;
    private ArrayList<BusStop> stopArrayList;
    private BusStopRecyclerAdapter adapter;
    private TextView tvClickhere, tvbusno, tvComp;
    private LinearLayout tvError;
    private ImageView ivMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_list);

        Intent intent = getIntent();

        try {
            tvClickhere = (TextView) findViewById(R.id.tvClickhere);
            tvComp = (TextView) findViewById(R.id.tvComp);
            tvbusno = (TextView) findViewById(R.id.tvbusno);
            tvError = (LinearLayout) findViewById(R.id.tvError);

            ivMessage = (ImageView) findViewById(R.id.ivMessage);
            ((TextView) findViewById(R.id.tvTripName)).setText(intent.getStringExtra(Constants.TRIP_NAME));
            ((TextView) findViewById(R.id.tvStartTime)).setText(intent.getStringExtra(Constants.TRIP_START_TIME));
            ((TextView) findViewById(R.id.tvEndTime)).setText(intent.getStringExtra(Constants.TRIP_END_TIME));
            tripId = intent.getIntExtra(Constants.BUs_TRIP_ID, 0);
            busno = intent.getStringExtra("busno");
            tripstatus = intent.getIntExtra("tripstatus", 0);

            tvbusno.setText(busno);
            stopRecyclerView = (RecyclerView) findViewById(R.id.recycleStop);
            stopRecyclerView.setHasFixedSize(true);
            stopRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            stopArrayList = new ArrayList<>();
            adapter = new BusStopRecyclerAdapter(this, stopArrayList);
            stopRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            stopArrayList.addAll(BusStop.find(BusStop.class, "bus_trip_id = ? and busno = ? ", new String[]{String.valueOf(tripId), busno}));
            if (stopArrayList.size() <= 0) {
                tvError.setVisibility(View.VISIBLE);
                stopRecyclerView.setVisibility(View.GONE);
                ivMessage.setVisibility(View.INVISIBLE);
            } else {
                tvError.setVisibility(View.GONE);
                stopRecyclerView.setVisibility(View.VISIBLE);
                ivMessage.setVisibility(View.VISIBLE);
            }
            Animation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(1200); //You can manage the blinking time with this parameter
            anim.setStartOffset(20);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            tvClickhere.startAnimation(anim);

            Log.e("Data", "data  : " + stopArrayList.toString());
            adapter.notifyDataSetChanged();
            if (tripstatus == 0) {
                tvComp.setVisibility(View.VISIBLE);
                tvClickhere.setVisibility(View.GONE);

            } else {
                tvComp.setVisibility(View.GONE);
                tvClickhere.setVisibility(View.VISIBLE);

            }
            tvClickhere.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (PreferencesManger.getBooleanFields(getApplicationContext(), Constants.Pref.KEY_IS_TRIP_START)) {
                        if (PreferencesManger.getBooleanFields(getApplicationContext(), Constants.Pref.KEY_IS_CURRENT)) {
                            // startActivity(new Intent(StopListActivity.this, TripActivity.class).putExtra(Constants.BUs_TRIP_ID, tripId));
                            Intent intent = new Intent(getApplicationContext(), TripActivity.class);
                            intent.putExtra(Constants.BUs_TRIP_ID, tripId);
                            intent.putExtra("busno", busno);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), " Another trip is already running....", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        // startActivity(new Intent(StopListActivity.this, TripActivity.class).putExtra(Constants.BUs_TRIP_ID, tripId));
                        Intent intent = new Intent(getApplicationContext(), TripActivity.class);
                        intent.putExtra(Constants.BUs_TRIP_ID, tripId);
                        intent.putExtra("busno", busno);
                        startActivity(intent);
                    }
                }
            });

            ivMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openMessageSelector();
//                    sentMessageConfirmationForAllStudent();
                }
            });

            adapter.SetOnItemClickListener(new BusStopRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (PreferencesManger.getBooleanFields(getApplicationContext(), Constants.Pref.KEY_IS_TRIP_START)) {
                        if (PreferencesManger.getBooleanFields(getApplicationContext(), Constants.Pref.KEY_IS_CURRENT)) {
                            // startActivity(new Intent(StopListActivity.this, TripActivity.class).putExtra(Constants.BUs_TRIP_ID, tripId));
                            Intent intd = new Intent(StopListActivity.this, StudentListActivity.class);
                            intd.putExtra(Constants.BUS_STOP_NAME, stopArrayList.get(position).getStopName());
                            intd.putExtra(Constants.BUS_STOP_Address, stopArrayList.get(position).getAddress());
                            intd.putExtra(Constants.BUS_RUNNING_ID, stopArrayList.get(position).getBusAssignId());
                            intd.putExtra(Constants.BUs_TRIP_ID, tripId);
                            startActivity(intd);
                        } else {
                            Toast.makeText(getApplicationContext(), " Another trip is already running....", Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        // startActivity(new Intent(StopListActivity.this, TripActivity.class).putExtra(Constants.BUs_TRIP_ID, tripId));
                        Intent intd = new Intent(StopListActivity.this, StudentListActivity.class);
                        intd.putExtra(Constants.BUS_STOP_NAME, stopArrayList.get(position).getStopName());
                        intd.putExtra(Constants.BUS_STOP_Address, stopArrayList.get(position).getAddress());
                        intd.putExtra(Constants.BUS_RUNNING_ID, stopArrayList.get(position).getBusAssignId());
                        intd.putExtra(Constants.BUs_TRIP_ID, tripId);
                        startActivity(intd);
                    }
                }

                @Override
                public void onLocation(View view, int position) {

                    Intent intd = new Intent(StopListActivity.this, StopLocationActivity.class);
                    intd.putExtra(Constants.LAT, stopArrayList.get(position).getLatitude());
                    intd.putExtra(Constants.LNG, stopArrayList.get(position).getLongitude());
                    intd.putExtra(Constants.CITY, stopArrayList.get(position).getStopName());
                    startActivity(intd);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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


    private void sentMessageConfirmationForAllStudent() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);

        alert.setMessage("sent message to all student " + "\n");
        alert.setTitle("Confirmation");
        edittext.setHint("Enter your message here");
        alert.setView(edittext);

        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String YouEditTextValue = edittext.getText().toString();
                sentMessage(YouEditTextValue);
            }
        });

        alert.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        alert.show();
    }

    private void openMessageSelector() {
        final ArrayList<MessageTemplate> list = new ArrayList<>();
        list.addAll(MessageTemplate.listAll(MessageTemplate.class));
        final String[] items = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            items[i] = list.get(i).getTemplate();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(items, 0, null).setTitle("Select Message :")
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
                                Toast.makeText(StopListActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (object.get("status").getAsInt() == Constants.SUCCESS) {
                                Toast.makeText(StopListActivity.this, object.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(StopListActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        UIUtil.stopProgressDialog(getApplicationContext());
                        Toast.makeText(StopListActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(StopListActivity.this, "No internet", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkIn:
                break;
            case R.id.checkOut:
                break;
        }

    }
}
