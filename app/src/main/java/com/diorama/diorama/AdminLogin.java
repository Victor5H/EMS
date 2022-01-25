package com.diorama.diorama;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.amb.bo.DbOper;
import com.amb.bo.RegisterBo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.diorama.diorama.Utility.dbName;
import static com.diorama.diorama.Utility.dbPassword;
import static com.diorama.diorama.Utility.dbUser;
import static com.diorama.diorama.Utility.hostName;
import static com.diorama.diorama.Utility.userId;
import static com.diorama.diorama.Utility.userName;

public class AdminLogin extends AppCompatActivity {

    Button adLogin,homeMain;
    RegisterBo bo=new RegisterBo();
    EditText user,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        adLogin=(Button)findViewById(R.id.adminLog);
        homeMain=(Button) findViewById(R.id.mainHome);
        user=(EditText) findViewById(R.id.ed_email);
        pass=(EditText) findViewById(R.id.ed_password);

        adLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bo.setUsername(user.getText().toString());
                bo.setPassword(pass.getText().toString());
                new AsyncCaller().execute();

            }
        });
        homeMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });


    }
    private class AsyncCaller extends AsyncTask<Void, Void, RegisterBo>
    {
        ProgressDialog pdLoading = new ProgressDialog(AdminLogin.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tChecking Credentials...");
            pdLoading.show();
        }
        @Override
        protected RegisterBo doInBackground(Void... params) {
            Connection con=null;
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con= DriverManager.getConnection("jdbc:mysql://"+hostName+"/"+dbName+"",dbUser,dbPassword);
                RegisterBo rb = new RegisterBo();
                PreparedStatement ps = con.prepareStatement("select id,usertype from registration where username='" + bo.getUsername() + "'AND password='" + bo.getPassword() + "' AND usertype='"+0+"'");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    rb.setId(rs.getString(1));
                    rb.setUsertype(rs.getString(2));
                }
                return rb;
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
        protected void onPostExecute(RegisterBo result) {
            super.onPostExecute(result);
        if(result!=null) {
            if(result.getId()!=null) {
                if (result.getUsertype().equalsIgnoreCase("0")) {

                    Intent intent = new Intent(getApplicationContext(), AdminView.class);
                    startActivity(intent);
                    finish();
                }
            }else {
                Toast.makeText(getApplicationContext(), "Please Check Username or Password", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Please Check Details...",Toast.LENGTH_LONG).show();
        }
            //this method will be running on UI thread

            pdLoading.dismiss();
        }

    }
}
