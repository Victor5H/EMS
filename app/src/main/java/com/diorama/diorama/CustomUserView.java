package com.diorama.diorama;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;



public class CustomUserView  extends BaseAdapter {
    Context context;
    ArrayList<UserDetailsBo> countryList;
    int flags[];
    LayoutInflater inflter;

    public CustomUserView(Context applicationContext, ArrayList<UserDetailsBo> countryList) {
        this.context = context;//todo: maybe application context
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
        view = inflter.inflate(R.layout.user_list_view, null);
        TextView userName=(TextView) view.findViewById(R.id.uName);
        TextView uAddress=(TextView) view.findViewById(R.id.uAddress);
        TextView uMobile=(TextView) view.findViewById(R.id.uMobile);
        TextView uGender=(TextView) view.findViewById(R.id.uGender);
        UserDetailsBo bo = countryList.get(i);
        userName.setText(bo.getUserName());
        uAddress.setText(bo.getAddress());
        uMobile.setText(bo.getMobileNo());
        uGender.setText(bo.getGender());

/*
userName.setText( bo.getUserName());
dateCurrent.setText(bo.getDateCurrent());
condition.setText(bo.getCondition());
*/

        return view;
    }
}
