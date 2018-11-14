package com.exalogic.transmegh.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nikhil on 03-07-2016.
 */
public class Bus {

    @SerializedName("bus_no")
    @Expose
    private String busNo;
    @SerializedName("registration_no")
    @Expose
    private String registrationNo;
    @SerializedName("capacity")
    @Expose
    private Integer capacity;
    @SerializedName("year_of_manufacture")
    @Expose
    private String yearOfManufacture;
    @SerializedName("make")
    @Expose
    private String make;
    @SerializedName("permit_upto")
    @Expose
    private String permitUpto;
    @SerializedName("tax_paid_upto")
    @Expose
    private String taxPaidUpto;
    @SerializedName("break_fitness_upto")
    @Expose
    private String breakFitnessUpto;
    @SerializedName("insurance_upto")
    @Expose
    private String insuranceUpto;
    @SerializedName("insurance_company")
    @Expose
    private String insuranceCompany;
    @SerializedName("pollution_upto")
    @Expose
    private String pollutionUpto;
    @SerializedName("owner")
    @Expose
    private String owner;

    public String getBusNo() {
        return busNo;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getYearOfManufacture() {
        return yearOfManufacture;
    }

    public void setYearOfManufacture(String yearOfManufacture) {
        this.yearOfManufacture = yearOfManufacture;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getPermitUpto() {
        return permitUpto;
    }

    public void setPermitUpto(String permitUpto) {
        this.permitUpto = permitUpto;
    }

    public String getTaxPaidUpto() {
        return taxPaidUpto;
    }

    public void setTaxPaidUpto(String taxPaidUpto) {
        this.taxPaidUpto = taxPaidUpto;
    }

    public String getBreakFitnessUpto() {
        return breakFitnessUpto;
    }

    public void setBreakFitnessUpto(String breakFitnessUpto) {
        this.breakFitnessUpto = breakFitnessUpto;
    }

    public String getInsuranceUpto() {
        return insuranceUpto;
    }

    public void setInsuranceUpto(String insuranceUpto) {
        this.insuranceUpto = insuranceUpto;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public String getPollutionUpto() {
        return pollutionUpto;
    }

    public void setPollutionUpto(String pollutionUpto) {
        this.pollutionUpto = pollutionUpto;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Bus{" +
                "busNo='" + busNo + '\'' +
                ", registrationNo='" + registrationNo + '\'' +
                ", capacity=" + capacity +
                ", yearOfManufacture='" + yearOfManufacture + '\'' +
                ", make='" + make + '\'' +
                ", permitUpto='" + permitUpto + '\'' +
                ", taxPaidUpto='" + taxPaidUpto + '\'' +
                ", breakFitnessUpto='" + breakFitnessUpto + '\'' +
                ", insuranceUpto='" + insuranceUpto + '\'' +
                ", insuranceCompany='" + insuranceCompany + '\'' +
                ", pollutionUpto='" + pollutionUpto + '\'' +
                ", owner='" + owner + '\'' +
                '}';
    }
}