package com.example.machenike.model;

/**
 * Created by MACHENIKE on 2016/12/27.
 */

public class Template {
    private int id;
    private String templateText;

    public Template(int id, String templateText) {
        this.id = id;
        this.templateText = templateText;
    }

    public Template(String templateText) {
        this.templateText = templateText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTemplateText() {
        return templateText;
    }

    public void setTemplateText(String templateText) {
        this.templateText = templateText;
    }
}
