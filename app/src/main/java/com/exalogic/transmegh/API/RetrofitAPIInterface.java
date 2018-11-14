package com.exalogic.transmegh.API;

import com.exalogic.transmegh.Models.BusResponse;
import com.exalogic.transmegh.Models.MessageTemplateResponse;
import com.exalogic.transmegh.Models.ParentChatListResponse;
import com.exalogic.transmegh.Models.ProfileResponse;
import com.exalogic.transmegh.Models.RouteResponse;
import com.exalogic.transmegh.Models.SignInResponse;
import com.exalogic.transmegh.Models.StudentResponse;
import com.exalogic.transmegh.Models.TripListResponse;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.QueryMap;


/**
 * Created by Nikhil on 04-06-2016.
 */
public interface RetrofitAPIInterface {


    @POST("/driver_sign_in")
    @Headers({
            "Accept: application/json"
    })
    public void signIn(@Body JsonObject loginDetails, Callback<SignInResponse> status);


    // Get attendance Details From Server
    @POST("/start_trip")
    public void updateTripStatus(@Body JsonObject jsonObject, Callback<JsonObject> json);


    @POST("/update_current_status")
    public void updateTripLatLong(@Body JsonObject date, Callback<JsonObject> json);

    //    @POST("/mark_attendance")
//    public void markAttendance(@Body JsonObject date, Callback<JSONObject> attendance);
//
    @GET("/registrations/sign_out")
    public void logout(@QueryMap Map<String, String> params, Callback<JSONObject> logout);

    @GET("/personal_profile")
    public void getProfile(@QueryMap Map<String, String> params, Callback<ProfileResponse> profileResponse);

    @GET("/my_bus")
    public void getBusDetails(@QueryMap Map<String, String> params, Callback<BusResponse> busResponse);

    @GET("/running_status")
    public void getRunningTripStatus(@QueryMap Map<String, String> params, Callback<JsonObject> jsonObjectCallback);

    @GET("/my_route")
    public void getMyRoute(Callback<RouteResponse> jsonObjectCallback);

    @POST("/my_route_student")
    public void getRouteStudent(@Body JsonObject jsonObject, Callback<StudentResponse> jsonObjectCallback);

    @POST("/send_sms")
    public void setMessage(@Body JsonObject message, Callback<JsonObject> leaveResponse);

    @POST("/send_trip_message")
    public void setMessageToCompleteBusTrip(@Body JsonObject message, Callback<JsonObject> leaveResponse);

//    @POST("/apply_leave")
//    public void applyLeave(@Body JsonObject jsonObject, Callback<JsonObject> leaveJson);

//    @GET("/student_leave_application")
//    public void studentLeaveApplication(Callback<StudentLeaveResponse> leaveResponse);

//    @POST("/teacher_approved_leave_application")
//    public void teacherApprovedLeaveApplication(@Body JsonObject jsonObject, Callback<JsonObject> leaveJson);

//    // Get Search result https://api.myjson.com/bins/1kb7w
//    @GET("/bins/1kb7w")
//    public void getSearchResult(Callback<List<Product>> productList);


    @GET("/get_parent_name")
    public void getParentForDriver(@QueryMap Map<String, String> params, Callback<ParentChatListResponse> chatListResponseCallback);

    @POST("/send_message")
    public void sentChatMessage(@Body JsonObject message, Callback<JsonObject> jsonObjectCallback);

    @GET("/list_trip")
    public void getTripList(@QueryMap Map<String, String> params, Callback<TripListResponse> chatListResponseCallback);

    @POST("/update_speed_exceed_count")
    public void getmaxspeed(@Body JsonObject message, Callback<JsonObject> jsonObjectCallback);

    @POST("/get_student_details")
    public void getStudentListForBusStop(@Body JsonObject message, Callback<StudentResponse> responseCallback);

    @GET("/get_message_template")
    public void getMessageTemplate(@QueryMap Map<String, String> params, Callback<MessageTemplateResponse> messageTemplateResponseCallback);

    @POST("/transport_mark_attendance")
    public void markAttendance(@Body JsonObject jsonObject, Callback<JsonObject> objectCallback);

}
