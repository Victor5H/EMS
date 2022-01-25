package com.diorama.diorama;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amb.bo.HospitalDetailsBo;
import com.amb.bo.RegisterBo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.diorama.diorama.Utility.dbName;
import static com.diorama.diorama.Utility.dbPassword;
import static com.diorama.diorama.Utility.dbUser;
import static com.diorama.diorama.Utility.hostName;

public class ResponderRegister extends AppCompatActivity {

    Button logResponder,home;
    SQLiteDatabase sd = null;
    RegisterBo bo = new RegisterBo();
    EditText user,pass,mobile,lic,hospital,address,imei;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responder_register);
        logResponder=(Button) findViewById(R.id.loginRes);
        user=(EditText) findViewById(R.id.ed_resuser);
        pass=(EditText) findViewById(R.id.ed_respass);
        mobile=(EditText) findViewById(R.id.ed_resmobile);
        lic=(EditText) findViewById(R.id.ed_reslic);
//        hospital=(EditText) findViewById(R.id.ed_reshospital);
        address=(EditText) findViewById(R.id.ed_resaddress);
        imei=(EditText) findViewById(R.id.ed_imei);
        spinner=(Spinner) findViewById(R.id.spHospitalName);
//        spinner.setBackgroundColor(Color.BLACK);
        spinner.setDrawingCacheBackgroundColor(Color.BLACK);
        new AsyncCaller1().execute();
//        sd = this.openOrCreateDatabase("MYDB", MODE_PRIVATE, null);
//        sd.execSQL("create table if not exists registration (username text,password text,mobile text,address text,sex text ,hospital text, imei text, driving text,usertype text )");




        home=(Button) findViewById(R.id.homeMain);
        logResponder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bo.setUsername(user.getText().toString());
                bo.setPassword(pass.getText().toString());
                bo.setMobile(mobile.getText().toString());
                bo.setAddress(address.getText().toString());
                bo.setHospitalname(spinner.getSelectedItem().toString());
                bo.setDrivinglicence(lic.getText().toString());
                bo.setImeino(imei.getText().toString());
                bo.setUsertype("2");
                new AsyncCaller().execute();


            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }


    private class AsyncCaller extends AsyncTask<Void, Void, Integer>
    {
        ProgressDialog pdLoading = new ProgressDialog(ResponderRegister.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }
        @Override
        protected Integer doInBackground(Void... params) {
            Connection con=null;
            try {
                int selectedItemPosition = spinner.getSelectedItemPosition();
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con= DriverManager.getConnection("jdbc:mysql://"+hostName+"/"+dbName+"",dbUser,dbPassword);
                PreparedStatement ps = con.prepareStatement("insert into registration (username,password,mobile,address,gender,hospitalname,imeino,drivinglicence,usertype) values (?,?,?,?,?,?,?,?,?)");
                int hosId=selectedItemPosition+1;
                ps.setString(1, bo.getUsername());
                ps.setString(2, bo.getPassword());
                ps.setString(3, bo.getMobile());
                ps.setString(4, bo.getAddress());
                ps.setString(5, bo.getGender());
                ps.setString(6, hosId+"");
                ps.setString(7, bo.getImeino());
                ps.setString(8, bo.getDrivinglicence());
                ps.setString(9, bo.getUsertype());
                return ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            } finally {
                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
//            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            if (result == 0) {
                Toast.makeText(getApplicationContext(),"Responder Registration Failed..",Toast.LENGTH_SHORT).show();
            } else{
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
            //this method will be running on UI thread

            pdLoading.dismiss();
        }

    }



    private class AsyncCaller1 extends AsyncTask<Void, Void, ArrayList<HospitalDetailsBo>>
    {
        ProgressDialog pdLoading = new ProgressDialog(ResponderRegister.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }
        @Override
        protected ArrayList<HospitalDetailsBo> doInBackground(Void... params) {
            Connection con=null;
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Class.forName("com.mysql.jdbc.Driver").newInstance();//hospitaldetail id,hospitalname,hospitaladdress,hoslat,hoslon,location
                con= DriverManager.getConnection("jdbc:mysql://"+hostName+"/"+dbName+"",dbUser,dbPassword);
                PreparedStatement ps = con.prepareStatement("select id,hospitalname,hospitaladdress,hoslat,hoslon,location from hospitaldetail");
                ResultSet rs= ps.executeQuery();
                ArrayList<HospitalDetailsBo> list=new ArrayList<>();
                while (rs.next()){
                    HospitalDetailsBo bo=new HospitalDetailsBo();
                    bo.setId(rs.getString(1));
                    bo.setHospitalname(rs.getString(2));
                    bo.setHospitaladdress(rs.getString(3));
                    bo.setHoslat(rs.getString(4));
                    bo.setHoslon(rs.getString(5));
                    bo.setLocation(rs.getString(6));
                    list.add(bo);
                }


//                ps.setString(1, bo.getUsername());
//                ps.setString(2, bo.getPassword());
//                ps.setString(3, bo.getMobile());
//                ps.setString(4, bo.getAddress());
//                ps.setString(5, bo.getGender());
//                ps.setString(6, bo.getHospitalname());
//                ps.setString(7, bo.getImeino());
//                ps.setString(8, bo.getDrivinglicence());
//                ps.setString(9, bo.getUsertype());
//                return ps.executeUpdate();
                return list;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
//            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<HospitalDetailsBo> result) {
            super.onPostExecute(result);

            List<String> list=new ArrayList<>();
            for (int i=0;i<result.size();i++){
                list.add(result.get(i).getHospitalname());
            }
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            //this method will  be running on UI thread

            pdLoading.dismiss();
        }

    }


}
