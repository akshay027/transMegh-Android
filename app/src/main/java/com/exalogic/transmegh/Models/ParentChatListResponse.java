package com.exalogic.transmegh.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil on 9/13/2016.
 */
public class ParentChatListResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("list")
    @Expose
    private List<Parent> list = new ArrayList<Parent>();
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Parent> getList() {
        return list;
    }

    public void setList(List<Parent> list) {
        this.list = list;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ParentChatListResponse{" +
                "status=" + status +
                ", list=" + list +
                ", message='" + message + '\'' +
                '}';
    }
}
