package com.exalogic.transmegh.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nikhil on 03-07-2016.
 */
public class Profile {
    @SerializedName("primary_email")
    @Expose
    private String email;
    @SerializedName("school_email")
    @Expose
    private String schoolEmail;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("date_of_birth")
    @Expose
    private String dateOfBirth;

    @SerializedName("blood_group")
    @Expose
    private String bloodGroup;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("joining_date")
    @Expose
    private String joiningDate;

    @SerializedName("license")
    @Expose
    private String license;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("department")
    @Expose
    private String department;

    public String getSchoolEmail() {
        return schoolEmail;
    }

    public void setSchoolEmail(String schoolEmail) {
        this.schoolEmail = schoolEmail;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * @return The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email
     */
    public void setEmail(String email) {
        this.email = email;
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
     * @return The gender
     */

    /**
     * @param gender The gender
     */

    /**
     * @return The dateOfBirth
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * @param dateOfBirth The date_of_birth
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * @return The martialStatus
     */

    /**
     * @param martialStatus The martial_status
     */

    /**
     * @return The bloodGroup
     */
    public String getBloodGroup() {
        return bloodGroup;
    }

    /**
     * @param bloodGroup The blood_group
     */
    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return The mobileNo
     */
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     * @param mobileNo The mobile_no
     */
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    /**
     * @return The joiningDate
     */
    public String getJoiningDate() {
        return joiningDate;
    }

    /**
     * @param joiningDate The joining_date
     */
    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
    }

    /**
     * @return The panCard
     */

    /**
     * @param panCard The pan_card
     */

    /**
     * @return The aadhaarCard
     */


    /**
     * @param aadhaarCard The aadhaar_card
     */

    /**
     * @return The license
     */
    public String getLicense() {
        return license;
    }

    /**
     * @param license The license
     */
    public void setLicense(String license) {
        this.license = license;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", status='" + status + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", joiningDate='" + joiningDate + '\'' +
                ", license='" + license + '\'' +
                ", photo='" + photo + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
