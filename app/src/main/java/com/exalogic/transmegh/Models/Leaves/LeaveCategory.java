package com.exalogic.inmegh.transmegh.Models.Leaves;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nikhil on 18-06-2016.
 */
public class LeaveCategory {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("max_leave")
    @Expose
    private String maxLeave;
    @SerializedName("available_leave")
    @Expose
    private String availableLeave;


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
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The maxLeave
     */
    public String getMaxLeave() {
        return maxLeave;
    }

    /**
     * @param maxLeave The max_leave
     */
    public void setMaxLeave(String maxLeave) {
        this.maxLeave = maxLeave;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAvailableLeave() {
        return availableLeave;
    }

    public void setAvailableLeave(String availableLeave) {
        this.availableLeave = availableLeave;
    }

    @Override
    public String toString() {
        return "LeaveCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", maxLeave='" + maxLeave + '\'' +
                '}';
    }
}

