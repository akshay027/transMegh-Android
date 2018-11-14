package com.exalogic.transmegh.Models.database;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

/**
 * Created by Nikhil on 25-06-2016.
 */
public class BusStop extends SugarRecord {
    @SerializedName("bus_no")
    @Expose
    private String busno;
    @SerializedName("stop_id")
    @Expose
    private Integer stopId;
    @SerializedName("bus_trip_id")
    @Expose
    private Integer busTripId;
    @SerializedName("stop_name")
    @Expose
    private String stopName;
    @SerializedName("priority")
    @Expose
    private Integer priority;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("timing")
    @Expose
    private String timing;
    @SerializedName("bus_assign_id")
    @Expose
    private Integer busAssignId;
    @SerializedName("student_count")
    @Expose
    private String studentCount;

    public String getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(String studentCount) {
        this.studentCount = studentCount;
    }

    private boolean reached;

    public Integer getStopId() {
        return stopId;
    }

    public void setStopId(Integer stopId) {
        this.stopId = stopId;
    }

    public Integer getBusTripId() {
        return busTripId;
    }

    public void setBusTripId(Integer busTripId) {
        this.busTripId = busTripId;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public Integer getBusAssignId() {
        return busAssignId;
    }

    public void setBusAssignId(Integer busAssignId) {
        this.busAssignId = busAssignId;
    }

    public boolean isReached() {
        return reached;
    }

    public void setReached(boolean reached) {
        this.reached = reached;
    }

    public String getBusno() {
        return busno;
    }

    public void setBusno(String busno) {
        this.busno = busno;
    }

    @Override
    public String toString() {
        return "BusStop{" +
                "busno='" + busno + '\'' +
                ", stopId=" + stopId +
                ", busTripId=" + busTripId +
                ", stopName='" + stopName + '\'' +
                ", priority=" + priority +
                ", address='" + address + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", timing='" + timing + '\'' +
                ", busAssignId=" + busAssignId +
                ", studentCount=" + studentCount +
                ", reached=" + reached +
                '}';
    }
}