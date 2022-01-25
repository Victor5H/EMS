package com.diorama.diorama;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import static android.content.Context.MODE_PRIVATE;
import static com.diorama.diorama.MainActivity.MY_PREFS_NAME;
import static com.diorama.diorama.Utility.dbName;
import static com.diorama.diorama.Utility.dbPassword;
import static com.diorama.diorama.Utility.dbUser;
import static com.diorama.diorama.Utility.hostName;
import static com.diorama.diorama.Utility.loginUser;
import static com.diorama.diorama.Utility.responderId;
import static com.diorama.diorama.Utility.userId;


public class ResponderLogin extends AppCompatActivity {

    Button resLogin,homeAdmin;
    EditText _userEmail,_userPassword;
    RegisterBo bo=new RegisterBo();
    SQLiteDatabase sd = null;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responder_login);
        homeAdmin=(Button) findViewById(R.id.adminHome);
        resLogin=(Button) findViewById(R.id.respLogin);
        sd = this.openOrCreateDatabase("MYDB", MODE_PRIVATE, null);
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        _userEmail=(EditText) findViewById(R.id.edemail);
        _userPassword=(EditText) findViewById(R.id.edpassword);
        homeAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        resLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String   EmailID = _userEmail.getText().toString();
                String   Pass = _userPassword.getText().toString();

                if (EmailID.isEmpty() || EmailID.equalsIgnoreCase("") || Pass.isEmpty() || Pass.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter User Name or Password...", Toast.LENGTH_LONG).show();
                } else {

                    bo.setUsername(EmailID);
                    bo.setPassword(Pass);
//                    DbOper oper=new DbOper();
//                     bo1 = oper.getUserDetails(bo);
//
                        new AsyncCaller().execute();

//                                }
//
//                            } while (c.moveToNext());
//                        }
//                    }
                }






            }
        });
    }

    private class AsyncCaller extends AsyncTask<Void, Void, RegisterBo>
    {
        ProgressDialog pdLoading = new ProgressDialog(ResponderLogin.this);

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
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con= DriverManager.getConnection("jdbc:mysql://"+hostName+"/"+dbName+"",dbUser,dbPassword);
                RegisterBo rb = new RegisterBo();
                PreparedStatement ps = con.prepareStatement("select id,usertype from registration where username='" + bo.getUsername() + "'AND password='" + bo.getPassword() + "' AND usertype='"+2+"'");
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
            if(result.getId()!=null) {
                if (result.getUsertype().equalsIgnoreCase("2")) {
                    editor.putString(responderId, result.getId());
                    editor.putString(loginUser, result.getUsertype());

                    editor.apply();
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), ResponderView.class);
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
