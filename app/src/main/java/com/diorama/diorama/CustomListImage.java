package com.diorama.diorama;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amb.bo.IncidenceBo;
import com.amb.bo.ResponderViewDataBo;
import com.amb.bo.ViewIncidenceResponder;

import java.text.DecimalFormat;
import java.util.ArrayList;



public class CustomListImage extends BaseAdapter {
    Context context;
    ArrayList<ViewIncidenceResponder> countryList;
    int flags[];
    LayoutInflater inflter;
    ArrayList<ResponderViewDataBo> dataBos;

    public CustomListImage(Context applicationContext, ArrayList<ViewIncidenceResponder> countryList, ArrayList<ResponderViewDataBo> dataBos) {
        this.context = context;
        this.countryList = countryList;
        this.dataBos = dataBos;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        if(countryList!=null) {
            return countryList.size();
        }else if (dataBos!=null){
            return dataBos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_listview, null);
        TextView id = (TextView) view.findViewById(R.id.idInc);
        TextView userName = (TextView) view.findViewById(R.id.userName);
        TextView dateCurrent = (TextView) view.findViewById(R.id.dateCurrent);
        TextView condition = (TextView) view.findViewById(R.id.conditionList);
        TextView approxTime=(TextView) view.findViewById(R.id.approxTimec);
        if(countryList!=null) {
            approxTime.setVisibility(View.VISIBLE);
            ViewIncidenceResponder bo = countryList.get(i);


            id.setText(bo.getId());
            userName.setText(bo.getUserName());
            dateCurrent.setText(bo.getCriticalLevel());
            condition.setText(bo.getDistance());
            approxTime.setText(bo.getApproxTime());
        }else if (dataBos!=null){
            ResponderViewDataBo bo = dataBos.get(i);
            int ii= i+1;
            id.setText(ii+"");
            approxTime.setVisibility(View.GONE);

            if(!bo.getUsername().equalsIgnoreCase("")){
                /*userName.setText(bo.getCurrentLocation());*/
                SocketProg prog = new SocketProg();
                DecimalFormat df21 = new DecimalFormat(".##");
                double distance = prog.distance(Double.parseDouble(bo.getHospitalDetailHoslat()), Double.parseDouble(bo.getHospitalDetailHoslon()), Double.parseDouble(bo.getLatitude()), Double.parseDouble(bo.getLongitude()));
                String format = df21.format(distance);
                double iii=  Double.parseDouble(format)*60/40;
//                DecimalFormat df21 = new DecimalFormat(".##");
                String format1 = df21.format(iii);
                approxTime.setVisibility(View.VISIBLE);
                userName.setText(bo.getUsername());
                condition.setText("Accepted");
                approxTime.setText(format+" KM\n"+format1+" min");
            }else{
                userName.setText(bo.getCurrentLocation());
                condition.setText("Pending Request");
            }


            dateCurrent.setText(bo.getCriticalLevel());


        }
//        if (bo.getIncstatus().equalsIgnoreCase("0")){
//            dateCurrent.setText("Pending");
//        }else if ( bo.getIncstatus().equalsIgnoreCase("1")) {
//            dateCurrent.setText("Approve");
//        }else if(bo.getIncstatus().equalsIgnoreCase("2"))
//        {
//            dateCurrent.setText("Completed");
//        }
//        condition.setText(bo.getCriticallevel());

        return view;
    }
}
