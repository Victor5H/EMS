package com.diorama.diorama;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class DbQuery {

    SQLiteDatabase sd = null;
    public DbQuery(Context context){
        sd = context.openOrCreateDatabase("MYDB", MODE_PRIVATE, null);
        sd.execSQL("create table if not exists details (id INTEGER PRIMARY KEY AUTOINCREMENT,userName text, address text,level text,lat text,log text,currdate text)");
        sd.execSQL("create table if not exists registration (username text,password text,mobile text,address text,sex text ,hospital text, imei text, driving text,usertype text )");
        sd.execSQL("create table if not exists imagedetails (id INTEGER,photo blob);");
    }

    public ArrayList<ResponderListBo> getResponderDetails(){
        try {
            Cursor c= sd.rawQuery("Select username,mobile,address,hospital,imei,driving from registration where usertype=?",new String[]{"responder"});
            ArrayList<ResponderListBo> list=new ArrayList<>();

            if (c != null) {

                if (c.moveToFirst()) {
                    do {
                        ResponderListBo bo=new ResponderListBo();
                        bo.setName(c.getString(0));
                        bo.setMobile(c.getString(1));
                        bo.setAddress(c.getString(2));
                        bo.setHospitalName(c.getString(3));
                        bo.setIemiNo(c.getString(4));
                        bo.setDrivingLicence(c.getString(5));
                        list.add(bo);

                    } while (c.moveToNext());
                }
            }
            return list;
                    }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }

    public ArrayList<Double> getLatLon(String id){
        Cursor c= sd.rawQuery("Select lat,log from details where id=?",new String []{id});
        ArrayList<Double> arrayList=new ArrayList<>();
        if (c != null) {

            if (c.moveToFirst()) {
                do {

                    arrayList.add(c.getDouble(0));
                    arrayList.add(c.getDouble(1));

//                    arrayList.add(photo);
                } while (c.moveToNext());
            }
        }
        return arrayList;

    }

    public ArrayList<byte[]> getImageData(String id){
        Cursor c= sd.rawQuery("Select photo from imagedetails where id=?",new String []{id});
       ArrayList<byte[]> arrayList=new ArrayList<>();
        if (c != null) {

            if (c.moveToFirst()) {
                do {

                    byte[] photo = c.getBlob(0);
                   arrayList.add(photo);
                } while (c.moveToNext());
            }
        }
        return arrayList;

    }


    public ArrayList<UserDetailsBo> getUserDetails(){
        Cursor c= sd.rawQuery("Select username,mobile,address,sex from registration where usertype='user'",null);
//        Cursor c = sd.query("registration",new String[]{"username,mobile,address,sex"}, null,null,null,null,null);
       ArrayList<UserDetailsBo> bos=new ArrayList<>();

        if (c != null) {
            int i = 0;
            String userName = "";
            String currd = "";
            if (c.moveToFirst()) {
                do {
                    UserDetailsBo bo=new UserDetailsBo();
                    String user = c.getString(0);
                    String mobile = c.getString(1);
                    String address = c.getString(2);
                    String gender= c.getString(3);
                    bo.setUserName(user);
                    bo.setAddress(address);
                    bo.setMobileNo(mobile);
                    bo.setGender(gender);
                    bos.add(bo);
                } while (c.moveToNext());
            }
        }
        return bos;
    }

    public ArrayList<IncidenceListBo> getIncidenceDetails(){
        try{//Cursor findEntry = db.query("sku_table", columns, "owner=?", new String[] { owner }, null, null, null);
            ArrayList<IncidenceListBo> bos=new ArrayList<>();
            Cursor c = sd.query("details",new String[]{"id,userName,level,currdate"}, null,null,null,null,null);
            if (c != null) {
                int i=0;
                String userName="";
                String currd="";
                if (c.moveToFirst()) {
                    do {
                        String id = c.getString(0);
                        String user = c.getString(1);
                        String level = c.getString(2);

                        String currdate = c.getString(3);
//                        byte[] blob = c.getBlob(4);

//                        if(Integer.parseInt(imageSize)!=i){
//                            System.out.println(user+"\n"+level);
//                           userName=user;
//                           currd=currdate;
                           if(!user.equalsIgnoreCase(userName)||!currdate.equalsIgnoreCase(currd))
                           {
                               String s = currdate.replaceAll("\\+", "-").replaceAll("_", " ");

                               IncidenceListBo bo=new IncidenceListBo();
                               bo.setId(id);
                               bo.setCondition(level);
                               bo.setDateCurrent(s);
                               bo.setUserName(user);
                               System.out.println(user+"\n"+level);
                               userName=user;
                               currd=currdate;
                               bos.add(bo);
                           }
//                            i=Integer.parseInt(imageSize);
//                        }
//                        i--;




                    } while (c.moveToNext());
                }
            }
            return bos;
                    }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }

}
