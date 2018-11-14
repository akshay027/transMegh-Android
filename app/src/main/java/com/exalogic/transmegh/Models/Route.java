package com.exalogic.transmegh.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nikhil on 14-08-2016.
 */
public class Route {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("routing_place_timing_id")
    @Expose
    private Integer routingPlaceTimingId;
    @SerializedName("route_id")
    @Expose
    private Integer routeId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("stop_name")
    @Expose
    private String stopName;

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The routingPlaceTimingId
     */
    public Integer getRoutingPlaceTimingId() {
        return routingPlaceTimingId;
    }

    /**
     * @param routingPlaceTimingId The routing_place_timing_id
     */
    public void setRoutingPlaceTimingId(Integer routingPlaceTimingId) {
        this.routingPlaceTimingId = routingPlaceTimingId;
    }

    /**
     * @return The routeId
     */
    public Integer getRouteId() {
        return routeId;
    }

    /**
     * @param routeId The route_id
     */
    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    /**
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return The stopName
     */
    public String getStopName() {
        return stopName;
    }

    /**
     * @param stopName The stop_name
     */
    public void setStopName(String stopName) {
        this.stopName = stopName;
    }
}
