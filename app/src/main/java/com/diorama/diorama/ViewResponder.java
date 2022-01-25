package com.diorama.diorama;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

//import com.amb.bo.DbOper;
import com.amb.bo.AcceptDetailsBo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.diorama.diorama.Utility.dbName;
import static com.diorama.diorama.Utility.dbPassword;
import static com.diorama.diorama.Utility.dbUser;
import static com.diorama.diorama.Utility.hostName;


public class ViewResponder extends AppCompatActivity {

    Button viewResp,approveResponder;
    ListView responderList,approvList;
    int status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_responder);
        viewResp=(Button) findViewById(R.id.respView);
        approveResponder=(Button) findViewById(R.id.approveRes);
        approvList = (ListView) findViewById(R.id.approveList);
        responderList = (ListView) findViewById(R.id.userlist);
        viewResp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                approvList.setVisibility(View.GONE);
                responderList.setVisibility(View.VISIBLE);
                status=0;
                new AsyncCaller().execute();

//                DbOper oper=new DbOper();
//                ArrayList<IncidenceBo> pandingRequest = oper.getIncidenceDetails(0);
//                SocketProg prog=new SocketProg();
//                ArrayList<IncidenceBo> pandingRequest = prog.getPandingRequest(0);

//                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
//                startActivity(intent);
            }
        });
        approveResponder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                approvList.setVisibility(View.VISIBLE);
                responderList.setVisibility(View.GONE);
                status=1;
                new AsyncCaller().execute();
//                DbOper oper=new DbOper();
//                ArrayList<IncidenceBo> pandingRequest = oper.getIncidenceDetails(1);
//                SocketProg prog=new SocketProg();
//                ArrayList<IncidenceBo> pandingRequest = prog.getPandingRequest(1);

            }
        });
    }


    private class AsyncCaller extends AsyncTask<Void, Void, ArrayList<AcceptDetailsBo>>
    {
        ProgressDialog pdLoading = new ProgressDialog(ViewResponder.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }
        @Override
        protected ArrayList<AcceptDetailsBo> doInBackground(Void... params) {
            Connection con=null;
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con= DriverManager.getConnection("jdbc:mysql://"+hostName+"/"+dbName+"",dbUser,dbPassword);
                ArrayList<AcceptDetailsBo> list = new ArrayList<>();
                PreparedStatement ps=null;
                if (status==1) {
                     ps = con.prepareStatement("select incidence.id,currentlocation,criticallevel,r.username, IF(incstatus=1,'Accepted','pending') As us,latitude,longitude,s.username  from incidence" +
                            " INNER JOIN registration r on r.id=incidence.userid"
                            + " inner join registration s on  incidence.resId=s.id where incstatus='1'");
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        AcceptDetailsBo bo = new AcceptDetailsBo();
                        bo.setId(rs.getString(1));
                        bo.setAddress(rs.getString(2));
                        bo.setStatusLevel(rs.getString(3));
                        bo.setUserName(rs.getString(4));
                        bo.setActualStatus(rs.getString(5));
//                        bo.setLatitude(rs.getString(6));
//                        bo.setLongitude(rs.getString(7));
                        bo.setResponderName(rs.getString(8));
                        list.add(bo);
                    }
                }else if(status==0){
                     ps = con.prepareStatement("select incidence.id,currentlocation,criticallevel,r.username, IF(incstatus=1,'Accepted','pending') As us,latitude,longitude  from incidence" +
                            " INNER JOIN registration r on r.id=incidence.userid"+" where incstatus='0'");
//                            + " inner join registration s on  incidence.resId=s.id
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            AcceptDetailsBo bo = new AcceptDetailsBo();
                            bo.setId(rs.getString(1));
                            bo.setAddress(rs.getString(2));
                            bo.setStatusLevel(rs.getString(3));
                            bo.setUserName(rs.getString(4));
                            bo.setActualStatus(rs.getString(5));
//                        bo.setLatitude(rs.getString(6));
//                        bo.setLongitude(rs.getString(7));
//                            bo.setResponderName(rs.getString(8));
                            list.add(bo);
                        }
                }
//                ResultSet rs = ps.executeQuery();
//                while (rs.next()) {
//                    IncidenceBo bo = new IncidenceBo();
//                    bo.setId(rs.getString(1));
//                    bo.setCurrentlocation(rs.getString(2));
//                    bo.setCriticallevel(rs.getString(3));
//                    bo.setUserid(rs.getString(4));
//                    bo.setIncstatus(rs.getString(5));
//                    bo.setLatitude(rs.getString(6));
//                    bo.setLongitude(rs.getString(7));
//                    list.add(bo);

//                }
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
        protected void onPostExecute(ArrayList<AcceptDetailsBo> result) {
            super.onPostExecute(result);
            if(result!=null) {
                if (status == 0) {
                    CustomResponderList listImage = new CustomResponderList(getApplicationContext(), result);
                    responderList.setAdapter(listImage);
                } else if (status == 1) {
                    CustomResponderList listImage = new CustomResponderList(getApplicationContext(), result);
                    approvList.setAdapter(listImage);
                }
            }
            //this method will be running on UI thread

            pdLoading.dismiss();
        }

    }
}
