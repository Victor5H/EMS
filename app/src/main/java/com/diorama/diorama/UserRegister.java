package com.diorama.diorama;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

//import com.amb.bo.DbOper;
import com.amb.bo.RegisterBo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.diorama.diorama.Utility.dbName;
import static com.diorama.diorama.Utility.dbPassword;
import static com.diorama.diorama.Utility.dbUser;
import static com.diorama.diorama.Utility.hostName;


public class UserRegister extends AppCompatActivity {

    EditText _useremailreg,_userpassreg,_useraddreg,_usermobreg;
    RadioGroup _radiomalefemale;
    RadioButton _sexbutton;
    Button userReg,Huser;
    RegisterBo bo=new RegisterBo();
    SQLiteDatabase sd = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        userReg=(Button) findViewById(R.id.regUser);
        Huser=(Button) findViewById(R.id.regHomeUser);

        _useraddreg=(EditText) findViewById(R.id.UserAddressReg);
        _useremailreg=(EditText) findViewById(R.id.UseremailReg);
        _usermobreg=(EditText) findViewById(R.id.UserMobileReg);
        _userpassreg=(EditText) findViewById(R.id.UserpasswordReg);
        _radiomalefemale=(RadioGroup) findViewById(R.id.malefemailgrp);
        sd = this.openOrCreateDatabase("MYDB", MODE_PRIVATE, null);
        sd.execSQL("create table if not exists registration (username text,password text,mobile text,address text,sex text ,hospital text, imei text, driving text,usertype text )");
        userReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              String userName=  _useremailreg.getText().toString();
              String userAddress=  _useraddreg.getText().toString();
              String userMobile=  _usermobreg.getText().toString();
              String userPassword=  _userpassreg.getText().toString();
              int _id=  _radiomalefemale.getCheckedRadioButtonId();
               _sexbutton=(RadioButton) findViewById(_id);
              String sex=  _sexbutton.getText().toString();
                if (userName.equals("") || userPassword.equals("") || userMobile.equals("") || userAddress.equals("") ) {
                    Toast.makeText(getApplicationContext(), "Please Fill all fields....", Toast.LENGTH_LONG).show();
                    return;
                } else {

                    bo.setUsername(userName);
                    bo.setAddress(userAddress);
                    bo.setGender(sex);
                    bo.setMobile(userMobile);
                    bo.setPassword(userPassword);
                    bo.setUsertype("1");
                    new AsyncCaller().execute();
//                    DbOper oper=new DbOper();
//                    int result = oper.insertUserDetails(bo);
//                    SocketProg prog=new SocketProg();
//                    int result = prog.sendUserData(bo);

//                    sd.execSQL("INSERT INTO registration (username,\n" +
//                            "password, \n" +
//                            "mobile, \n" +
//                            "address, \n" +
//                            "sex, \n" +
//                            "usertype ) VALUES(\'" + userName + "\', \'" + userPassword + "\', \'" + userMobile + "\',\'" + userAddress + "\',\'" + sex + "\',\'" + "user" + "\')");

                }
            }
        });

        Huser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private class AsyncCaller extends AsyncTask<Void, Void, Integer>
    {
        ProgressDialog pdLoading = new ProgressDialog(UserRegister.this);

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
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con= DriverManager.getConnection("jdbc:mysql://"+hostName+"/"+dbName+"",dbUser,dbPassword);
                PreparedStatement ps = con.prepareStatement("insert into registration (username,password,mobile,address,gender,hospitalname,imeino,drivinglicence,usertype) values (?,?,?,?,?,?,?,?,?)");
                ps.setString(1, bo.getUsername());
                ps.setString(2, bo.getPassword());
                ps.setString(3, bo.getMobile());
                ps.setString(4, bo.getAddress());
                ps.setString(5, bo.getGender());
                ps.setString(6, bo.getHospitalname());
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

            if (result==0){
                Toast.makeText(getApplicationContext(),"User Registration Failed..",Toast.LENGTH_SHORT).show();
            }else {
                _useremailreg.setText("");
                _userpassreg.setText("");
                _usermobreg.setText("");
                _useraddreg.setText("");

                Intent intent = new Intent(getApplicationContext(), UserLogin.class);
                startActivity(intent);
            }
            //this method will be running on UI thread

            pdLoading.dismiss();
        }

    }
}
