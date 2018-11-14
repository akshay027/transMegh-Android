//package com.exalogic.inmegh.driver.Activities;
//
//import android.Manifest;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.net.Uri;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.exalogic.inmegh.driver.API.RetrofitAPI;
//import com.exalogic.inmegh.driver.Adapters.StudentDetailsRecyclerAdapter;
//import com.exalogic.inmegh.driver.Models.Leaves.StudentLeave;
//import com.exalogic.inmegh.driver.Models.database.Student;
//import com.exalogic.inmegh.driver.Models.StudentResponse;
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
//public class StudentDetailsActivity extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private ArrayList<Student> arrayList;
//
//    private StudentDetailsRecyclerAdapter adapter;
//    private int placeId, routPlaceId;
//    private String placeName;
//    private ProgressBar progressBar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_student_details);
//
//        progressBar = (ProgressBar) findViewById(R.id.progress);
//
//        recyclerView = (RecyclerView) findViewById(R.id.recyclerStudent);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        arrayList = new ArrayList<>();
//        adapter = new StudentDetailsRecyclerAdapter(this, arrayList);
//
//        recyclerView.setAdapter(adapter);
//
//        adapter.notifyDataSetChanged();
//
//        try {
//
//            Intent intent = getIntent();
//            placeId = intent.getIntExtra(Constants.ONWARD, -1);
//            routPlaceId = intent.getIntExtra(Constants.RETURN, -1);
//            placeName = intent.getStringExtra(Constants.CITY);
//
//            ((TextView) findViewById(R.id.tvTitle)).setText(placeName);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        adapter.SetOnItemClickListener(new StudentDetailsRecyclerAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//            }
//
//            @Override
//            public void onCallClick(View view, int position) {
//                confirmationForMakeCall(position);
//            }
//
//            @Override
//            public void onMessageClick(View view, int position) {
//                sentMessageConfirmation(position);
//            }
//        });
//
//
//        ((ImageView) findViewById(R.id.tvSentMessage)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sentMessageConfirmationForAllStudent();
//            }
//        });
//
//        GetData();
//
//
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
//
//    private void makeCall(String number) {
//        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
//        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
//        startActivity(intent);
//    }
//
//
//    private void sentMessage(String number, String message) {
//
//        if (UIUtil.isInternetAvailable(this)) {
//
//
//            progressBar.setVisibility(View.VISIBLE);
//
//            final JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("message", message);
//            jsonObject.addProperty("mobile_no", number);
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
//                            Toast.makeText(StudentDetailsActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//
//                        if (object.get("status").getAsInt() == Constants.SUCCESS) {
//                            Toast.makeText(StudentDetailsActivity.this, object.get("message").getAsString(), Toast.LENGTH_SHORT).show();
//                        }
//
//                    } catch (Exception e) {
//                        progressBar.setVisibility(View.GONE);
//                        e.printStackTrace();
//                        Toast.makeText(StudentDetailsActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(StudentDetailsActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
//
//                }
//            });
//        } else {
//            Toast.makeText(StudentDetailsActivity.this, "No internet", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//    private void sentMessageToAllStudent(String message) {
//
//        if (UIUtil.isInternetAvailable(this)) {
//
//
//            progressBar.setVisibility(View.VISIBLE);
//
//            final JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("message", message);
//            jsonObject.addProperty("route_placing_id", routPlaceId);
//            jsonObject.addProperty("place_id", placeId);
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
//                            Toast.makeText(StudentDetailsActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//
//                        if (object.get("status").getAsInt() == Constants.SUCCESS) {
//                            Toast.makeText(StudentDetailsActivity.this, object.get("message").getAsString(), Toast.LENGTH_SHORT).show();
//                        }
//
//                    } catch (Exception e) {
//                        progressBar.setVisibility(View.GONE);
//                        e.printStackTrace();
//                        Toast.makeText(StudentDetailsActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(StudentDetailsActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
//
//                }
//            });
//        } else {
//            Toast.makeText(StudentDetailsActivity.this, "No internet", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//    private void GetData() {
//
//        if (UIUtil.isInternetAvailable(this)) {
//            UIUtil.startProgressDialog(this, "Please wait, getting details...");
//
//            JsonObject jsonObject = new JsonObject();
//
//            jsonObject.addProperty("place_id", placeId);
//            jsonObject.addProperty("route_placing_id", routPlaceId);
//
//
//            RetrofitAPI.getInstance(this).getApi().getRouteStudent(jsonObject, new Callback<StudentResponse>() {
//                @Override
//                public void success(StudentResponse studentResponse, Response response) {
//
//                    UIUtil.stopProgressDialog(StudentDetailsActivity.this);
//                    if (studentResponse.getStatus() == Constants.SUCCESS) {
//
//                        arrayList.clear();
//                        arrayList.addAll(studentResponse.getStudent());
//                        adapter.notifyDataSetChanged();
//                    }
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    UIUtil.stopProgressDialog(StudentDetailsActivity.this);
//                    Toast.makeText(StudentDetailsActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            Toast.makeText(StudentDetailsActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//    private void confirmationForMakeCall(final int position) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
////        AlertDialog.Builder builder =
////                new AlertDialog.Builder(this, R.style.AppTheme_AppBarOverlay);
//        final Student student = arrayList.get(position);
//        builder.setTitle("Confirmation");
//        String message = "Do you want to Call " + student.getName() + " on this number " + student.getContactNo() + " ?";
//        builder.setMessage(message);
//        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                makeCall(student.getContactNo());
//
//            }
//        });
//        builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.show();
//    }
//
//    private void sentMessageConfirmation(final int position) {
//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//        final EditText edittext = new EditText(this);
//
//        final Student studentLeave = arrayList.get(position);
//
//        alert.setMessage("Student Name : " + studentLeave.getName() + "\n");
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
//                sentMessage(studentLeave.getContactNo(), YouEditTextValue);
//            }
//        });
//
//        alert.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                // what ever you want to do with No option.
//            }
//        });
//
//        alert.show();
//    }
//
//
//    private void sentMessageConfirmationForAllStudent() {
//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//        final EditText edittext = new EditText(this);
//
//
//        alert.setMessage("Sent message to all student" + "\n");
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
//                sentMessageToAllStudent(YouEditTextValue);
//            }
//        });
//
//        alert.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                // what ever you want to do with No option.
//            }
//        });
//
//        alert.show();
//    }
//
//
//}
