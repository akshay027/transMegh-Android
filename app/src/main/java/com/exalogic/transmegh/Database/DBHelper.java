package com.exalogic.transmegh.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.exalogic.transmegh.Models.database.BusStop;
import com.exalogic.transmegh.Utility.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil on 04-06-2016.
 */
public class DBHelper extends SQLiteOpenHelper {


    private SQLiteDatabase db;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "App_Database";
    private final Context context;

    // Table Name
    public static final String TABLE_BUS_STOP = "bus_stop";


    // Product Table Field
    public static final String COL_BUS_STOP_ID = "bus_stop_id";
    public static final String COL_BUS_STOP_NAME = "bus_stop_name";
    public static final String COL_TIME = "time";
    public static final String COL_SEQUENCE = "sequence";
    public static final String COL_LAT = "latitude";
    public static final String COL_LNG = "longitude";
    public static final String COL_SHIFT_TYPE = "shift_type";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String CREATE_TABLE_BUS_STOP = "CREATE TABLE " + TABLE_BUS_STOP + "("
                + COL_BUS_STOP_ID + " INTEGER, "
                + COL_BUS_STOP_NAME + " TEXT, "
                + COL_SEQUENCE + " INT, "
                + COL_LAT + " TEXT, "
                + COL_LNG + " TEXT, "
                + COL_TIME + " TEXT, "
                + COL_SHIFT_TYPE + " INT " + ");";
        db.execSQL(CREATE_TABLE_BUS_STOP);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    /**
     * Methods For Product Table
     */

   /* public boolean checkForProductExists(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery;
        selectQuery = "SELECT * FROM " + TABLE_PRODUCT + " where " + COL_PRODUCT_CODE + " = '" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }
*/

