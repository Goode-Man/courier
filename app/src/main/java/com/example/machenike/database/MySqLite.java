package com.example.machenike.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MySqLite extends SQLiteOpenHelper{
    static String name="ledger";
    static int version=1;

    public MySqLite(Context context) {
        super(context, name, null, version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table courier(" +
                "courierid INTEGER PRIMARY KEY AUTOINCREMENT," +
                "couriername text," +
                "username text,"+
                "password text," +
                "depotid INTEGER,"+
                "phone text)";

        String sql1="create table express(" +
                "expressid text," +
                "sender text," +
                "sendertel text," +
                "sendercity text," +
                "senderadress text," +
                "Addressee text," +
                "addresseetel text," +
                "addresseecity text," +
                "addresseeadress text," +
                "companyid INTEGER," +
                "longitude double," +
                "latitude double," +
                "state INTEGER)";

        String sql2="create table staterecord(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "courierid INTEGER," +
                "datetimes text,"+
                "state INTEGER," +
                "expressid text)";

        String sql3="create table template(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "templateText text)";
        db.execSQL(sql);
        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);

//        String sql10="insert into express(expressid,Addressee,addresseetel,addresseecity,addresseeadress,state)values(?,?,?,?,?,?)";
//        String[] username={"name"};
//        for(int q=0;q<username.length;q++){
//            Object[] datas={"7100039312",username[q],2,"3","4",1};
//            db.execSQL(sql10,datas);
//        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }
}
