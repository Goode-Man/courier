package com.example.machenike.model;

/**
 * Created by MACHENIKE on 2016/12/19.
 */

public class Express {
    private String expressid;
    private String sender;
    private String sendertel;
    private String sendercity;
    private String senderadress;
    private String Addressee;
    private String addresseetel;
    private String addresseecity;
    private String addresseeadress;
    private int companyid;
    private double longitude;
    private double latitude;
    private int state;//1.派件中 2.已签收 3.待收件 4.已揽件

    public Express(String expressid, String sender, String sendertel, String sendercity, String senderadress, String addressee, String addresseetel, String addresseecity, String addresseeadress, int companyid, double longitude, double latitude, int state) {
        this.expressid = expressid;
        this.sender = sender;
        this.sendertel = sendertel;
        this.sendercity = sendercity;
        this.senderadress = senderadress;
        Addressee = addressee;
        this.addresseetel = addresseetel;
        this.addresseecity = addresseecity;
        this.addresseeadress = addresseeadress;
        this.companyid = companyid;
        this.longitude = longitude;
        this.latitude = latitude;
        this.state = state;
    }

    public String getExpressid() {

        return expressid;
    }

    public void setExpressid(String expressid) {
        this.expressid = expressid;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSendertel() {
        return sendertel;
    }

    public void setSendertel(String sendertel) {
        this.sendertel = sendertel;
    }

    public String getSendercity() {
        return sendercity;
    }

    public void setSendercity(String sendercity) {
        this.sendercity = sendercity;
    }

    public String getSenderadress() {
        return senderadress;
    }

    public void setSenderadress(String senderadress) {
        this.senderadress = senderadress;
    }

    public String getAddressee() {
        return Addressee;
    }

    public void setAddressee(String addressee) {
        Addressee = addressee;
    }

    public String getAddresseetel() {
        return addresseetel;
    }

    public void setAddresseetel(String addresseetel) {
        this.addresseetel = addresseetel;
    }

    public String getAddresseecity() {
        return addresseecity;
    }

    public void setAddresseecity(String addresseecity) {
        this.addresseecity = addresseecity;
    }

    public String getAddresseeadress() {
        return addresseeadress;
    }

    public void setAddresseeadress(String addresseeadress) {
        this.addresseeadress = addresseeadress;
    }

    public int getCompanyid() {
        return companyid;
    }

    public void setCompanyid(int companyid) {
        this.companyid = companyid;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