    // insert Multiple Product
    public void insertBusStopData(List<BusStop> arrayList) {

        db = this.getWritableDatabase();

        for (int i = 0; i < arrayList.size(); i++) {

            BusStop busStop = arrayList.get(i);

            try {

                ContentValues values = new ContentValues();
                values.put(COL_BUS_STOP_ID, busStop.getId());
                values.put(COL_BUS_STOP_NAME, busStop.getStopName());
                values.put(COL_LAT, busStop.getLatitude());
                values.put(COL_LNG, busStop.getLongitude());
//                values.put(COL_SEQUENCE, busStop.getSequence());
//                values.put(COL_SHIFT_TYPE, busStop.getType().equalsIgnoreCase(Constants.ONWARD) ? 1 : 2);

                db.insert(TABLE_BUS_STOP, null, values);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        db.close();
    }


    public void ClearBusTop() {
        try {
            db = this.getWritableDatabase();
            db.delete(TABLE_BUS_STOP, null, null);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public ArrayList<BusStop> getMorningShift() {
        ArrayList<BusStop> arrayList = new ArrayList<>();

        try {
            String query = "Select * from " + TABLE_BUS_STOP + " where " + COL_SHIFT_TYPE + " = 1 ;";
            db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);


            if (cursor.moveToFirst()) {
                do {
                    BusStop morningShift = new BusStop();
//                    morningShift.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_BUS_STOP_ID)));
//                    morningShift.setStopName(cursor.getString(cursor.getColumnIndexOrThrow(COL_BUS_STOP_NAME)));
//                    morningShift.setSequence(cursor.getInt(cursor.getColumnIndexOrThrow(COL_SEQUENCE)));
                    morningShift.setLatitude(cursor.getString(cursor.getColumnIndexOrThrow(COL_LAT)));
                    morningShift.setLongitude(cursor.getString(cursor.getColumnIndexOrThrow(COL_LNG)));
                    arrayList.add(morningShift);

                } while (cursor.moveToNext());
            }

            db.close();
            cursor.close();
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrayList;
    }

    public ArrayList<BusStop> getEveningShift() {
        ArrayList<BusStop> arrayList = new ArrayList<>();

        try {
            String query = "Select * from " + TABLE_BUS_STOP + " where " + COL_SHIFT_TYPE + " = 2 ;";
            db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);


            if (cursor.moveToFirst()) {
                do {
                    BusStop eveningShift = new BusStop();
//                    eveningShift.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_BUS_STOP_ID)));
//                    eveningShift.setStopName(cursor.getString(cursor.getColumnIndexOrThrow(COL_BUS_STOP_NAME)));
//                    eveningShift.setSequence(cursor.getInt(cursor.getColumnIndexOrThrow(COL_SEQUENCE)));
                    eveningShift.setLatitude(cursor.getString(cursor.getColumnIndexOrThrow(COL_LAT)));
                    eveningShift.setLongitude(cursor.getString(cursor.getColumnIndexOrThrow(COL_LNG)));
                    arrayList.add(eveningShift);

                } while (cursor.moveToNext());
            }

            db.close();
            cursor.close();
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrayList;
    }


    public int getButSequence(int id) {

        try {
            String query;
            int type = PreferencesManger.getIntFields(context, Constants.Pref.KEY_SHIFT_TYPE);
            query = "Select " + COL_SEQUENCE + " from " + TABLE_BUS_STOP + " where " + COL_BUS_STOP_ID + " = " + id
                    + " and " + COL_SHIFT_TYPE + " = " + type + ";";
//            } else {
//                query = "Select " + COL_SEQUENCE + " from " + TABLE_BUS_STOP + " where " + COL_BUS_STOP_ID + " = " + id
//                        + " and " + COL_SHIFT_TYPE + " = 2 ;";
//            }
            db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);


            if (cursor.moveToFirst()) {
                do {
                    Log.e("COL_SEQUENCE", COL_SEQUENCE + " ------------" + cursor.getInt(cursor.getColumnIndexOrThrow(COL_SEQUENCE)));
                    return cursor.getInt(cursor.getColumnIndexOrThrow(COL_SEQUENCE)) + 1;
                } while (cursor.moveToNext());
            }

            db.close();
            cursor.close();
            return -1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }


    public ArrayList<BusStop> getRunningTrip(boolean morning) {
        ArrayList<BusStop> arrayList = new ArrayList<>();

        try {
            String query;
            if (morning) {
                query = "Select * from " + TABLE_BUS_STOP + " where " + COL_SHIFT_TYPE + " = 1 ;";
            } else {
                query = "Select * from " + TABLE_BUS_STOP + " where " + COL_SHIFT_TYPE + " = 2 ;";
            }

            db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    BusStop eveningShift = new BusStop();
//                    eveningShift.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_BUS_STOP_ID)));
//                    eveningShift.setStopName(cursor.getString(cursor.getColumnIndexOrThrow(COL_BUS_STOP_NAME)));
//                    eveningShift.setSequence(cursor.getInt(cursor.getColumnIndexOrThrow(COL_SEQUENCE)));
                    eveningShift.setLatitude(cursor.getString(cursor.getColumnIndexOrThrow(COL_LAT)));
                    eveningShift.setLongitude(cursor.getString(cursor.getColumnIndexOrThrow(COL_LNG)));
                    arrayList.add(eveningShift);

                } while (cursor.moveToNext());
            }

            db.close();
            cursor.close();
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrayList;
    }


/**
 * Methods For ADS Table
 *//*


    // Insert Single Ads
    public void insertAds(Ads ads) {

        try {

            ContentValues values = new ContentValues();
            values.put(COL_COMPANY_ID, ads.getCompanyId());
            values.put(COL_PRODUCT_DSC, ads.getProductDescription());
            values.put(COL_COMPANY_LOGO_URL, ads.getCompanyLogoUrl());
            values.put(COL_PRODUCT_NAME, ads.getProductName());
            values.put(COL_COMPANY_NAME, ads.getCompanyName());
            values.put(COL_PRODUCT_IMG_URL, ads.getProductImageUrl());

            if (db == null)
                db = this.getWritableDatabase();

            db.insert(TABLE_ADS, null, values);
            db.close();

        } catch (Exception e) {
            db.close();
            e.printStackTrace();
        }

    }


    // insert Multiple Ads
    public void insertAds(ArrayList<Ads> arrayList) {

        if (db == null)
            db = this.getWritableDatabase();

        for (int i = 0; i < arrayList.size(); i++) {

            Ads ads = arrayList.get(i);

            try {

                ContentValues values = new ContentValues();
                values.put(COL_COMPANY_ID, ads.getCompanyId());
                values.put(COL_PRODUCT_DSC, ads.getProductDescription());
                values.put(COL_COMPANY_LOGO_URL, ads.getCompanyLogoUrl());
                values.put(COL_PRODUCT_NAME, ads.getProductName());
                values.put(COL_COMPANY_NAME, ads.getCompanyName());
                values.put(COL_PRODUCT_IMG_URL, ads.getProductImageUrl());

                db.insert(TABLE_ADS, null, values);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        db.close();
    }
*/


}
