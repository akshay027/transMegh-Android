package com.exalogic.transmegh.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil on 03-07-2016.
 */
public class BusResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("my_bus")
    @Expose
    private List<Bus> bus = new ArrayList<Bus>();
    @SerializedName("message")
    @Expose
    private String message;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Bus> getBus() {
        return bus;
    }

    public void setBus(List<Bus> bus) {
        this.bus = bus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BusResponse{" +
                "status=" + status +
                ", bus=" + bus +
                ", message='" + message + '\'' +
                '}';
    }
}