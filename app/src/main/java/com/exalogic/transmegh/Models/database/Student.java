package com.exalogic.transmegh.Models.database;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

/**
 * Created by Nikhil on 14-08-2016.
 */
public class Student extends SugarRecord {

    @SerializedName("student_id")
    @Expose
    private Integer studentId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("father_name")
    @Expose
    private String fatherName;
    @SerializedName("mother_name")
    @Expose
    private String motherName;
    @SerializedName("father_no")
    @Expose
    private String fatherNo;
    @SerializedName("mother_no")
    @Expose
    private String motherNo;
    @SerializedName("guardian_name")
    @Expose
    private String guardianName;
    @SerializedName("guardian_no")
    @Expose
    private String guardianNo;
    @SerializedName("check_in")
    @Expose
    private boolean checkIn;
    @SerializedName("check_out")
    @Expose
    private boolean checkOut;
    @SerializedName("photo")
    @Expose
    private String photo;
    private int busAssignId;
    private int busTripId;

    public Student() {
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getFatherNo() {
        return fatherNo;
    }

    public void setFatherNo(String fatherNo) {
        this.fatherNo = fatherNo;
    }

    public String getMotherNo() {
        return motherNo;
    }

    public void setMotherNo(String motherNo) {
        this.motherNo = motherNo;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getGuardianNo() {
        return guardianNo;
    }

    public void setGuardianNo(String guardianNo) {
        this.guardianNo = guardianNo;
    }
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }



    public boolean isCheckIn() {
        return checkIn;
    }

    public void setCheckIn(boolean checkIn) {
        this.checkIn = checkIn;
    }

    public boolean isCheckOut() {
        return checkOut;
    }

    public void setCheckOut(boolean checkOut) {
        this.checkOut = checkOut;
    }

    public int getBusAssignId() {
        return busAssignId;
    }

    public void setBusAssignId(int busAssignId) {
        this.busAssignId = busAssignId;
    }

    public int getBusTripId() {
        return busTripId;
    }

    public void setBusTripId(int busTripId) {
        this.busTripId = busTripId;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", motherName='" + motherName + '\'' +
                ", fatherNo='" + fatherNo + '\'' +
                ", motherNo='" + motherNo + '\'' +
                ", guardianName='" + guardianName + '\'' +
                ", guardianNo='" + guardianNo + '\'' +
                ", checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                ", photo='" + photo + '\'' +
                ", busAssignId=" + busAssignId +
                ", busTripId=" + busTripId +
                '}';
    }
}
