package com.exalogic.transmegh.Models;

import com.exalogic.transmegh.Models.database.Trip;
import com.exalogic.transmegh.Models.database.Trip;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil on 12-11-2016.
 */

public class TripListResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("bus_trips")
    @Expose
    private List<Trip> trips = new ArrayList<Trip>();
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "TripListResponse{" +
                "status=" + status +
                ", trips=" + trips +
                ", message='" + message + '\'' +
                '}';
    }
}
