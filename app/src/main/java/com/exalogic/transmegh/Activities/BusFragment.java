/*
 * Copyright (c) 2017. Truiton (http://www.truiton.com/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.exalogic.transmegh.API.RetrofitAPI;
import com.exalogic.transmegh.Database.PreferencesManger;
import com.exalogic.transmegh.Models.Bus;
import com.exalogic.transmegh.Models.BusResponse;
import com.exalogic.transmegh.R;
import com.exalogic.transmegh.Utility.Constants;
import com.exalogic.transmegh.Utility.UIUtil;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BusFragment extends Fragment {


    TextView tvBusNumber, tvOwner, tvCapacity, tvMake, tvYearOfManufacture, tvRegNo, tvPermitUpto,
            tvTaxPaidUpto, tvinsurance_upto, tvInsuranceCompany, tvBrealFitnessUpto, tvpollution_upto, tvRCDetail;
    private ArrayAdapter<String> spinneradapter;
    private ArrayList<String> busIdList;
    private ArrayList<Bus> busArrayList;
    private String selectBusID;
    private Spinner busspinner;
    private ArrayList<Bus> demoArraylist;
    private LinearLayout busDetailsView, tvError;

    public static BusFragment newInstance() {
        BusFragment fragment = new BusFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_three, container, false);

        busDetailsView = (LinearLayout) view.findViewById(R.id.busDetailsView);
        tvError = (LinearLayout) view.findViewById(R.id.tvError);

        tvBusNumber = (TextView) view.findViewById(R.id.tvBusNumber);
        busspinner = (Spinner) view.findViewById(R.id.busspinner);
        tvOwner = (TextView) view.findViewById(R.id.tvOwner);
        tvCapacity = (TextView) view.findViewById(R.id.tvCapacity);
        tvMake = (TextView) view.findViewById(R.id.tvMake);
        tvYearOfManufacture = (TextView) view.findViewById(R.id.tvYearOfManufacture);
        tvRegNo = (TextView) view.findViewById(R.id.tvRegNo);
        tvPermitUpto = (TextView) view.findViewById(R.id.tvPermitUpto);
        tvTaxPaidUpto = (TextView) view.findViewById(R.id.tvTaxPaidUpto);
        tvinsurance_upto = (TextView) view.findViewById(R.id.tvinsurance_upto);
        tvInsuranceCompany = (TextView) view.findViewById(R.id.tvInsuranceCompany);
        tvBrealFitnessUpto = (TextView) view.findViewById(R.id.tvBrealFitnessUpto);
        tvpollution_upto = (TextView) view.findViewById(R.id.tvpollution_upto);
        tvRCDetail = (TextView) view.findViewById(R.id.tvRCDetail);

        busIdList = new ArrayList<>();
        demoArraylist = new ArrayList<>();
        busArrayList = new ArrayList<>();
        spinneradapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, busIdList);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        busspinner.setAdapter(spinneradapter);

        busspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectBusID = busIdList.get(position);
                Log.e("selectposition", "====" + selectBusID);
                updateBusDetailsOnUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        GetBusInfo();
        return view;
    }


    private void GetBusInfo() {
        try {

            if (UIUtil.isInternetAvailable(getActivity())) {
                UIUtil.startProgressDialog(getActivity(), "Getting trip data...");

                Map<String, String> params = new HashMap<String, String>();
                params.put("branch_id", PreferencesManger.getStringFields(getActivity(), Constants.Pref.KEY_BRANCH_ID));
                RetrofitAPI.getInstance(getActivity()).getApi().getBusDetails(params, new Callback<BusResponse>() {
                    @Override
                    public void success(BusResponse busResponse, Response response) {
                        try {
                            UIUtil.stopProgressDialog(getActivity());
                            if (busResponse == null)
                                return;
                            if (busResponse.getStatus() == Constants.SUCCESS) {
                                UIUtil.stopProgressDialog(getActivity());

                                demoArraylist.addAll(busResponse.getBus());
                                if (demoArraylist.size() <= 0) {
                                    tvError.setVisibility(View.VISIBLE);
                                    busDetailsView.setVisibility(View.GONE);

                                   /* tvBusNumber.setText("NA");

                                    tvOwner.setText("NA");

                                    tvCapacity.setText("NA");

                                    tvMake.setText("NA");

                                    tvYearOfManufacture.setText("NA");

                                    tvRegNo.setText("NA");

                                    tvPermitUpto.setText("NA");

                                    tvTaxPaidUpto.setText("NA");

                                    tvinsurance_upto.setText("NA");

                                    tvInsuranceCompany.setText("NA");

                                    tvBrealFitnessUpto.setText("NA");

                                    tvpollution_upto.setText("NA");

                                    tvRCDetail.setText("NA");*/


                                } else {
                                    tvError.setVisibility(View.GONE);
                                    busDetailsView.setVisibility(View.VISIBLE);

                                    updateDropDown(busResponse.getBus());
                                }
                            } else {
                                Toast.makeText(getActivity(), "" + busResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        UIUtil.stopProgressDialog(getActivity());

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

    private void updateDropDown(List<Bus> bus) {
        try {
            busArrayList.clear();
            busArrayList.addAll(bus);

            Log.e("bus", "Bus list : " + busArrayList.toString());
            busIdList.clear();
            for (int i = 0; i < busArrayList.size(); i++) {
                busIdList.add(busArrayList.get(i).getBusNo());
            }
            spinneradapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateBusDetailsOnUI() {
        for (int i = 0; i < busArrayList.size(); i++) {
            if (busArrayList.get(i).getBusNo().equalsIgnoreCase(selectBusID)) {
                bindData(busArrayList.get(i));
                break;
            }
        }
    }


    private void bindData(Bus bus) {

        try {
            if (TextUtils.isEmpty(bus.getBusNo())) {
                tvBusNumber.setText("NA");
            } else {
                tvBusNumber.setText(bus.getBusNo());
            }
            if (TextUtils.isEmpty(bus.getOwner())) {
                tvOwner.setText("NA");
            } else {
                tvOwner.setText(bus.getOwner());
            }
            if (TextUtils.isEmpty(String.valueOf(bus.getCapacity()))) {
                tvCapacity.setText("NA");
            } else {
                tvCapacity.setText(String.valueOf(bus.getCapacity()));
            }
            if (TextUtils.isEmpty(bus.getMake())) {
                tvMake.setText("NA");
            } else {
                tvMake.setText(bus.getMake());

            }
            if (TextUtils.isEmpty(bus.getYearOfManufacture())) {
                tvYearOfManufacture.setText("NA");
            } else {
                tvYearOfManufacture.setText(bus.getYearOfManufacture());
            }
            if (TextUtils.isEmpty(bus.getRegistrationNo())) {
                tvRegNo.setText("NA");
            } else {
                tvRegNo.setText(bus.getRegistrationNo());
            }
            if (TextUtils.isEmpty(bus.getPermitUpto())) {
                tvPermitUpto.setText("NA");
            } else {
                tvPermitUpto.setText(bus.getPermitUpto());
            }
            if (TextUtils.isEmpty(bus.getTaxPaidUpto())) {
                tvTaxPaidUpto.setText("NA");
            } else {
                tvTaxPaidUpto.setText(bus.getTaxPaidUpto());
            }
            if (TextUtils.isEmpty(bus.getInsuranceUpto())) {
                tvinsurance_upto.setText("NA");
            } else {
                tvinsurance_upto.setText(bus.getInsuranceUpto());
            }
            if (TextUtils.isEmpty(bus.getInsuranceCompany())) {
                tvInsuranceCompany.setText("NA");
            } else {
                tvInsuranceCompany.setText(bus.getInsuranceCompany());
            }
            if (TextUtils.isEmpty(bus.getBreakFitnessUpto())) {
                tvBrealFitnessUpto.setText("NA");
            } else {
                tvBrealFitnessUpto.setText(bus.getBreakFitnessUpto());
            }
            if (TextUtils.isEmpty(bus.getPollutionUpto())) {
                tvpollution_upto.setText("NA");
            } else {
                tvpollution_upto.setText(bus.getPollutionUpto());
            }
            if (TextUtils.isEmpty(bus.getRegistrationNo())) {
                tvRCDetail.setText("NA");
            } else {
                tvRCDetail.setText(bus.getRegistrationNo());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


