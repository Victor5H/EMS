package com.diorama.diorama;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;



public class CustomListResponder extends BaseAdapter {
    Context context;
    ArrayList<ResponderListBo> countryList;
    int flags[];
    LayoutInflater inflter;

    public CustomListResponder(Context applicationContext, ArrayList<ResponderListBo> countryList) {
        this.context = context;
        this.countryList = countryList;

        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return countryList.size();
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
        view = inflter.inflate(R.layout.responder_list_view, null);
//        TextView user=(TextView) view.findViewById(R.id.userView);
//        TextView mobile=(TextView) view.findViewById(R.id.mobileView);
//        TextView hospital=(TextView) view.findViewById(R.id.hospitalView);
//        TextView address=(TextView) view.findViewById(R.id.addressView);
//        TextView imei=(TextView) view.findViewById(R.id.imeiView);
//        TextView licence=(TextView) view.findViewById(R.id.licenceView);
//        ResponderListBo bo=countryList.get(i);
//        user.setText(bo.getName());
//        mobile.setText(bo.getMobile());
//        hospital.setText(bo.getHospitalName());
//        address.setText(bo.getAddress());
//        imei.setText(bo.getIemiNo());
//        licence.setText(bo.getDrivingLicence());
//        IncidenceListBo bo = countryList.get(i);
//        id.setText(bo.getId());
//        userName.setText( bo.getUserName());
//        dateCurrent.setText(bo.getDateCurrent());
//        condition.setText(bo.getCondition());

        return view;
    }
}
