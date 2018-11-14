package com.exalogic.transmegh.Models.database;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

/**
 * Created by Nikhil on 26-11-2016.
 */

public class MessageTemplate extends SugarRecord {

    @SerializedName("template_id")
    @Expose
    private Integer templateId;
    @SerializedName("name")
    @Expose
    private String template;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("body")
    @Expose
    private String body;

    public MessageTemplate() {
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "MessageTemplate{" +
                "templateId=" + templateId +
                ", template='" + template + '\'' +
                ", color='" + color + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
