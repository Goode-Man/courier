package com.example.machenike.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.machenike.model.Courier;
import com.example.machenike.model.Express;
import com.example.machenike.model.Staterecord;
import com.example.machenike.model.Template;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by hasee on 2016/4/15.
 */
public class DBmanager {
    private MySqLite mysqlite;
    private SQLiteDatabase db;
    private Context context;

    public DBmanager(Context context) {
        this.context=context;
        mysqlite=new MySqLite(context);
        db=mysqlite.getWritableDatabase();
    }


    //Courier
    public Courier LoginSelectBycourierid(int courierid){
        String sql="Select * from courier where courierid=?";
        String[]datas={Integer.toString(courierid)};
        Cursor c=db.rawQuery(sql, datas);
        Courier courier=null;
        while(c.moveToNext()){
            String couriername=c.getString(c.getColumnIndex("couriername"));
            String username=c.getString(c.getColumnIndex("username"));
            String password=c.getString(c.getColumnIndex("password"));
            int depotid=c.getInt(c.getColumnIndex("depotid"));
            String phone=c.getString(c.getColumnIndex("phone"));
            courier=new Courier(couriername,username,password,depotid,phone);
        }
        c.close();
        return courier;
    }

    public void LoginInsert(Courier data){
        String sql="insert into courier(courierid,couriername,username,password,depotid,phone) values(?,?,?,?,?,?)";
        Object[]datas={data.getCourierid(),data.getCouriername(),data.getUsername(),data.getPassword(),data.getDepotid(),data.getPhone()};
        db.execSQL(sql, datas);
    }

    public void UpdatePassword(int courierid,String password){
        String sql="UPDATE courier SET password=? where courierid=?";
        Object[]datas={password,courierid};
        db.execSQL(sql, datas);
    }

    public void UpdatePhone(int courierid,String phone){
        String sql="UPDATE courier SET phone=? where courierid=?";
        Object[]datas={phone,courierid};
        db.execSQL(sql, datas);
    }



    //Express
    public Express selectByExpressId(String expressid){
        String sql="SELECT * FROM express where expressid=?";
        String[] datas={String.valueOf(expressid)};
        Cursor c=db.rawQuery(sql,datas);
        Express express=null;
        while(c.moveToNext()){
            String sender=c.getString(c.getColumnIndex("sender"));
            String sendertel=c.getString(c.getColumnIndex("sendertel"));
            String sendercity=c.getString(c.getColumnIndex("sendercity"));
            String senderadress=c.getString(c.getColumnIndex("senderadress"));
            String Addressee=c.getString(c.getColumnIndex("Addressee"));
            String addresseetel=c.getString(c.getColumnIndex("addresseetel"));
            String addresseecity=c.getString(c.getColumnIndex("addresseecity"));
            String addresseeadress=c.getString(c.getColumnIndex("addresseeadress"));
            int companyid=c.getInt(c.getColumnIndex("companyid"));
            double longitude=c.getFloat(c.getColumnIndex("longitude"));
            double latitude=c.getFloat(c.getColumnIndex("latitude"));
            int state=c.getInt(c.getColumnIndex("state"));

            express=new Express(expressid,sender,sendertel,sendercity,senderadress,
                    Addressee,addresseetel,addresseecity,addresseeadress,
                    companyid,longitude,latitude,state);
        }
        c.close();
        return express;
    }

    public void UpdateExpressState(String expressid,int state){
        String sql="UPDATE express SET state=? where expressid=?";
        Object[]datas={state,expressid};
        db.execSQL(sql, datas);
    }

    public void UpdateExpressId(String expressid,int sendertel){
        String sql="UPDATE express SET expressid=? where sendertel=?";
        Object[]datas={expressid,sendertel};
        db.execSQL(sql, datas);
    }

    public List<Express> selectExpressByState(int state) {
        List<Express> list = new ArrayList<Express>();
        String sql = "SELECT * FROM express where state=?";
        String[] datas = {Integer.toString(state)};
        Cursor c = db.rawQuery(sql, datas);
        while (c.moveToNext()) {
            String expressid=c.getString(c.getColumnIndex("expressid"));
            String sender=c.getString(c.getColumnIndex("sender"));
            String sendertel=c.getString(c.getColumnIndex("sendertel"));
            String sendercity=c.getString(c.getColumnIndex("sendercity"));
            String senderadress=c.getString(c.getColumnIndex("senderadress"));
            String Addressee=c.getString(c.getColumnIndex("Addressee"));
            String addresseetel=c.getString(c.getColumnIndex("addresseetel"));
            String addresseecity=c.getString(c.getColumnIndex("addresseecity"));
            String addresseeadress=c.getString(c.getColumnIndex("addresseeadress"));
            int companyid=c.getInt(c.getColumnIndex("companyid"));
            double longitude=c.getFloat(c.getColumnIndex("longitude"));
            double latitude=c.getFloat(c.getColumnIndex("latitude"));

            Express express = new Express(expressid,sender,sendertel,sendercity,senderadress,
                    Addressee,addresseetel,addresseecity,addresseeadress,
                    companyid,longitude,latitude,state);
            list.add(express);
        }
        return list;
    }

