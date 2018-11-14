/*
package com.exalogic.inmegh.driver.Activities;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.exalogic.inmegh.driver.API.RetrofitAPI;
import com.exalogic.inmegh.driver.Models.Bus;
import com.exalogic.inmegh.driver.Models.BusResponse;
import com.exalogic.inmegh.driver.R;
import com.exalogic.inmegh.driver.Utility.Constants;
import com.exalogic.inmegh.driver.Utility.UIUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BusActivity extends AppCompatActivity {

    TextView tvBusNumber, tvOwner, tvCapacity, tvMake, tvYearOfManufacture, tvRegNo, tvPermitUpto,
            tvTaxPaidUpto, tvinsurance_upto, tvInsuranceCompany, tvBrealFitnessUpto, tvpollution_upto, tvRCDetail;
    private ArrayAdapter<String> spinneradapter;
    private ArrayList<String> busIdList;
    private ArrayList<Bus> busArrayList;
    private String selectBusID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);


        tvBusNumber = (TextView) findViewById(R.id.tvBusNumber);
        tvOwner = (TextView) findViewById(R.id.tvOwner);
        tvCapacity = (TextView) findViewById(R.id.tvCapacity);
        tvMake = (TextView) findViewById(R.id.tvMake);
        tvYearOfManufacture = (TextView) findViewById(R.id.tvYearOfManufacture);
        tvRegNo = (TextView) findViewById(R.id.tvRegNo);
        tvPermitUpto = (TextView) findViewById(R.id.tvPermitUpto);
        tvTaxPaidUpto = (TextView) findViewById(R.id.tvTaxPaidUpto);
        tvinsurance_upto = (TextView) findViewById(R.id.tvinsurance_upto);
        tvInsuranceCompany = (TextView) findViewById(R.id.tvInsuranceCompany);
        tvBrealFitnessUpto = (TextView) findViewById(R.id.tvBrealFitnessUpto);
        tvpollution_upto = (TextView) findViewById(R.id.tvpollution_upto);
        tvRCDetail = (TextView) findViewById(R.id.tvRCDetail);

        busArrayList = new ArrayList<>();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GetBusInfo();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_spinner, menu);
        MenuItem item = menu.findItem(R.id.spinner);
        busIdList = new ArrayList<>();
        final Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        spinneradapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, busIdList);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinneradapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectBusID = busIdList.get(position);
                Log.e("selectposition", "====" + selectBusID);
                updateBusDetailsOnUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return true;
    }


    private void GetBusInfo() {
        if (UIUtil.isInternetAvailable(this)) {
            UIUtil.startProgressDialog(this, "Getting trip data...");


            RetrofitAPI.getInstance(this).getApi().getBusDetails(new Callback<BusResponse>() {
                @Override
                public void success(BusResponse busResponse, Response response) {
                    UIUtil.stopProgressDialog(getApplicationContext());
                    if (busResponse == null)
                        return;
                    if (busResponse.getStatus() == Constants.SUCCESS) {
                        updateDropDown(busResponse.getBus());
                    } else {
                        Toast.makeText(getApplicationContext(), "" + busResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    UIUtil.stopProgressDialog(getApplicationContext());

                    Toast.makeText(getApplicationContext(), "Server is not responding, Try after some time.", Toast.LENGTH_SHORT).show();

                }
            });
        } else {

            Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateDropDown(List<Bus> bus) {
        busArrayList.clear();
        busArrayList.addAll(bus);

        Log.e("bus", "Bus list : " + busArrayList.toString());
        busIdList.clear();
        for (int i = 0; i < busArrayList.size(); i++) {
            busIdList.add(busArrayList.get(i).getBusNo());
        }
        spinneradapter.notifyDataSetChanged();

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
        tvBusNumber.setText(bus.getBusNo());
        tvOwner.setText(bus.getOwner());
        tvCapacity.setText(String.valueOf(bus.getCapacity()));
        tvMake.setText(bus.getMake());
        tvYearOfManufacture.setText(bus.getYearOfManufacture());
        tvRegNo.setText(bus.getRegistrationNo());
        tvPermitUpto.setText(bus.getPermitUpto());
        tvTaxPaidUpto.setText(bus.getTaxPaidUpto());
        tvinsurance_upto.setText(bus.getInsuranceUpto());
        tvInsuranceCompany.setText(bus.getInsuranceCompany());
        tvBrealFitnessUpto.setText(bus.getBreakFitnessUpto());
        tvpollution_upto.setText(bus.getPollutionUpto());
        tvRCDetail.setText(bus.getRegistrationNo());
    }

}
*/
