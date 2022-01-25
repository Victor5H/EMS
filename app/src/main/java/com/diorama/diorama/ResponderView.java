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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

//import com.amb.bo.DbOper;
import com.amb.bo.IncidenceBo;
import com.amb.bo.ResponderViewDataBo;
import com.amb.bo.ViewIncidenceResponder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.diorama.diorama.MainActivity.MY_PREFS_NAME;
import static com.diorama.diorama.Utility.dbName;
import static com.diorama.diorama.Utility.dbPassword;
import static com.diorama.diorama.Utility.dbUser;
import static com.diorama.diorama.Utility.hostName;
import static com.diorama.diorama.Utility.incId;
import static com.diorama.diorama.Utility.loginUser;
import static com.diorama.diorama.Utility.responderId;
import static com.diorama.diorama.Utility.userId;


public class ResponderView extends AppCompatActivity {
    LinearLayout viewLable;
    String resId;
    ListView listReport;
    String idres, status;
    SharedPreferences prefs;
    String uid;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responder_view);
        Button showRequest = (Button) findViewById(R.id.showAllRequest);
        Button acceptedRequest = (Button) findViewById(R.id.showAllAcceptedRequest);
        listReport = (ListView) findViewById(R.id.listReport);
        viewLable=(LinearLayout)  findViewById(R.id.viewLable);
        viewLable.setVisibility(View.GONE);
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        resId = prefs.getString(responderId, "");

        showRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prefs.getString(loginUser, "").equalsIgnoreCase("2")) {
                    idres = "0";
                    status = "0";
                } else if (prefs.getString(loginUser, "").equalsIgnoreCase("1")) {
                    uid = prefs.getString(userId, "");
                    status = "0";
                    idres = "0";
                }
                new AsyncCaller().execute();
