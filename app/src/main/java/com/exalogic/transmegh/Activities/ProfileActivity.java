/*
package com.exalogic.inmegh.driver.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.exalogic.inmegh.driver.API.RetrofitAPI;
import com.exalogic.inmegh.driver.Models.Profile;
import com.exalogic.inmegh.driver.Models.ProfileResponse;
import com.exalogic.inmegh.driver.R;
import com.exalogic.inmegh.driver.Utility.Constants;
import com.exalogic.inmegh.driver.Utility.UIUtil;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName, tvEmail, tvMobile, tvDOB, tvBloodGroup, tvGender, tvMartialStatus,
            tvEmpStatus, tvJoiningDate, tvLicense, tvPanCard, tvAadhaarCard;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvName = (TextView) findViewById(R.id.tvName);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvMobile = (TextView) findViewById(R.id.tvMobile);
        tvDOB = (TextView) findViewById(R.id.tvDOB);
        tvBloodGroup = (TextView) findViewById(R.id.tvBloodGroup);
        tvGender = (TextView) findViewById(R.id.tvGender);
        tvMartialStatus = (TextView) findViewById(R.id.tvMartialStatus);
        tvEmpStatus = (TextView) findViewById(R.id.tvEmpStatus);
        tvJoiningDate = (TextView) findViewById(R.id.tvJoiningDate);
        tvLicense = (TextView) findViewById(R.id.tvLicense);
        tvPanCard = (TextView) findViewById(R.id.tvPanCard);
        tvAadhaarCard = (TextView) findViewById(R.id.tvAadhaarCard);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getProfileDetails();

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    private void getProfileDetails() {
        if (UIUtil.isInternetAvailable(this)) {
            UIUtil.startProgressDialog(this, "Getting trip data...");


            RetrofitAPI.getInstance(this).getApi().getProfile(new Callback<ProfileResponse>() {
                @Override
                public void success(ProfileResponse profileResponse, Response response) {
                    UIUtil.stopProgressDialog(getApplicationContext());
                    if (profileResponse == null)
                        return;
                    if (profileResponse.getStatus() == Constants.SUCCESS) {
                        bindData(profileResponse.getProfile());
                    } else {
                        Toast.makeText(getApplicationContext(), "" + profileResponse.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void bindData(Profile profile) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (profile.getName() == "") {
            tvName.setText("N/A");
        } else {
            tvName.setText(profile.getName());
        }
        if (profile.getName() == "") {
            tvEmail.setText("N/A");
        } else {
            tvEmail.setText(profile.getEmail());
        }
        if (profile.getAadhaarCard() == "") {
            tvAadhaarCard.setText("N/A");
        } else {
            tvAadhaarCard.setText(profile.getAadhaarCard());
        }
        if (profile.getPanCard() == "") {
            tvPanCard.setText("N/A");
        } else {
            tvPanCard.setText(profile.getPanCard());
        }
        if (profile.getLicense() == "") {
            tvLicense.setText("N/A");
        } else {
            tvLicense.setText(profile.getLicense());
        }
        if (profile.getJoiningDate() == "") {
            tvJoiningDate.setText("N/A");
        } else {
            tvJoiningDate.setText(profile.getJoiningDate());
        }
        if (profile.getStatus() == "") {
            tvEmpStatus.setText("N/A");
        } else {
            tvEmpStatus.setText(profile.getStatus());
        }
        if (profile.getMartialStatus() == "") {
            tvMartialStatus.setText("N/A");
        } else {
            tvMartialStatus.setText(profile.getMartialStatus());
        }
        if (profile.getGender() == "") {
            tvGender.setText("N/A");
        } else {
            tvGender.setText(profile.getGender());
        }
        if (profile.getBloodGroup() == "") {
            tvBloodGroup.setText("N/A");
        } else {
            tvBloodGroup.setText(profile.getBloodGroup());
        }
        if (profile.getDateOfBirth() == "") {
            tvDOB.setText("N/A");
        } else {
            tvDOB.setText(profile.getDateOfBirth());
        }
        if (profile.getMobileNo() == "") {
            tvMobile.setText("N/A");
        } else {
            tvMobile.setText(profile.getMobileNo());
        }

    }
}
*/
