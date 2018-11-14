package com.exalogic.transmegh.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nikhil on 05-06-2016.
 */
public class TimeTable {

    @SerializedName("standard")
    @Expose
    private String standard;

    @SerializedName("section")
    @Expose
    private String section;

    @SerializedName("period")
    @Expose
    private String period;

    @SerializedName("subject")
    @Expose
    private String subject;

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "TimeTable{" +
                "standard='" + standard + '\'' +
                ", section='" + section + '\'' +
                ", period='" + period + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }
}
