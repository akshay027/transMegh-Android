package com.exalogic.transmegh.Utility;

import android.content.Context;

/**
 * Created by Nikhil on 09-05-2016.
 */
public class Constants {

    //public static final String Host = "http://192.168.1.10:3000/api/v1";
   // public static final String HostImage = "http://192.168.1.10:3000/";

    ///public static final String Host = "https://inmegh-kadambur.herokuapp.com/api/v1";

    public static final String Host = "http://inmegh-beta.com/api/v1";
  //  public static final String HostImage = "http://inmegh-beta.com/";

    public static final String CITY = "city";
    public static final String ONWARD = "Onward";
    public static final String RETURN = "Return";

    public static final String LNG = "lng";
    public static final String LAT = "lat";

    public static final int SUCCESS = 1;
    public static final int ERROR = 0;

    public static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    public static final int PRESENT = 1;
    public static final int MORNING = 1;
    public static final int ABSENT = 2;

    public static final int EVENING = 2;
    public static final int NOT_MARK = 3;
    public static final int SENT = 1;
    public static Context context;
    public static final String TEACHING = "Teaching";
    public static final String CLASS_TEACHER = "class teacher";

    public static final String BUs_TRIP_ID = "trip_id";
    public static final String TRIP_NAME = "trip_name";
    public static final String TRIP_START_TIME = "trip_start_timr";
    public static final String TRIP_END_TIME = "trip_end_time";
    public static final String BUS_STOP_NAME = "bus_stop_name";
    public static final String BUS_STOP_Address = "bus_stop_address";
    public static final String BUS_RUNNING_ID = "bus_running_id";


    public static final int START = 1;
    public static final int END = 3;
    public static final int RUNNING = 2;
    public static final int TRIP_RUNNING = 2;

    public static class Pref {
        public static final String KEY_TOKEN = "token";
        public static final String KEY_USER_TYPE = "user_type";
        public static final String KEY_USER_SUB_TYPE = "user_sub_type";
        public static final String KEY_IS_TRIP_START = "is_trip_start";
        public static final String KEY_SHIFT_TYPE = "shift_type";
        public static final String KEY_TRIP_ID = "trip_id";
        public static final String BUS_NO_APP = "12";
        public static final String SPEE = "3";
        public static final String TRIP_ID = "trip_id";
        public static final String KEY_BUS_TRIP_ID = "bus_trip_id";
        public static final String KEY_USER_NAME = "user_name";
        public static final String KEY_USER_EMAIL = "user_email";
        public static final String KEY_BRANCH_ID = "branch_id";
        public static final String KEY_CURRENT_STOP_SQU = "current_stop_squ";
        public static final String KEY_ONWARD_ROUTE_NAME = "onward_route_name";
        public static final String KEY_ONWARD_ROUTE_TIME = "onward_route_time";
        public static final String KEY_RETURN_ROUTE_NAME = "return_route_name";
        public static final String KEY_RETURN_ROUTE_TIME = "return_route_time";
        public static final String KEY_FCM_REG_ID = "Gcm_reg_id";
        public static final String KEY_CHAT_OPEN = "chat_open";

        public static final String KEY_IS_DROP = "is_drop";
        public static final String KEY_IS_CURRENT = "is_current";
    }

}
