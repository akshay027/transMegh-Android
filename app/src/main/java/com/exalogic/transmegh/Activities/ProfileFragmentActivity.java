package com.exalogic.transmegh.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.exalogic.transmegh.API.RetrofitAPI;
import com.exalogic.transmegh.Database.PreferencesManger;
import com.exalogic.transmegh.Models.Profile;
import com.exalogic.transmegh.Models.ProfileResponse;
import com.exalogic.transmegh.Utility.Constants;
import com.exalogic.transmegh.Utility.UIUtil;
import com.exalogic.transmegh.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProfileFragmentActivity extends Fragment {
    private TextView tvName, tvEmail, tvMobile, tvDOB, tvSchoolEmail, tvdepartment,
            tvEmpStatus, tvJoiningDate, tvLicense;
    private Toolbar toolbar;
    ImageView poster;

    public static Fragment newInstance() {
        ProfileFragmentActivity fragment = new ProfileFragmentActivity();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile_fragment, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        tvMobile = (TextView) view.findViewById(R.id.tvMobile);

        poster = (ImageView) view.findViewById(R.id.poster);
        tvDOB = (TextView) view.findViewById(R.id.tvDOB);
        tvSchoolEmail = (TextView) view.findViewById(R.id.tvSchoolEmail);
        tvdepartment = (TextView) view.findViewById(R.id.tvdepartment);

        tvEmpStatus = (TextView) view.findViewById(R.id.tvEmpStatus);
        tvJoiningDate = (TextView) view.findViewById(R.id.tvJoiningDate);
        tvLicense = (TextView) view.findViewById(R.id.tvLicense);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getProfileDetails();
        return view;
    }

    private void getProfileDetails() {
        try {
            if (UIUtil.isInternetAvailable(getActivity())) {
                UIUtil.startProgressDialog(getActivity(), "Getting trip data...");

                Map<String, String> params = new HashMap<String, String>();
                params.put("branch_id", PreferencesManger.getStringFields(getActivity(), Constants.Pref.KEY_BRANCH_ID));
                params.put("user_type", PreferencesManger.getStringFields(getActivity(), Constants.Pref.KEY_USER_TYPE));
                RetrofitAPI.getInstance(getActivity()).getApi().getProfile(params, new Callback<ProfileResponse>() {
                    @Override
                    public void success(ProfileResponse profileResponse, Response response) {
                        try {
                            UIUtil.stopProgressDialog(getActivity());
                            if (profileResponse == null)
                                return;
                            if (profileResponse.getStatus() == Constants.SUCCESS) {
                                UIUtil.stopProgressDialog(getActivity());
                                bindData(profileResponse.getProfile());
                            } else {
                                Toast.makeText(getActivity(), "" + profileResponse.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void bindData(Profile profile) {
        try {

            if (TextUtils.isEmpty(profile.getPhoto())) {
                poster.setImageResource(R.drawable.profile);
            } else {
                Picasso.with(getActivity()).load(profile.getPhoto()).fit().into(poster);
            }
            if (TextUtils.isEmpty(profile.getName())) {
                tvName.setText("NA");
            } else {
                tvName.setText(profile.getName());
            }
            if (TextUtils.isEmpty(profile.getEmail())) {
                tvEmail.setText("NA");
            } else {
                tvEmail.setText(profile.getEmail());
            }
            if (TextUtils.isEmpty(profile.getSchoolEmail())) {
                tvSchoolEmail.setText("NA");
            } else {
                tvSchoolEmail.setText(profile.getSchoolEmail());
            }
            if (TextUtils.isEmpty(profile.getDepartment())) {
                tvdepartment.setText("NA");
            } else {
                tvdepartment.setText(profile.getDepartment());
            }

            if (TextUtils.isEmpty(profile.getLicense())) {
                tvLicense.setText("NA");
            } else {
                tvLicense.setText(profile.getLicense());
            }
            if (TextUtils.isEmpty(profile.getJoiningDate())) {
                tvJoiningDate.setText("NA");
            } else {
                tvJoiningDate.setText(profile.getJoiningDate());
            }
            if (TextUtils.isEmpty(profile.getStatus())) {
                tvEmpStatus.setText("NA");
            } else {
                tvEmpStatus.setText(profile.getStatus());
            }
            if (TextUtils.isEmpty(profile.getDateOfBirth())) {
                tvDOB.setText("NA");
            } else {
                tvDOB.setText(profile.getDateOfBirth());
            }
            if (TextUtils.isEmpty(profile.getMobileNo())) {
                tvMobile.setText("NA");
            } else {
                tvMobile.setText(profile.getMobileNo());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

