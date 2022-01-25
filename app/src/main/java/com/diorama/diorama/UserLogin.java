package com.diorama.diorama;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.amb.bo.DbOper;
import com.amb.bo.RegisterBo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.diorama.diorama.MainActivity.MY_PREFS_NAME;
import static com.diorama.diorama.Utility.dbName;
import static com.diorama.diorama.Utility.dbPassword;
import static com.diorama.diorama.Utility.dbUser;
import static com.diorama.diorama.Utility.hostName;
import static com.diorama.diorama.Utility.loginUser;
import static com.diorama.diorama.Utility.userId;
import static com.diorama.diorama.Utility.userName;

public class UserLogin extends AppCompatActivity {

    Button logUser,hMain;
    TextView regUser;
    EditText _userEmail,_userPassword;
    SQLiteDatabase sd = null;
    String EmailID,Pass;
     SharedPreferences.Editor editor;
    RegisterBo bo = new RegisterBo();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        sd = this.openOrCreateDatabase("MYDB", MODE_PRIVATE, null);
         editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        _userEmail=(EditText) findViewById(R.id.Useremail);
        _userPassword=(EditText) findViewById(R.id.Userpassword);
        hMain=(Button) findViewById(R.id.userHome);
        regUser=(TextView) findViewById(R.id.userReg);
        logUser=(Button) findViewById(R.id.loginUser);

        hMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        regUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),UserRegister.class);
                startActivity(intent);
            }
        });
        logUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            try {
                 EmailID = _userEmail.getText().toString();
                 Pass = _userPassword.getText().toString();

                if (EmailID.isEmpty() || EmailID.equalsIgnoreCase("") || Pass.isEmpty() || Pass.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter User Name and Password...", Toast.LENGTH_LONG).show();
                } else {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);



                    bo.setUsername(EmailID);
                    bo.setPassword(Pass);
                    new AsyncCaller().execute();
//                    DbOper oper=new DbOper();
//                    RegisterBo bo1 = oper.getUserDetails(bo);
//                    SocketProg prog = new SocketProg();
//                    RegisterBo bo1 = prog.sendLoginDetails(bo);
//                    if (c != null) {
//                        if (c.moveToFirst()) {
//                            do {
//                                String n = c.getString(c.getColumnIndex("username"));
//                                String ci = c.getString(c.getColumnIndex("password"));
//                                String userType = c.getString(c.getColumnIndex("usertype"));


//                                if (n.equals(EmailID) && Pass.equals(ci)) {


                }

//                            } while (c.moveToNext());
//                        }
//                    }
//                }


            }catch (Exception e){e.printStackTrace();}

            }
        });


    }

    private class AsyncCaller extends AsyncTask<Void, Void, RegisterBo>
    {
        ProgressDialog pdLoading = new ProgressDialog(UserLogin.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tChecking credentials...");
            pdLoading.show();
        }
        @Override
        protected RegisterBo doInBackground(Void... params) {
            Connection con=null;
            try {
                ArrayList<RegisterBo> list=new ArrayList<>();
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con= DriverManager.getConnection("jdbc:mysql://"+hostName+"/"+dbName+"",dbUser,dbPassword);
                RegisterBo rb = new RegisterBo();
                PreparedStatement ps = con.prepareStatement("select id,usertype from registration where username='" + bo.getUsername() + "'AND password='" + bo.getPassword() + "' AND usertype='"+1+"'");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {

                    rb.setId(rs.getString(1));
                    rb.setUsertype(rs.getString(2));
                    list.add(rb);
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

            if(result.getId()!=null) {
                if (result.getUsertype().equalsIgnoreCase("1")) {
                    editor.putString(userName, EmailID);
                    editor.putString(userId, result.getId());
                    editor.putString(loginUser, result.getUsertype());

                    editor.apply();
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), UserHome.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Please Check Username or Password", Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(getApplicationContext(), "Please Check Username or Password", Toast.LENGTH_LONG).show();
            }
            //this method will be running on UI thread

            pdLoading.dismiss();
        }

    }
}