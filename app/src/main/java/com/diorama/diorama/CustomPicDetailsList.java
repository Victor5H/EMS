package com.diorama.diorama;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;


public class CustomPicDetailsList extends BaseAdapter {
    Context context;
    ArrayList<byte[]> countryList;
    int flags[];
    LayoutInflater inflter;

    public CustomPicDetailsList(Context applicationContext, ArrayList<byte[]> countryList) {
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
        view = inflter.inflate(R.layout.custom_pic_details, null);
        ImageView viewPic=(ImageView) view.findViewById(R.id.viewPic);
        byte[] bytes = countryList.get(i);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        viewPic.setImageBitmap(bitmap);

        return view;
    }
}

