package com.diorama.diorama;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amb.bo.AcceptDetailsBo;
import com.amb.bo.IncidenceBo;

import java.util.ArrayList;


public class CustomResponderList extends BaseAdapter {
    Context context;
    ArrayList<AcceptDetailsBo> countryList;
    int flags[];
    LayoutInflater inflter;

    public CustomResponderList(Context applicationContext, ArrayList<AcceptDetailsBo> countryList) {
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
        view = inflter.inflate(R.layout.custom_responder_list, null);
       TextView serialNo=(TextView) view.findViewById(R.id.serialNo);
        TextView userNameInc=(TextView) view.findViewById(R.id.userNameInc);
        TextView responderName=(TextView)view.findViewById(R.id.responderName);
        TextView statusLevel=(TextView)view.findViewById(R.id.statusLevel);
        TextView actualStatus=(TextView)view.findViewById(R.id.actualStatus);
        AcceptDetailsBo bo = countryList.get(i);
        serialNo.setText(bo.getId());
        userNameInc.setText(bo.getUserName());
        responderName.setText(bo.getResponderName());
        statusLevel.setText(bo.getStatusLevel());
        actualStatus.setText(bo.getActualStatus());
//        ImageView viewPic=(ImageView) view.findViewById(R.id.viewPic);
//        byte[] bytes = countryList.get(i);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//        viewPic.setImageBitmap(bitmap);

        return view;
    }
}
