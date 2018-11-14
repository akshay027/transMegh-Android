package com.exalogic.transmegh.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nikhil on 9/13/2016.
 */
public class Parent {
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
        private Boolean checkIn;
        @SerializedName("check_out")
        @Expose
        private Boolean checkOut;
        @SerializedName("photo")
        @Expose
        private String photo;

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

        public Boolean getCheckIn() {
            return checkIn;
        }

        public void setCheckIn(Boolean checkIn) {
            this.checkIn = checkIn;
        }

        public Boolean getCheckOut() {
            return checkOut;
        }

        public void setCheckOut(Boolean checkOut) {
            this.checkOut = checkOut;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }