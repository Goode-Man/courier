package com.example.machenike.model;

/**
 * Created by MACHENIKE on 2016/12/19.
 */

public class Depot {
    private int depotid;
    private String depotname;
    private String username;
    private String password;
    private String depottel;
    private String address;
    private float longitude;
    private float latitude;
    private int companyid;

    public int getDepotid() {
        return depotid;
    }

    public void setDepotid(int depotid) {
        this.depotid = depotid;
    }

    public String getDepotname() {
        return depotname;
    }

    public void setDepotname(String depotname) {
        this.depotname = depotname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepottel() {
        return depottel;
    }

    public void setDepottel(String depottel) {
        this.depottel = depottel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public int getCompanyid() {
        return companyid;
    }

    public void setCompanyid(int companyid) {
        this.companyid = companyid;
    }

    public Depot(int depotid, String depotname, String username, String password, String depottel, String address, float longitude, float latitude, int companyid) {
        this.depotid = depotid;
        this.depotname = depotname;
        this.username = username;
        this.password = password;
        this.depottel = depottel;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.companyid = companyid;
    }
}