    public boolean judgeExpress(String expressId){
        String sql = "SELECT * FROM express where expressid=?";
        String[] datas = {expressId};
        Cursor c = db.rawQuery(sql, datas);
        int companyid=0;
        while (c.moveToNext()) {
            companyid=c.getInt(c.getColumnIndex("companyid"));
        }
        if (companyid==0){
            return true;
        }else{
            return false;
        }
    }

    public boolean judgeNullExpress(String tel){
        String sql = "SELECT * FROM express where sendertel=?";
        String[] datas = {tel};
        Cursor c = db.rawQuery(sql, datas);
        int companyid=0;
        while (c.moveToNext()) {
            companyid=c.getInt(c.getColumnIndex("companyid"));
        }
        if (companyid==0){
            return true;
        }else{
            return false;
        }
    }

    public void ExpressInsert(List<Express> data){
        db.beginTransaction();
        try{
            //批量处理操作
        for(int i=0;i<data.size();i++){
            String sql="insert into Express(expressid,sender,sendertel,sendercity,senderadress,addressee,addresseetel,addresseecity,addresseeadress,companyid,longitude,latitude,state) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
            Object[]datas={data.get(i).getExpressid(),data.get(i).getSender(),data.get(i).getSendertel(),data.get(i).getSendercity(),data.get(i).getSenderadress(),
                    data.get(i).getAddressee(),data.get(i).getAddresseetel(),data.get(i).getAddresseecity(),data.get(i).getAddresseeadress(),
                    data.get(i).getCompanyid(),data.get(i).getLongitude(),data.get(i).getLatitude(),data.get(i).getState()};
            db.execSQL(sql, datas);
            db.setTransactionSuccessful();
        }
        }catch(Exception e){
            System.out.print(e.toString());
        }finally{
            db.endTransaction(); //处理完成
        }
    }





    //Staterecord
    public List<Staterecord> selectRecondByCourierid(int courierid) {
        List<Staterecord> list = new ArrayList<Staterecord>();
        String sql = "SELECT * FROM staterecord where courierid=?";
        String[] datas = {Integer.toString(courierid)};
        Cursor c = db.rawQuery(sql, datas);
        while (c.moveToNext()) {
            int id=c.getInt(c.getColumnIndex("id"));
            String datetime=c.getString(c.getColumnIndex("datetimes"));
            int state=c.getInt(c.getColumnIndex("state"));
            String expressid=c.getString(c.getColumnIndex("expressid"));

            Staterecord staterecord = new Staterecord(id,courierid,datetime,state,expressid);
            list.add(staterecord);
        }
        return list;
    }


    public List<Staterecord> selectRecondByCourierid(int courierid,int state) {
        List<Staterecord> list = new ArrayList<Staterecord>();
        String sql = "SELECT * FROM staterecord where courierid=? and state=?";
        String[] datas = {Integer.toString(courierid),Integer.toString(state)};
        Cursor c = db.rawQuery(sql, datas);
        while (c.moveToNext()) {
            int id=c.getInt(c.getColumnIndex("id"));
            String datetime=c.getString(c.getColumnIndex("datetimes"));
            String expressid=c.getString(c.getColumnIndex("expressid"));

            Staterecord staterecord = new Staterecord(id,courierid,datetime,state,expressid);
            list.add(staterecord);
        }
        return list;
    }

    public void InsertStateRecord(Staterecord data){
    String sql="insert into Staterecord(courierid,datetimes,state,expressid)values(?,?,?,?)";
        Object[] datas={data.getCourierid(),data.getDatetimes(),data.getState(),data.getExpressid()};
        db.execSQL(sql,datas);
    }




    //Template
    public void InsertTemplate(String templateText){
        String sql="insert into Template(templateText)values(?)";
        Object[] datas={templateText};
        db.execSQL(sql,datas);
    }


    public void deleteTemplate(int id){
        String sql="delete from template where id=?;";
        Object[] datas={id};
        db.execSQL(sql,datas);
    }

    public List<Template> selectTemplate() {
        List<Template> list = new ArrayList<Template>();
        String sql = "SELECT * FROM Template";
        String[] datas = {};
        Cursor c = db.rawQuery(sql, datas);
        while (c.moveToNext()) {
            String templateText=c.getString(c.getColumnIndex("templateText"));
            int id=c.getInt(c.getColumnIndex("id"));
            Template template = new Template(id,templateText);
            list.add(template);
        }
        return list;
    }
}
