package com.exalogic.transmegh.Models;

import com.exalogic.transmegh.Models.database.Student;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil on 14-08-2016.
 */
public class StudentResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("students")
    @Expose
    private List<Student> students = new ArrayList<Student>();
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "StudentResponse{" +
                "status=" + status +
                ", students=" + students +
                ", message='" + message + '\'' +
                '}';
    }
}
