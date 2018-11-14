package com.exalogic.transmegh.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nikhil on 04-06-2016.
 */
public class SignInResponse {

    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("user_type")
    @Expose
    private String userType;

    @SerializedName("user_sub_type")
    @Expose
    private String userSubType;

    @SerializedName("user_name")
    @Expose

    private String userName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("branch_id")
    @Expose
    private String Branchid;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserSubType() {
        return userSubType;
    }

    public void setUserSubType(String userSubType) {
        this.userSubType = userSubType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBranchid() {
        return Branchid;
    }

    public void setBranchid(String branchid) {
        Branchid = branchid;
    }

    @Override
    public String toString() {
        return "SignInResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", token='" + token + '\'' +
                ", userType='" + userType + '\'' +
                ", userSubType='" + userSubType + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", Branchid='" + Branchid + '\'' +
                '}';
    }
}
