package com.example.machenike.model;

/**
 * Created by MACHENIKE on 2016/12/19.
 */

public class Company {
    private int companyid;
    private String companyname;
    private String username;
    private String password;
    private String companytel;

    public int getCompanyid() {
        return companyid;
    }

    public void setCompanyid(int companyid) {
        this.companyid = companyid;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
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

    public String getCompanytel() {
        return companytel;
    }

    public void setCompanytel(String companytel) {
        this.companytel = companytel;
    }

    public Company() {
        super();
    }

    public Company(int companyid, String companyname, String username, String password, String companytel) {
        this.companyid = companyid;
        this.companyname = companyname;
        this.username = username;
        this.password = password;
        this.companytel = companytel;
    }
}
