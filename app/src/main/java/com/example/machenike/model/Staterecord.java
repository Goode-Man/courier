package com.example.machenike.model;

import java.util.Date;

/**
 * Created by MACHENIKE on 2016/12/20.
 */

public class Staterecord {
    private int id;
    private int courierid;
    private String datetimes;
    private int state;
    private String expressid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourierid() {
        return courierid;
    }

    public void setCourierid(int courierid) {
        this.courierid = courierid;
    }

    public String getDatetimes() {
        return datetimes;
    }

    public void setDatetimes(String datetimes) {
        this.datetimes = datetimes;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getExpressid() {
        return expressid;
    }

    public void setExpressid(String expressid) {
        this.expressid = expressid;
    }

    public Staterecord(int id, int courierid, String datetimes, int state, String expressid) {

        this.id = id;
        this.courierid = courierid;
        this.datetimes = datetimes;
        this.state = state;
        this.expressid = expressid;
    }

    public Staterecord(int courierid, String datetimes, int state, String expressid) {
        this.courierid = courierid;
        this.datetimes = datetimes;
        this.state = state;
        this.expressid = expressid;
    }
}
