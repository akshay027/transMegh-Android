/*
 * Copyright (c) 2017. Truiton (http://www.truiton.com/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use getActivity() file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Mohit Gupt (https://github.com/mohitgupt)
 *
 */

package com.exalogic.transmegh.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.exalogic.transmegh.API.RetrofitAPI;
import com.exalogic.transmegh.Adapters.ParentListRecyclerAdapter;
import com.exalogic.transmegh.Database.PreferencesManger;
import com.exalogic.transmegh.Models.Parent;
import com.exalogic.transmegh.Models.ParentChatListResponse;
import com.exalogic.transmegh.Models.database.MessageTemplate;
import com.exalogic.transmegh.Utility.Constants;
import com.exalogic.transmegh.Utility.UIUtil;
import com.exalogic.transmegh.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ParentListFragment extends Fragment {

    private RecyclerView chatView;
    private ArrayList<Parent> chatArrayList, finalParentList;
    private ParentListRecyclerAdapter adapter;
    private EditText etSearch;
    private String searchStr = "";
    String number;
    private LinearLayout tvError;
    private JsonArray contactNo;


    public static ParentListFragment newInstance() {
        ParentListFragment fragment = new ParentListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_two, container, false);

        chatView = (RecyclerView) view.findViewById(R.id.chatView);
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        tvError = (LinearLayout) view.findViewById(R.id.tvError);
        chatArrayList = new ArrayList<>();
        finalParentList = new ArrayList<>();
        contactNo=new JsonArray();
        chatView.setHasFixedSize(true);
        chatView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new ParentListRecyclerAdapter(getContext(), getActivity(), chatArrayList);
        chatView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        getDriverListFromServer();


        adapter.SetOnItemClickListener(new ParentListRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

//                Parent chatDriver = chatArrayList.get(position);
//                Intent intent = new Intent(ParentListActivity.getActivity(), ChatActivity.class);
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

        return view;
    }

    private void filterSearchData() {
        try {
            searchStr = searchStr.toLowerCase();
            chatArrayList.clear();
            for (int i = 0; i < finalParentList.size(); i++) {
                if (finalParentList.get(i).getName().toLowerCase().contains(searchStr) ||
                        finalParentList.get(i).getFatherName().toLowerCase().contains(searchStr) ||
                        finalParentList.get(i).getMotherName().toLowerCase().contains(searchStr)
                        || finalParentList.get(i).getGuardianName().toLowerCase().contains(searchStr)) {
                    chatArrayList.add(finalParentList.get(i));
                }
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDriverListFromServer() {
        try {

            if (UIUtil.isInternetAvailable(getActivity())) {
                UIUtil.startProgressDialog(getActivity(), "Please Wait...");
                Map<String, String> params = new HashMap<String, String>();
                params.put("branch_id", PreferencesManger.getStringFields(getActivity(), Constants.Pref.KEY_BRANCH_ID));
                RetrofitAPI.getInstance(getActivity()).getApi().getParentForDriver(params, new Callback<ParentChatListResponse>() {
                    @Override
                    public void success(ParentChatListResponse chatDriverResponse, Response response) {
                        UIUtil.stopProgressDialog(getActivity());

                        if (chatDriverResponse.getStatus() == Constants.SUCCESS) {
                            UIUtil.stopProgressDialog(getActivity());
                            try {
                                chatArrayList.clear();
                                finalParentList.clear();
                                finalParentList.addAll(chatDriverResponse.getList());
                                chatArrayList.addAll(chatDriverResponse.getList());
                                if (finalParentList.size() <= 0) {
                                    tvError.setVisibility(View.VISIBLE);
                                    chatView.setVisibility(View.GONE);

                                } else {
                                    tvError.setVisibility(View.GONE);
                                    chatView.setVisibility(View.VISIBLE);
                                }

                                adapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                PreferencesManger.addIntFields(getActivity(), Constants.Pref.KEY_CURRENT_STOP_SQU, 1);
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        UIUtil.stopProgressDialog(getActivity());
                        Toast.makeText(getActivity(), "Try After Some time", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(getActivity(), "Connect to internet.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void confirmationForMakeCall(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final Parent parent = chatArrayList.get(position);
        builder.setTitle("Confirmation");

        number = parent.getPhone();
        if (TextUtils.isEmpty(parent.getPhone())) {
            number = parent.getFatherNo();
        } else if (TextUtils.isEmpty(parent.getFatherName()) || TextUtils.isEmpty(parent.getPhone())) {
            number = parent.getMotherNo();
        }
        String message = "Do you want to Call " + parent.getName() + " on number " + number + " ?";
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
            int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
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

        new AlertDialog.Builder(getActivity())
                .setSingleChoiceItems(items, 0, null)
                .setPositiveButton("send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        contactNo.add(new JsonPrimitive(chatArrayList.get(position).getPhone().toString()));
                        sentMessage(contactNo, items[selectedPosition]);
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

    private void sentMessage(JsonArray number, String message) {
        try {
            if (UIUtil.isInternetAvailable(getActivity())) {
                UIUtil.startProgressDialog(getActivity(), "sending message...");
                final JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("message", message);
                jsonObject.add("mobile_no", number);
                jsonObject.addProperty("branch_id", PreferencesManger.getStringFields(getActivity(), Constants.Pref.KEY_BRANCH_ID));
                jsonObject.addProperty("user_type", PreferencesManger.getStringFields(getActivity(), Constants.Pref.KEY_USER_TYPE));

                Log.e("jsonObject", jsonObject.toString());

                RetrofitAPI.getInstance(getActivity()).getApi().setMessage(jsonObject, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject object, Response response) {
                        UIUtil.stopProgressDialog(getActivity());
                        try {
                            Log.e("jsonObject", "object -- : " + object.toString());
                            if (object == null) {
                                UIUtil.stopProgressDialog(getActivity());
                                Toast.makeText(getActivity(), "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (object.get("status").getAsInt() == Constants.SUCCESS) {
                                UIUtil.stopProgressDialog(getActivity());
                                Toast.makeText(getActivity(), object.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        UIUtil.stopProgressDialog(getActivity());
                        Toast.makeText(getActivity(), "Something went wrong, try after sometime...", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(getActivity(), "No internet", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
