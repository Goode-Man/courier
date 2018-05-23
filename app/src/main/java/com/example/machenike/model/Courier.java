package com.example.machenike.model;

/**
 * Created by MACHENIKE on 2016/12/19.
 */

public class Courier {
    private int courierid;
    private String couriername;
    private String username;
    private String password;
    private int depotid;
    private String phone;

    public int getCourierid() {
        return courierid;
    }

    public void setCourierid(int courierid) {
        this.courierid = courierid;
    }

    public String getCouriername() {
        return couriername;
    }

    public void setCouriername(String couriername) {
        this.couriername = couriername;
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

    public int getDepotid() {
        return depotid;
    }

    public void setDepotid(int depotid) {
        this.depotid = depotid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Courier(int courierid, String couriername, String username, String password, int depotid, String phone) {
        this.courierid = courierid;
        this.couriername = couriername;
        this.username = username;
        this.password = password;
        this.depotid = depotid;
        this.phone = phone;
    }

    public Courier() {

    }

    public Courier(String couriername, String username, String password, int depotid, String phone) {
        this.couriername = couriername;
        this.username = username;
        this.password = password;
        this.depotid = depotid;
        this.phone = phone;
    }
}
