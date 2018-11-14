//package com.exalogic.inmegh.driver.Activities;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import com.exalogic.inmegh.driver.API.RetrofitAPI;
//import com.exalogic.inmegh.driver.Adapters.StudentStopRecyclerAdapter;
//import com.exalogic.inmegh.driver.Models.Route;
//import com.exalogic.inmegh.driver.Models.RouteResponse;
//import com.exalogic.inmegh.driver.Models.database.Student;
//import com.exalogic.inmegh.driver.R;
//import com.exalogic.inmegh.driver.Utility.Constants;
//import com.exalogic.inmegh.driver.Utility.UIUtil;
//import com.google.gson.JsonObject;
//
//import java.util.ArrayList;
//
//import retrofit.Callback;
//import retrofit.RetrofitError;
//import retrofit.client.Response;
//
//public class StudentStopActivity extends AppCompatActivity implements View.OnClickListener {
//
//    private ArrayList<Route> arrayListOnward, arrayListReturn, finalArrayList;
//    private StudentStopRecyclerAdapter adapterOnward, adapterReturn;
//    private RecyclerView recyclerOnward, recyclerReturn;
//    private ImageView ivMessageReturn, ivMessageOnward;
//
//    private ProgressBar progressBar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_student_stop);
//
//        progressBar = (ProgressBar) findViewById(R.id.progress);
//
//        recyclerOnward = (RecyclerView) findViewById(R.id.recycleOnward);
//        recyclerReturn = (RecyclerView) findViewById(R.id.recycleReturn);
//
//        ivMessageOnward = (ImageView) findViewById(R.id.ivMessageOnward);
//        ivMessageReturn = (ImageView) findViewById(R.id.ivMessageReturn);
//
//        ivMessageOnward.setOnClickListener(this);
//        ivMessageReturn.setOnClickListener(this);
//
//        recyclerOnward.setHasFixedSize(true);
//        recyclerReturn.setHasFixedSize(true);
//
//        recyclerOnward.setLayoutManager(new LinearLayoutManager(this));
//        recyclerReturn.setLayoutManager(new LinearLayoutManager(this));
//
//        arrayListOnward = new ArrayList<>();
//        arrayListReturn = new ArrayList<>();
//        finalArrayList = new ArrayList<>();
//
//        adapterOnward = new StudentStopRecyclerAdapter(this, arrayListOnward);
//        adapterReturn = new StudentStopRecyclerAdapter(this, arrayListReturn);
//
//
//        recyclerOnward.setAdapter(adapterOnward);
//        recyclerReturn.setAdapter(adapterReturn);
//
//
//        adapterOnward.SetOnItemClickListener(new StudentStopRecyclerAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Intent intent = new Intent(StudentStopActivity.this, StudentDetailsActivity.class);
//                intent.putExtra(Constants.ONWARD, arrayListOnward.get(position).getId());
//                intent.putExtra(Constants.RETURN, arrayListOnward.get(position).getRoutingPlaceTimingId());
//                intent.putExtra(Constants.CITY, arrayListOnward.get(position).getStopName());
//                startActivity(intent);
//            }
//        });
//
//        adapterReturn.SetOnItemClickListener(new StudentStopRecyclerAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Intent intent = new Intent(StudentStopActivity.this, StudentDetailsActivity.class);
//                intent.putExtra(Constants.ONWARD, arrayListReturn.get(position).getId());
//                intent.putExtra(Constants.RETURN, arrayListReturn.get(position).getRoutingPlaceTimingId());
//                intent.putExtra(Constants.CITY, arrayListReturn.get(position).getStopName());
//                startActivity(intent);
//            }
//        });
//
//        GetDataFromServer();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        if (id == android.R.id.home) {
//            finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.ivMessageOnward:
//                sentMessageConfirmation(true);
//                break;
//
//            case R.id.ivMessageReturn:
//                sentMessageConfirmation(true);
//                break;
//
//        }
//
//    }
//
//    private void sentMessageConfirmation(final boolean type) {
//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//        final EditText edittext = new EditText(this);
//
//
//        alert.setMessage("Your message  \n");
//        alert.setTitle("Confirmation");
//
//        edittext.setHint("Enter your message here");
////        if (!TextUtils.isEmpty(studentLeave.getAcknowledgement()))
////            edittext.setText("" + studentLeave.getAcknowledgement());
//        alert.setView(edittext, 40, 2, 40, 2);
//
//        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                String YouEditTextValue = edittext.getText().toString();
//                dialog.dismiss();
//                sentMessage(type, YouEditTextValue);
//            }
//        });
//
//        alert.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                dialog.dismiss();
//                // what ever you want to do with No option.
//            }
//        });
//
//        alert.show();
//    }
//
//    private void sentMessage(boolean type, String youEditTextValue) {
//
//        if (UIUtil.isInternetAvailable(this)) {
//
//            progressBar.setVisibility(View.VISIBLE);
//
//            final JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("message_all", true);
//            if (type) {
//                jsonObject.addProperty("route_placing_id", arrayListOnward.get(0).getRoutingPlaceTimingId());
//            } else {
//                jsonObject.addProperty("route_placing_id", arrayListReturn.get(0).getRoutingPlaceTimingId());
//            }
//
//            jsonObject.addProperty("message", youEditTextValue);
//
//            Log.e("jsonObject", jsonObject.toString());
//
//            RetrofitAPI.getInstance(this).getApi().setMessage(jsonObject, new Callback<JsonObject>() {
//                @Override
//                public void success(JsonObject object, Response response) {
//                    try {
//
//                        Log.e("jsonObject", "object -- : " + object.toString());
//                        progressBar.setVisibility(View.GONE);
//                        if (object == null) {
//                            Toast.makeText(StudentStopActivity.this, "Something went worng, try after sometime...", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//
//                        if (object.get("status").getAsInt() == Constants.SUCCESS) {
//                            Toast.makeText(StudentStopActivity.this, object.get("message").getAsString(), Toast.LENGTH_SHORT).show();
//                        }
//
//                    } catch (Exception e) {
//                        progressBar.setVisibility(View.GONE);
//                        e.printStackTrace();
//                        Toast.makeText(StudentStopActivity.this, "Something went worng, try after sometime...", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(StudentStopActivity.this, "Something went worng, try after sometime...", Toast.LENGTH_SHORT).show();
//
//                }
//            });
//        } else {
//            Toast.makeText(StudentStopActivity.this, "No internet", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void GetDataFromServer() {
//
//        if (UIUtil.isInternetAvailable(this)) {
//            RetrofitAPI.getInstance(this).getApi().getMyRoute(new Callback<RouteResponse>() {
//                @Override
//                public void success(RouteResponse routeResponse, Response response) {
//                    if (routeResponse.getStatus() == Constants.SUCCESS) {
//                        finalArrayList.clear();
//                        finalArrayList.addAll(routeResponse.getRoute());
//                        attachedDataToGrid();
//
//                    } else {
//                        Toast.makeText(StudentStopActivity.this, "Something went worng, try after sometime...", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    Toast.makeText(StudentStopActivity.this, "Something went worng, try after sometime...", Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void attachedDataToGrid() {
//        for (int i = 0; i < finalArrayList.size(); i++) {
//            if (Constants.ONWARD.equalsIgnoreCase(finalArrayList.get(i).getType())) {
//                arrayListOnward.add(finalArrayList.get(i));
//            } else {
//                arrayListReturn.add(finalArrayList.get(i));
//            }
//        }
//
//        adapterOnward.notifyDataSetChanged();
//        adapterReturn.notifyDataSetChanged();
//    }
//
//
//}
