package com.diorama.diorama;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static com.diorama.diorama.MainActivity.MY_PREFS_NAME;
import static com.diorama.diorama.Utility.dbName;
import static com.diorama.diorama.Utility.dbPassword;
import static com.diorama.diorama.Utility.dbUser;
import static com.diorama.diorama.Utility.hostName;
import static com.diorama.diorama.Utility.incId;
import static com.diorama.diorama.Utility.responderId;


public class AcceptedRequestActivity extends AppCompatActivity {

    String resId, inc_Id;
    ListView listReport;
    SharedPreferences.Editor editor;// = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
    SharedPreferences prefs;// = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
    String lati="",longi="";
    String re;
    Button locate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_request);
        Button accept = (Button) findViewById(R.id.accept);
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        resId = prefs.getString(responderId, "");
        inc_Id = prefs.getString(incId, "");
        Button decline = (Button) findViewById(R.id.decline);
        listReport = (ListView) findViewById(R.id.requestAcceptList);
        locate = findViewById(R.id.button2);

        new AsyncCaller().execute();
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncCaller1().execute();
            }
        });
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ResponderView.class);
                startActivity(intent);
                finish();
            }
        });
        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(getApplicationContext(),Map_v.class);
                //i.putExtra("lati",lati);
                //i.putExtra("longi",longi);
                //startActivity(i);
            }
        });
    }

    private class AsyncCaller extends AsyncTask<Void, Void, ArrayList<byte[]>> {
        ProgressDialog pdLoading = new ProgressDialog(AcceptedRequestActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }

        @Override
        protected ArrayList<byte[]> doInBackground(Void... params) {
            Connection con = null;
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://" + hostName + "/" + dbName + "", dbUser, dbPassword);
//                ArrayList<ArrayList<ResponderViewDataBo>> lists = new ArrayList<>();
                ArrayList<byte[]> list = new ArrayList<>();
                PreparedStatement ps = con.prepareStatement("select pic from detailspic where inc_id='" + inc_Id + "'");

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    list.add(rs.getBytes(1));
                }

                try{
                    Statement ps1 = con.createStatement();
                    ResultSet rs1 = ps1.executeQuery("select latitude,longitude from incidence");
                    while(rs1.next()){
                        lati= rs1.getString(1);
                        longi = rs1.getString(2);
                    }
                    Log.e("info",lati+longi);
                }
                catch(Exception e){System.out.print(e);Log.e("error1"," "+e);}
               /* ArrayList<ResponderViewDataBo> list1=new ArrayList<>();
                PreparedStatement ps1=con.prepareStatement("select hospitaldetail.hoslat,hospitaldetail.hoslon,registration.id,registration.username from registration inner join hospitaldetail on hospitaldetail.id=registration.hospitalname  where  usertype=2 and registration.id='"+resId+"'");
                ResultSet resultSet= ps1.executeQuery();
                while (resultSet.next()){
                    ResponderViewDataBo bo = new ResponderViewDataBo();
                    bo.setHospitalDetailHoslat(resultSet.getString(1));
                    bo.setHospitalDetailHoslon(resultSet.getString(2));
                    bo.setRegistrationId(resultSet.getString(3));
                    list1.add(bo);
                }
                lists.add(list);
              */
//                lists.add(list);
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
        protected void onPostExecute(ArrayList<byte[]> result) {
            super.onPostExecute(result);
            if (result.size() != 0 || result != null) {
                CustomPicDetailsList listImage = new CustomPicDetailsList(getApplicationContext(), result);
//                responderList.setAdapter(listImage);
                listReport.setAdapter(listImage);

            }
            //this method will be running on UI thread

            pdLoading.dismiss();
        }

    }


    private class AsyncCaller1 extends AsyncTask<Void, Void, Integer> {
        ProgressDialog pdLoading = new ProgressDialog(AcceptedRequestActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            Connection con = null;
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://" + hostName + "/" + dbName + "", dbUser, dbPassword);
                PreparedStatement ps = con.prepareStatement("update incidence set incstatus=1,resId='" + resId + "' where id='" + inc_Id + "'");
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

            if (result != 0) {
                Intent intent = new Intent(getApplicationContext(), ResponderView.class);
                startActivity(intent);
                finish();
            }
//            if(result.size()!=0||result!=null) {
//                CustomPicDetailsList listImage = new CustomPicDetailsList(getApplicationContext(), result);
////                responderList.setAdapter(listImage);
//                listReport.setAdapter(listImage);
//
//            }
            //this method will be running on UI thread

            pdLoading.dismiss();
        }

    }


}
