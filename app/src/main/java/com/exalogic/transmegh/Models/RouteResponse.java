package com.exalogic.transmegh.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil on 14-08-2016.
 */
public class RouteResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("route")
    @Expose
    private List<Route> route = new ArrayList<Route>();
    @SerializedName("message")
    @Expose
    private String message;

    /**
     * @return The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return The route
     */
    public List<Route> getRoute() {
        return route;
    }

    /**
     * @param route The route
     */
    public void setRoute(List<Route> route) {
        this.route = route;
    }

    /**
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
