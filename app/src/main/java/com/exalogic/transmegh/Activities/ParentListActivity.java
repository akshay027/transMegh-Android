/*
package com.exalogic.inmegh.driver.Activities;

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
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.exalogic.inmegh.driver.API.RetrofitAPI;
import com.exalogic.inmegh.driver.Adapters.ParentListRecyclerAdapter;
import com.exalogic.inmegh.driver.Models.Parent;
import com.exalogic.inmegh.driver.Models.ParentChatListResponse;
import com.exalogic.inmegh.driver.Models.database.MessageTemplate;
import com.exalogic.inmegh.driver.R;
import com.exalogic.inmegh.driver.Utility.Constants;
import com.exalogic.inmegh.driver.Utility.UIUtil;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ParentListActivity extends AppCompatActivity {

    private RecyclerView chatView;
    private ArrayList<Parent> chatArrayList, finalParentList;
    private ParentListRecyclerAdapter adapter;
    private EditText etSearch;
    private String searchStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        etSearch = (EditText) findViewById(R.id.etSearch);

        chatView = (RecyclerView) findViewById(R.id.chatView);
        chatArrayList = new ArrayList<>();
        finalParentList = new ArrayList<>();
        chatView.setHasFixedSize(true);
        chatView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ParentListRecyclerAdapter(this, chatArrayList);
        chatView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        getDriverListFromServer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter.SetOnItemClickListener(new ParentListRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

//                Parent chatDriver = chatArrayList.get(position);
//                Intent intent = new Intent(ParentListActivity.this, ChatActivity.class);
//                intent.putExtra("name", chatDriver.getParentName());
//                intent.putExtra("id", chatDriver.getParentId());
//                startActivity(intent);
            }

            @Override
            public void onClickCall(View view, int position) {
                confirmationForMakeCall(position);
            }

            @Override
            public void onClickMessage(View view, int position) {
                openMessageSelector(position);
            }

            @Override
            public void onClickChat(View view, int position) {

            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    if (s.length() > 1) {
                        filterSearchData();
                        searchStr = s.toString();
                    }
                } else {
                    chatArrayList.clear();
                    chatArrayList.addAll(finalParentList);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
    private void filterSearchData() {
        searchStr = searchStr.toLowerCase();
        chatArrayList.clear();
        for (int i = 0; i < finalParentList.size(); i++) {
            if (finalParentList.get(i).getName().toLowerCase().contains(searchStr) || finalParentList.get(i).getParentName().toLowerCase().contains(searchStr)) {
                chatArrayList.add(finalParentList.get(i));
            }
        }
        adapter.notifyDataSetChanged();

    }

    private void getDriverListFromServer() {

        if (UIUtil.isInternetAvailable(this)) {
            UIUtil.startProgressDialog(this, "Please Wait...");
            RetrofitAPI.getInstance(this).getApi().getParentForDriver(new Callback<ParentChatListResponse>() {
                @Override
                public void success(ParentChatListResponse chatDriverResponse, Response response) {
                    UIUtil.stopProgressDialog(getApplicationContext());

                    if (chatDriverResponse.getStatus() == Constants.SUCCESS) {
                        chatArrayList.clear();
                        finalParentList.clear();
                        finalParentList.addAll(chatDriverResponse.getList());
                        chatArrayList.addAll(chatDriverResponse.getList());
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    UIUtil.stopProgressDialog(getApplicationContext());
                    Toast.makeText(getApplicationContext(), "Try After Some time", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Connect to internet.", Toast.LENGTH_LONG).show();
        }
    }


    private void confirmationForMakeCall(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final Parent parent = chatArrayList.get(position);
        builder.setTitle("Confirmation");
        final String number = parent.getPhone();

        String message = "Do you want to Call " + parent.getName() + " on this number " + number + " ?";
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

    private void makeCall(String number) {
        try {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openMessageSelector(final int position) {
        ArrayList<MessageTemplate> list = new ArrayList<>();
        list.addAll(MessageTemplate.listAll(MessageTemplate.class));
        final String[] items = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            items[i] = list.get(i).getTemplate();
        }

        new AlertDialog.Builder(this)
                .setSingleChoiceItems(items, 0, null)
                .setPositiveButton("send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        sentMessage(chatArrayList.get(position).getPhone(), items[selectedPosition]);
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

    private void sentMessage(String number, String message) {

        if (UIUtil.isInternetAvailable(this)) {
            UIUtil.startProgressDialog(this, "sending message...");
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("message", message);
            jsonObject.addProperty("mobile_no", number);

            Log.e("jsonObject", jsonObject.toString());

            RetrofitAPI.getInstance(this).getApi().setMessage(jsonObject, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject object, Response response) {
                    UIUtil.stopProgressDialog(getApplicationContext());
                    try {
                        Log.e("jsonObject", "object -- : " + object.toString());
                        if (object == null) {
                            Toast.makeText(ParentListActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (object.get("status").getAsInt() == Constants.SUCCESS) {
                            Toast.makeText(ParentListActivity.this, object.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ParentListActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    UIUtil.stopProgressDialog(getApplicationContext());
                    Toast.makeText(ParentListActivity.this, "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(ParentListActivity.this, "No internet", Toast.LENGTH_SHORT).show();
        }
    }

}
*/