//        SocketProg prog = new SocketProg();//todo:start
//               ArrayList<IncidenceBo> pandingRequest = prog.getPandingRequest(2);//todo:end

            }
        });

        acceptedRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prefs.getString(loginUser, "").equalsIgnoreCase("2")) {
                    idres = resId;
                    status = "1";
                } else if (prefs.getString(loginUser, "").equalsIgnoreCase("1")) {
                    uid = prefs.getString(userId, "");
                    status = "1";
//                    idres="0";
                }
                new AsyncCaller().execute();
            }
        });


    }

    private static DecimalFormat df2 = new DecimalFormat(".##");

    private class AsyncCaller extends AsyncTask<Void, Void, ArrayList<ArrayList<ResponderViewDataBo>>> {
        ProgressDialog pdLoading = new ProgressDialog(ResponderView.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tFetching Requests...");
            pdLoading.show();
        }

        @Override
        protected ArrayList<ArrayList<ResponderViewDataBo>> doInBackground(Void... params) {
            Connection con = null;
            String re="";
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://" + hostName + "/" + dbName + "", dbUser, dbPassword);
                ArrayList<ArrayList<ResponderViewDataBo>> lists = new ArrayList<>();
                ArrayList<ResponderViewDataBo> list = new ArrayList<>();
                if (prefs.getString(loginUser, "").equalsIgnoreCase("2")) {

                    PreparedStatement ps = con.prepareStatement("select incidence.id,currentlocation,criticallevel,username,incstatus," +
                            "latitude,longitude from incidence   INNER JOIN registration on registration.id=incidence.userid where " +
                            "incstatus='" + status + "' AND resId='" + idres + "'");
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        ResponderViewDataBo bo = new ResponderViewDataBo();
                        bo.setIncidenceId(rs.getString(1));
                        bo.setCurrentLocation(rs.getString(2));
                        bo.setCriticalLevel(rs.getString(3));
                        bo.setUsername(rs.getString(4));
                        bo.setIncStatus(rs.getString(5));
                        bo.setLatitude(rs.getString(6));
                        bo.setLongitude(rs.getString(7));
                        list.add(bo);

                    }
                    ArrayList<ResponderViewDataBo> list1 = new ArrayList<>();
                    if (list != null) {

                        PreparedStatement ps1 = con.prepareStatement("select hospitaldetail.hoslat,hospitaldetail.hoslon,registration.id,registration.username from registration inner join hospitaldetail on hospitaldetail.id=registration.hospitalname  where  usertype=2 and registration.id='" + resId + "'");
                        ResultSet resultSet = ps1.executeQuery();
                        while (resultSet.next()) {
                            ResponderViewDataBo bo = new ResponderViewDataBo();
                            bo.setHospitalDetailHoslat(resultSet.getString(1));
                            bo.setHospitalDetailHoslon(resultSet.getString(2));
                            bo.setRegistrationId(resultSet.getString(3));
                            list1.add(bo);
                        }
                    }
                    lists.add(list);
                    lists.add(list1);
                } else if (prefs.getString(loginUser, "").equalsIgnoreCase("1")) {
                    if (status.equalsIgnoreCase("1")) { /*select incidence.id,currentlocation,criticallevel,username,incstatus,latitude,longitude from incidence   INNER JOIN registration on registration.id=incidence.resId*/
                        PreparedStatement ps = con.prepareStatement("select incidence.id,currentlocation,criticallevel,username,incstatus,latitude,longitude,hospitaldetail.hoslat,hospitaldetail.hoslon from incidence   INNER JOIN registration on registration.id=incidence.resId Inner join hospitaldetail on hospitaldetail.id=registration.hospitalname     where incstatus='" + status + "' AND userid='" + uid + "'");
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            ResponderViewDataBo bo = new ResponderViewDataBo();
                            bo.setIncidenceId(rs.getString(1));
                            bo.setCurrentLocation(rs.getString(2));
                            bo.setCriticalLevel(rs.getString(3));
                            bo.setUsername(rs.getString(4));
                            bo.setIncStatus(rs.getString(5));
                            bo.setLatitude(rs.getString(6));
                            bo.setLongitude(rs.getString(7));
                            bo.setHospitalDetailHoslat(rs.getString(8));
                            bo.setHospitalDetailHoslon(rs.getString(9));

                            list.add(bo);
                        }
                        //re ="Latitude = "+lati+" Longitude= "+longi;
                        Log.e("infor",re);

                    } else if (status.equalsIgnoreCase("0")) {
                        PreparedStatement ps = con.prepareStatement("select incidence.id,currentlocation,criticallevel,incstatus,latitude,longitude from incidence  where incstatus=" + status + " AND userid=" + uid + " AND resId=" + idres + "");
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {ResponderViewDataBo bo = new ResponderViewDataBo();
                            bo.setIncidenceId(rs.getString(1));
                            bo.setCurrentLocation(rs.getString(2));
                            bo.setCriticalLevel(rs.getString(3));
//                           bo.setUsername(rs.getString(4));
                            bo.setUsername("");
                            bo.setIncStatus(rs.getString(4));
                            bo.setLatitude(rs.getString(5));
                            bo.setLongitude(rs.getString(6));
                            list.add(bo);
                        }
                    }
                    lists.add(list);
                }
                return lists;
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
        protected void onPostExecute(ArrayList<ArrayList<ResponderViewDataBo>> result) {
            super.onPostExecute(result);
            if (result != null) {
                if (prefs.getString(loginUser, "").equalsIgnoreCase("2")) {
                    viewLable.setVisibility(View.VISIBLE);
                    SocketProg prog = new SocketProg();
                    ArrayList<ResponderViewDataBo> list1 = result.get(0);
                    ArrayList<ResponderViewDataBo> list2 = result.get(1);
                    final ArrayList<ViewIncidenceResponder> arrayList = new ArrayList<>();
                    for (int j = 0; j < list1.size(); j++) {
                        ResponderViewDataBo bo = list1.get(j);
                        ResponderViewDataBo dataBo = list2.get(0);
                        double distance = prog.distance(Double.parseDouble(dataBo.getHospitalDetailHoslat()), Double.parseDouble(dataBo.getHospitalDetailHoslon()), Double.parseDouble(bo.getLatitude()), Double.parseDouble(bo.getLongitude()));
                        ViewIncidenceResponder responder = new ViewIncidenceResponder();
                        String format = df2.format(distance);
                        System.out.println("bo.getUsername() : : : "+bo.getUsername());
                        responder.setId(bo.getIncidenceId());
                        responder.setUserName(bo.getUsername());
                        responder.setCriticalLevel(bo.getCriticalLevel());
                        responder.setDistance(format + " KM");
                      double iii=  Double.parseDouble(format)*60/40;
                        DecimalFormat df21 = new DecimalFormat(".##");
                        String format1 = df21.format(iii);
                        responder.setApproxTime(format1+" min");
                        arrayList.add(responder);
                    }

                    CustomListImage listImage = new CustomListImage(getApplicationContext(), arrayList,null);

                    listReport.setAdapter(listImage);
                    if (status.equalsIgnoreCase("0")) {
                        listReport.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                ViewIncidenceResponder viewIncidenceResponder = arrayList.get(i);
                                String id = viewIncidenceResponder.getId();
                           Toast.makeText(getApplicationContext(), id, Toast.LENGTH_LONG).show();
                                editor.putString(incId, id);
                                editor.apply();
                                editor.commit();
                                Intent intent = new Intent(getApplicationContext(), AcceptedRequestActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        });
                    }
                    if (list1.size()==0){
                        Toast.makeText(getApplicationContext(),"No Record Found...",Toast.LENGTH_LONG).show();
                    }else {
                      //  viewLable.setVisibility(View.VISIBLE);
                    }
                }else if(prefs.getString(loginUser,"").equalsIgnoreCase("1")){
                    ArrayList<ResponderViewDataBo> list1 = result.get(0);

                    CustomListImage listImage = new CustomListImage(getApplicationContext(), null,list1);
                    listReport.setAdapter(listImage);
                    if (list1.size()==0){
                        Toast.makeText(getApplicationContext(),"No Record Found...",Toast.LENGTH_LONG).show();

                    }else {
                       // viewLable.setVisibility(View.VISIBLE);
                    }
                }
            }else {
                Toast.makeText(getApplicationContext(),"No Record Found...",Toast.LENGTH_LONG).show();
            }
            //this method will be running on UI thread

            pdLoading.dismiss();
        }

    }
}