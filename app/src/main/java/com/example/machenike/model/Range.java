package com.example.machenike.model;

/**
 * Created by MACHENIKE on 2016/12/19.
 */

public class Range {
    private int rangeid;
    private float longitude;
    private float latitude;
    private int courierid;

    public Range(int rangeid, float longitude, float latitude, int courierid) {
        this.rangeid = rangeid;
        this.longitude = longitude;
        this.latitude = latitude;
        this.courierid = courierid;
    }

    public int getRangeid() {
        return rangeid;
    }

    public void setRangeid(int rangeid) {
        this.rangeid = rangeid;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public int getCourierid() {
        return courierid;
    }

    public void setCourierid(int courierid) {
        this.courierid = courierid;
    }
}
