/*
 * Copyright (c) 2016. Truiton (http://www.truiton.com/).
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

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.exalogic.transmegh.API.RetrofitAPI;
import com.exalogic.transmegh.Database.PreferencesManger;
import com.exalogic.transmegh.Utility.Constants;
import com.exalogic.transmegh.Utility.UIUtil;
import com.exalogic.transmegh.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {
    private int mSelectedItem;
    private static final String SELECTED_ITEM = "arg_selected_item";
    private BottomNavigationView bottomNavigationView;
    Fragment frag = null;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = getSupportActionBar();
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        getSupportActionBar();

        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        //bottomNavigationView.getMenu().getItem(mSelectedItem).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        selectFragment(item);
                        bottomNavigationView.getMenu().getItem(1).setChecked(true);
                        return true;
                    }
                });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, HomeTabActivity.newInstance());
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        MenuItem homeItem = bottomNavigationView.getMenu().getItem(1);
        if (mSelectedItem != homeItem.getItemId()) {
            // select home item
            selectFragment(homeItem);
        } else {
            super.onBackPressed();
        }
    }

    private void selectFragment(MenuItem item) {

        // init corresponding fragment
        switch (item.getItemId()) {
            case R.id.home:
                bottomNavigationView.getMenu().getItem(0).setCheckable(false);
                bottomNavigationView.getMenu().getItem(1).setCheckable(true);
                bottomNavigationView.getMenu().getItem(2).setCheckable(false);
                frag = HomeTabActivity.newInstance();
                actionBar.setTitle("Home");
                break;
            case R.id.parent:
                bottomNavigationView.getMenu().getItem(0).setCheckable(false);
                bottomNavigationView.getMenu().getItem(1).setCheckable(false);
                bottomNavigationView.getMenu().getItem(2).setCheckable(true);
                frag = ParentListFragment.newInstance();
                actionBar.setTitle("Contact Parent");
                break;
            case R.id.mybus:
                bottomNavigationView.getMenu().getItem(0).setCheckable(true);
                bottomNavigationView.getMenu().getItem(1).setCheckable(false);
                bottomNavigationView.getMenu().getItem(2).setCheckable(false);
                frag = BusFragment.newInstance();
                actionBar.setTitle("Bus Info");
                break;
        }

        // update selected item
        mSelectedItem = item.getItemId();

        // uncheck the other items.
        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
            menuItem.setChecked(menuItem.getItemId() == item.getItemId());
        }

        //updateToolbarText(item.getTitle());

        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_layout, frag);
            ft.commit();
        }
    }

    public void setTitle(String title) {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = new TextView(this);
        textView.setText(title);
        textView.setTextSize(20);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(textView);
    }

    private void updateToolbarText(CharSequence text) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(text);


        }
    }

    private int getColorFromRes(@ColorRes int resId) {
        return ContextCompat.getColor(this, resId);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.driver_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.profile:
                bottomNavigationView.getMenu().getItem(0).setCheckable(false);
                bottomNavigationView.getMenu().getItem(1).setCheckable(false);
                bottomNavigationView.getMenu().getItem(2).setCheckable(false);
                frag = ProfileFragmentActivity.newInstance();
                for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
                    MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
                    menuItem.setChecked(menuItem.getItemId() == item.getItemId());
                    actionBar.setTitle("Profile");
                }
                //updateToolbarText(item.getTitle());
                if (frag != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame_layout, frag);
                    ft.commit();
                }

                return true;
            case R.id.logout:
                showLogoutConfirmation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //rest of app

    private void showLogoutConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

//        AlertDialog.Builder builder =
//                new AlertDialog.Builder(this, R.style.AppTheme_AppBarOverlay);

        builder.setTitle("Confirmation");
        String message = "Do you want to logout?";
        builder.setMessage(message);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
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

    private void logout() {
        try {
            if (UIUtil.isInternetAvailable(this)) {
                UIUtil.startProgressDialog(this, "Please Wait.. logging out..");
                Map<String, String> params = new HashMap<String, String>();
                params.put("branch_id", PreferencesManger.getStringFields(getApplicationContext(), Constants.Pref.KEY_BRANCH_ID));
                RetrofitAPI.getInstance(this).getApi().logout(params, new Callback<JSONObject>() {
                    @Override
                    public void success(JSONObject object, Response response) {
                        try {
                            UIUtil.stopProgressDialog(getApplicationContext());
                            Log.e("API", "logout-" + object.toString());
                            Toast.makeText(getApplicationContext(), "Logout successfully..", Toast.LENGTH_SHORT).show();
                            PreferencesManger.clearPreferences(getApplicationContext());
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            overridePendingTransition(R.anim.right_to_center, R.anim.center_to_left);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        PreferencesManger.clearPreferences(getApplicationContext());
                        UIUtil.stopProgressDialog(getApplicationContext());
                        Toast.makeText(getApplicationContext(), "Logout successfully..", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        overridePendingTransition(R.anim.right_to_center, R.anim.center_to_left);
                        finish();
                    }
                });
            } else {
                Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
