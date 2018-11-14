package com.exalogic.transmegh.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.exalogic.transmegh.API.RetrofitAPI;
import com.exalogic.transmegh.Adapters.StudentRecyclerAdapter;
import com.exalogic.transmegh.Database.PreferencesManger;
import com.exalogic.transmegh.Models.StudentResponse;
import com.exalogic.transmegh.Models.database.MessageTemplate;
import com.exalogic.transmegh.Models.database.Student;
import com.exalogic.transmegh.Utility.Constants;
import com.exalogic.transmegh.Utility.UIUtil;
import com.exalogic.transmegh.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class StudentListActivity extends AppCompatActivity implements View.OnClickListener {

    private int busAssignId, busTripId;
    private RecyclerView studentRecyclerView;
    private ArrayList<Student> studentArrayList;
    private StudentRecyclerAdapter adapter;
    private String number;
    private LinearLayout llAttendance, tvError;
    private CheckBox allCheckIn, allCheckOut;
    private boolean isTripRunning, isDrop;
    private boolean isActiveTrip;
    private boolean checkOutAll, checkInAll;
    //private ArrayList mobilelist;
    private JsonArray allNoList;
    private ImageView ivMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_list);
        Intent intent = getIntent();
        try {
            ((TextView) findViewById(R.id.tvBusStopName)).setText(intent.getStringExtra(Constants.BUS_STOP_NAME));
            ((TextView) findViewById(R.id.tvBusStopAddress)).setText(intent.getStringExtra(Constants.BUS_STOP_Address));

            busAssignId = intent.getIntExtra(Constants.BUS_RUNNING_ID, 0);
            busTripId = intent.getIntExtra(Constants.BUs_TRIP_ID, 0);

            ivMessage = (ImageView) findViewById(R.id.ivMessage);
            //mobilelist = new ArrayList();
            llAttendance = (LinearLayout) findViewById(R.id.llAttendance);
            tvError = (LinearLayout) findViewById(R.id.tvError);

            allCheckIn = (CheckBox) findViewById(R.id.checkIn);
            allCheckOut = (CheckBox) findViewById(R.id.checkOut);
            allNoList = new JsonArray();
            allCheckOut.setOnClickListener(this);
            allCheckIn.setOnClickListener(this);

            isTripRunning = PreferencesManger.getBooleanFields(this, Constants.Pref.KEY_IS_TRIP_START);
            isDrop = PreferencesManger.getBooleanFields(this, Constants.Pref.KEY_IS_DROP);
            isActiveTrip = PreferencesManger.getBooleanFields(this, Constants.Pref.KEY_IS_CURRENT);

//            boolean b = isTripRunning ? (isDrop ? false : true) : (isDrop) ? true : false;
//            b = isDrop ? true : isTripRunning;


            Log.e("drop check in :::::::::::  ", String.valueOf(isTripRunning));
            if (isTripRunning) {
                if (isDrop)
                    llAttendance.setVisibility(View.VISIBLE);
                allCheckIn.setVisibility((isDrop ? true : isTripRunning) ? View.VISIBLE : View.INVISIBLE);
                allCheckOut.setVisibility((isDrop && isTripRunning) ? View.VISIBLE : View.INVISIBLE);
            } else {
                llAttendance.setVisibility(View.GONE);
            }

           /* Log.e("auhyvahva    :", String.valueOf(isDrop));
            if (isTripRunning) {
                llAttendance.setVisibility(View.VISIBLE);
                if (isDrop) {
                    allCheckIn.setVisibility(View.GONE);
                    allCheckOut.setVisibility(View.VISIBLE);
                } else {
                    allCheckIn.setVisibility(View.VISIBLE);
                    allCheckOut.setVisibility(View.GONE);
                }

            } else {
                llAttendance.setVisibility(View.GONE);
            }*/
            boolean isTripRun = PreferencesManger.getBooleanFields(this, Constants.Pref.KEY_IS_TRIP_START);
            boolean isDrop = PreferencesManger.getBooleanFields(this, Constants.Pref.KEY_IS_DROP);
            studentRecyclerView = (RecyclerView) findViewById(R.id.recycleStudent);
            studentRecyclerView.setHasFixedSize(true);
            studentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            studentArrayList = new ArrayList<>();
            adapter = new StudentRecyclerAdapter(getBaseContext(), this, studentArrayList, isTripRun, isDrop);
            studentRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            getStudentForStop();

            adapter.SetOnItemClickListener(new StudentRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                }

                @Override
                public void onMessage(View view, int position) {
                    openMessageSelector(false, position);
                }

                @Override
                public void onCall(View view, int position) {
                    confirmationForMakeCall(position);
                }

                @Override
                public void onCheckIn(View view, int position) {
                    if (PreferencesManger.getBooleanFields(getApplicationContext(), Constants.Pref.KEY_IS_TRIP_START)) {
                        markAttendance(true, false, position);
                    } else {
                        Student student = studentArrayList.get(position);
                        student.setCheckIn(!student.isCheckIn());
                        if (student.isCheckIn()) {
                            student.setBusAssignId(busAssignId);
                            student.setBusTripId(busTripId);
                            student.save();
                            Log.e("Student", "----------Save : count : " + student.getStudentId());
                        } else {
                            int deleteStud = Student.deleteAll(Student.class, "student_id = ? ", new String[]{String.valueOf(student.getStudentId())});
                            Log.e("Student", "----------Delete : count : " + deleteStud);
                        }
                        studentArrayList.set(position, student);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCheckOut(View view, int position) {
                    if (PreferencesManger.getBooleanFields(getApplicationContext(), Constants.Pref.KEY_IS_TRIP_START)) {
                        markAttendance(false, false, position);
                    }
                }

            });

            ivMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openMessageSelector(true, 0);
                }
            });

        } catch (
                Exception e)

        {
            e.printStackTrace();
        }

        getSupportActionBar().

                setDisplayHomeAsUpEnabled(true);

    }

    private void checkForAllCheckIn() {
        checkInAll = true;
        for (int i = 0; i < studentArrayList.size(); i++) {
            if (!studentArrayList.get(i).isCheckIn()) {
                checkInAll = false;
            }
        }
        allCheckIn.setChecked(checkInAll);
    }

    private void checkForAllCheckOut() {
        checkOutAll = true;
        for (int i = 0; i < studentArrayList.size(); i++) {
            if (!studentArrayList.get(i).isCheckOut()) {
                checkOutAll = false;
            }
        }
        allCheckOut.setChecked(checkOutAll);
    }

    private void markAttendance(final boolean b, final boolean markAll, final int position) {
        try {
            if (UIUtil.isInternetAvailable(this)) {
                UIUtil.startProgressDialog(this, "Marking attendance...");
                final Student student = studentArrayList.get(position);
                final JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("student_id", String.valueOf(markAll ? getAllStudentIds() : student.getStudentId()));
                jsonObject.addProperty("trip_id", PreferencesManger.getIntFields(this, Constants.Pref.KEY_TRIP_ID));
                jsonObject.addProperty("branch_id", PreferencesManger.getStringFields(getApplicationContext(), Constants.Pref.KEY_BRANCH_ID));

                JsonObject object = new JsonObject();
                if (b) {
                    object.addProperty("check_in", markAll ? !checkInAll : !student.isCheckIn());
                } else {
                    object.addProperty("check_out", markAll ? !checkOutAll : !student.isCheckOut());
                }
                jsonObject.add("attendance", object);

                Log.e("jsonObject", jsonObject.toString());

                RetrofitAPI.getInstance(this).getApi().markAttendance(jsonObject, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject object, Response response) {
                        UIUtil.stopProgressDialog(getApplicationContext());
                        try {
                            Log.e("jsonObject", "object -- : " + object.toString());
                            if (object == null) {
                                Toast.makeText(StudentListActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (object.get("status").getAsInt() == Constants.SUCCESS) {
                                if (markAll) {
                                    getStudentForStop();
                                } else {
                                    if (b) {
                                        student.setCheckIn(!student.isCheckIn());
                                    } else {
                                        student.setCheckOut(!student.isCheckOut());
                                    }
                                    studentArrayList.set(position, student);
                                    adapter.notifyDataSetChanged();
                                }
                                Toast.makeText(StudentListActivity.this, object.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(StudentListActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        UIUtil.stopProgressDialog(getApplicationContext());
                        Toast.makeText(StudentListActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(StudentListActivity.this, "No internet", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void confirmationForMakeCall(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final Student student = studentArrayList.get(position);
        builder.setTitle("Confirmation");

        final String number = TextUtils.isEmpty(student.getPhone()) ? (TextUtils.isEmpty(student.getFatherNo()) ?
                (TextUtils.isEmpty(student.getMotherNo()) ? (TextUtils.isEmpty(student.getGuardianNo()) ? "NA" : student.getGuardianNo()) : student.getMotherNo())
                : student.getFatherNo()) : student.getFatherNo();

        String message = "Call " + student.getName() + " ?";
        builder.setMessage(message);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                makeCall(number);
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

    private void sentMessageConfirmation(final int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);

        final Student student = studentArrayList.get(position);

        final String number = TextUtils.isEmpty(student.getPhone()) ? (TextUtils.isEmpty(student.getFatherNo()) ?
                (TextUtils.isEmpty(student.getMotherNo()) ? (TextUtils.isEmpty(student.getGuardianNo()) ? "NA" : student.getGuardianNo()) : student.getMotherNo())
                : student.getFatherNo()) : student.getPhone();

        alert.setMessage("Student Name : " + student.getName() + "\n");
        alert.setTitle("Confirmation");
        edittext.setHint("Enter your message here");
        alert.setView(edittext);

        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String YouEditTextValue = edittext.getText().toString();
                sentMessage(number, YouEditTextValue);
            }
        });

        alert.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        alert.show();
    }

    private void openMessageSelector(final boolean allStudent, int position) {
        //mobilelist.clear();
        allNoList=new JsonArray();
        ArrayList<MessageTemplate> list = new ArrayList<>();
        list.addAll(MessageTemplate.listAll(MessageTemplate.class));
        final String[] items = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            items[i] = list.get(i).getTemplate();
        }
        if (!allStudent) {

            final Student student = studentArrayList.get(position);
            allNoList.add(new JsonPrimitive(TextUtils.isEmpty(student.getPhone()) ? (TextUtils.isEmpty(student.getFatherNo()) ?
                    (TextUtils.isEmpty(student.getMotherName()) ? (TextUtils.isEmpty(student.getGuardianNo()) ? "NA" : student.getGuardianNo()) : student.getMotherNo())
                    : student.getFatherNo()) : student.getPhone()));
           /* //mobilelist.add(TextUtils.isEmpty(student.getPhone()) ? (TextUtils.isEmpty(student.getFatherNo()) ?
                    (TextUtils.isEmpty(student.getMotherName()) ? (TextUtils.isEmpty(student.getGuardianNo()) ? "NA" : student.getGuardianNo()) : student.getMotherNo())
                    : student.getFatherNo()) : student.getPhone());*/
            number = TextUtils.isEmpty(student.getPhone()) ? (TextUtils.isEmpty(student.getFatherNo()) ?
                    (TextUtils.isEmpty(student.getMotherName()) ? (TextUtils.isEmpty(student.getGuardianNo()) ? "NA" : student.getGuardianNo()) : student.getMotherNo())
                    : student.getFatherNo()) : student.getPhone();
        }
        new AlertDialog.Builder(this)
                .setSingleChoiceItems(items, 0, null).setTitle("Select Message :")
                .setPositiveButton("send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        if (allStudent) {
                            sentMessage(getAllStudentNumber(), items[selectedPosition]);
                        } else {
                            sentMessage(String.valueOf(allNoList), items[selectedPosition]);
                        }
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
                sentMessage(getAllStudentNumber(), YouEditTextValue);
            }
        });

        alert.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        alert.show();
    }


    private String getAllStudentNumber() {
        String number = "";

        for (int i = 0; i < studentArrayList.size(); i++) {
            Student student = studentArrayList.get(i);
            allNoList.add(new JsonPrimitive((TextUtils.isEmpty(student.getPhone()) ? (TextUtils.isEmpty(student.getFatherNo()) ?
                    (TextUtils.isEmpty(student.getMotherNo()) ? (TextUtils.isEmpty(student.getGuardianNo()) ? "NA" : student.getGuardianNo()) : student.getMotherNo())
                    : student.getFatherNo()) : student.getPhone())));
        /*    mobilelist.add((TextUtils.isEmpty(student.getPhone()) ? (TextUtils.isEmpty(student.getFatherNo()) ?
                    (TextUtils.isEmpty(student.getMotherNo()) ? (TextUtils.isEmpty(student.getGuardianNo()) ? "NA" : student.getGuardianNo()) : student.getMotherNo())
                    : student.getFatherNo()) : student.getPhone()));*/
            number = number + (TextUtils.isEmpty(student.getPhone()) ? (TextUtils.isEmpty(student.getFatherNo()) ?
                    (TextUtils.isEmpty(student.getMotherNo()) ? (TextUtils.isEmpty(student.getGuardianNo()) ? "NA" : student.getGuardianNo()) : student.getMotherNo())
                    : student.getFatherNo()) : student.getPhone()) + ",";
        }

        return number;
    }

    private String getAllStudentIds() {
        String number = "";
        for (int i = 0; i < studentArrayList.size(); i++) {
            Student student = studentArrayList.get(i);
            number = number + studentArrayList.get(i).getStudentId() + ",";
        }

        return number;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void sentMessage(String number, String message) {
        try {
            if (UIUtil.isInternetAvailable(this)) {
                UIUtil.startProgressDialog(this, "sending message...");
                final JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("message", message);
                jsonObject.add("mobile_no", allNoList);
                jsonObject.addProperty("branch_id", PreferencesManger.getStringFields(getApplicationContext(), Constants.Pref.KEY_BRANCH_ID));
                jsonObject.addProperty("user_type", PreferencesManger.getStringFields(getApplicationContext(), Constants.Pref.KEY_USER_TYPE));

                Log.e("jsonObject", jsonObject.toString());

                RetrofitAPI.getInstance(this).getApi().setMessage(jsonObject, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject object, Response response) {
                        UIUtil.stopProgressDialog(getApplicationContext());
                        try {
                            Log.e("jsonObject", "object -- : " + object.toString());
                            if (object == null) {
                                Toast.makeText(StudentListActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (object.get("status").getAsInt() == Constants.SUCCESS) {
                                Toast.makeText(StudentListActivity.this, object.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(StudentListActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        UIUtil.stopProgressDialog(getApplicationContext());
                        Toast.makeText(StudentListActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(StudentListActivity.this, "No internet", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getStudentForStop() {
        try {
            if (UIUtil.isInternetAvailable(this)) {

                UIUtil.startProgressDialog(this, "Getting Student details.....");
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("bus_assign_id", String.valueOf(busAssignId));
                jsonObject.addProperty("branch_id", PreferencesManger.getStringFields(getApplicationContext(), Constants.Pref.KEY_BRANCH_ID));

                if (PreferencesManger.getBooleanFields(this, Constants.Pref.KEY_IS_TRIP_START) && PreferencesManger.getBooleanFields(this, Constants.Pref.KEY_IS_CURRENT)) {
                    jsonObject.addProperty("trip_id", PreferencesManger.getIntFields(this, Constants.Pref.KEY_TRIP_ID));
                    jsonObject.addProperty("flag", true);
                }

                RetrofitAPI.getInstance(this).getApi().getStudentListForBusStop(jsonObject, new Callback<StudentResponse>() {
                    @Override
                    public void success(StudentResponse studentResponse, Response response) {
                        try {
                            UIUtil.stopProgressDialog(getApplicationContext());
                            if (studentResponse.getStatus() == Constants.SUCCESS) {
                                studentArrayList.clear();
                                studentArrayList.addAll(studentResponse.getStudents());
                                if (studentArrayList.size() <= 0) {
                                    tvError.setVisibility(View.VISIBLE);
                                    studentRecyclerView.setVisibility(View.GONE);
                                    ivMessage.setVisibility(View.INVISIBLE);
                                } else {
                                    tvError.setVisibility(View.GONE);
                                    studentRecyclerView.setVisibility(View.VISIBLE);
                                    ivMessage.setVisibility(View.VISIBLE);
                                }
                                if (!isActiveTrip)
                                    checkForLocal();
                                adapter.notifyDataSetChanged();
                                checkForAllCheckIn();
                                checkForAllCheckOut();

                            } else {
                                Toast.makeText(getApplicationContext(), studentResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        UIUtil.stopProgressDialog(getApplicationContext());
                        Toast.makeText(getApplicationContext(), "Something went wrong, server not responding", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "No internet, Please Enable Internet", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkForLocal() {
        for (int i = 0; i < studentArrayList.size(); i++) {
            Student student = studentArrayList.get(i);
            try {
                student.setCheckIn(Student.find(Student.class, "student_id = ?", new String[]{String.valueOf(student.getStudentId())}).get(0).isCheckIn());
                studentArrayList.set(i, student);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void makeCall(String number) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkIn:
                if (isActiveTrip) {
                    markAttendance(true, true, 0);
                } else {
                    saveInDB();
                }
                break;
            case R.id.checkOut:
                markAttendance(false, true, 0);
                break;
        }

    }

    private void saveInDB() {
        try {
            for (int i = 0; i < studentArrayList.size(); i++) {
                Student student = studentArrayList.get(i);
                student.setBusTripId(busTripId);
                student.setBusAssignId(busAssignId);
                student.setCheckIn(allCheckIn.isChecked());
                if (allCheckIn.isChecked())
                    student.save();
                studentArrayList.set(i, student);
            }
            if (!allCheckIn.isChecked()) {
                int deleteCount = Student.deleteAll(Student.class, " bus_assign_id = ? ", new String[]{String.valueOf(busAssignId)});
                Log.e("delete", "Delete-------------------------Count : " + deleteCount);
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
