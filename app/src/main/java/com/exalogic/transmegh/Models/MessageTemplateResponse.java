package com.exalogic.transmegh.Models;

import com.exalogic.transmegh.Models.database.MessageTemplate;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil on 26-11-2016.
 */

public class MessageTemplateResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("template")
    @Expose
    private List<MessageTemplate> template = new ArrayList<MessageTemplate>();
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<MessageTemplate> getTemplate() {
        return template;
    }

    public void setTemplate(List<MessageTemplate> template) {
        this.template = template;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageTemplateResponse{" +
                "status=" + status +
                ", template=" + template +
                ", message='" + message + '\'' +
                '}';
    }
}
