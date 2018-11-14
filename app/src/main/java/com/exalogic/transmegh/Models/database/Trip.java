package com.exalogic.transmegh.Models.database;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil on 12-11-2016.
 */

public class Trip extends SugarRecord {
    @SerializedName("bus_no")
    @Expose
    private String busno;
    @SerializedName("bus_trip_id")
    @Expose
    private Integer busTripId;
    @SerializedName("bus_trip_name")
    @Expose
    private String busTripName;
    @SerializedName("pickup_type")
    @Expose
    private Boolean pickupType;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("bus_stop")
    @Expose
    private List<BusStop> busStop = new ArrayList<BusStop>();
    @SerializedName("bus_stop_count")
    @Expose
    private Integer busStopCount;
    @SerializedName("trip_status")
    @Expose
    private Integer  tripstatus;

    private boolean isTripActive;

    public Integer getBusTripId() {
        return busTripId;
    }

    public void setBusTripId(Integer busTripId) {
        this.busTripId = busTripId;
    }

    public String getBusTripName() {
        return busTripName;
    }

    public void setBusTripName(String busTripName) {
        this.busTripName = busTripName;
    }

    public Boolean getPickupType() {
        return pickupType;
    }

    public void setPickupType(Boolean pickupType) {
        this.pickupType = pickupType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<BusStop> getBusStop() {
        return busStop;
    }

    public void setBusStop(List<BusStop> busStop) {
        this.busStop = busStop;
    }

    public Integer getBusStopCount() {
        return busStopCount;
    }

    public void setBusStopCount(Integer busStopCount) {
        this.busStopCount = busStopCount;
    }

    public boolean isTripActive() {
        return isTripActive;
    }

    public void setTripActive(boolean tripActive) {
        isTripActive = tripActive;
    }

    public String getBusno() {
        return busno;
    }

    public void setBusno(String busno) {
        this.busno = busno;
    }

    public Integer getTripstatus() {
        return tripstatus;
    }

    public void setTripstatus(Integer tripstatus) {
        this.tripstatus = tripstatus;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "busno='" + busno + '\'' +
                ", busTripId=" + busTripId +
                ", busTripName='" + busTripName + '\'' +
                ", pickupType=" + pickupType +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", busStop=" + busStop +
                ", busStopCount=" + busStopCount +
                ", tripstatus=" + tripstatus +
                ", isTripActive=" + isTripActive +
                '}';
    }
}
