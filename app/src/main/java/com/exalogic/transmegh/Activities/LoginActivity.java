package com.exalogic.transmegh.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.exalogic.transmegh.API.RetrofitAPI;
import com.exalogic.transmegh.Database.PreferencesManger;
import com.exalogic.transmegh.Models.SignInResponse;
import com.exalogic.transmegh.Utility.Constants;
import com.exalogic.transmegh.Utility.UIUtil;
import com.exalogic.transmegh.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.orm.SugarContext;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUserName, etPassword;
    private Button btnSubmit;
    private Dialog dialog;
    private CheckBox cbShowpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SugarContext.init(this);

        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        cbShowpassword = (CheckBox) findViewById(R.id.cbShowpassword);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(this);

        getSupportActionBar().hide();
//        showDialog();
        etUserName.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER) ||
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        if (validateCredentials()) {
                            login();
                        }
                        // Toast.makeText(HelloFormStuff.this, edittext.getText(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                return false;
            }
        });
        etPassword.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER) ||
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        if (validateCredentials()) {
                            login();
                        }
                        // Toast.makeText(HelloFormStuff.this, edittext.getText(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                return false;
            }

        });
        cbShowpassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
             /*   if(isChecked) {
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    etPassword.setInputType(129);
                }*/
                if (!isChecked) {
                    // show password
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                if (validateCredentials())
                    login();
                break;
        }
    }

    private boolean validateCredentials() {

        if (TextUtils.isEmpty(etUserName.getText().toString())) {
            etUserName.setError("Please enter username");
            return false;
        }

        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            etPassword.setError("Please enter Password");
            return false;
        }

        return true;
//
    }

    private void login() {
        try {
            if (UIUtil.isInternetAvailable(this)) {
                UIUtil.startProgressDialog(this, "Signing .....");

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("user_email", etUserName.getText().toString());
                jsonObject.addProperty("password", etPassword.getText().toString());
                // jsonObject.addProperty("devise_id", ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId());
                jsonObject.addProperty("registration_id", FirebaseInstanceId.getInstance().getToken());
                jsonObject.addProperty("mobile_os", "Android");

                RetrofitAPI.getInstance(this).getApi().signIn(jsonObject, new Callback<SignInResponse>() {

                    @Override
                    public void success(SignInResponse jsonObject, Response response) {
                        try {
                            UIUtil.stopProgressDialog(getApplicationContext());

                            Log.e("Json ", "Hhd---" + jsonObject.toString());
                            Log.e("Json ", "response---" + response.getBody());

                            if (jsonObject.getStatus() == Constants.SUCCESS) {

                                PreferencesManger.addStringFields(getApplicationContext(), Constants.Pref.KEY_TOKEN, jsonObject.getToken());
                                PreferencesManger.addStringFields(getApplicationContext(), Constants.Pref.KEY_USER_TYPE, jsonObject.getUserType());
                                PreferencesManger.addStringFields(getApplicationContext(), Constants.Pref.KEY_USER_SUB_TYPE, jsonObject.getUserSubType());
                                PreferencesManger.addStringFields(getApplicationContext(), Constants.Pref.KEY_USER_NAME, jsonObject.getUserName());
                                PreferencesManger.addStringFields(getApplicationContext(), Constants.Pref.KEY_USER_EMAIL, jsonObject.getEmail());
                                PreferencesManger.addStringFields(getApplicationContext(), Constants.Pref.KEY_BRANCH_ID, jsonObject.getBranchid());
                                Toast.makeText(getApplicationContext(), jsonObject.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("Token", "-=-=-=-=-=----=-=-=-=-=-= jsonObject " + jsonObject.getToken());
                                Log.e("Token", "-=-=-=-=-=----=-=-=-=-=-=" + PreferencesManger.getStringFields(getApplicationContext(), Constants.Pref.KEY_TOKEN));
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                overridePendingTransition(R.anim.right_to_center, R.anim.center_to_left);
                                finish();

                            } else {
                                Toast.makeText(getApplicationContext(), jsonObject.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        UIUtil.stopProgressDialog(getApplicationContext());

                    }
                });
            } else {
                Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showDialog() {
        try {
            dialog = new Dialog(this, R.style.AppTheme_AppBarOverlay);
            dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_alert_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            final LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.llAlert);

            linearLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink));
            TextView dialogButton = (TextView) dialog.findViewById(R.id.tvAlertName);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
