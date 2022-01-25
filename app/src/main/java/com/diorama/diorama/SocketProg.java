package com.diorama.diorama;

import android.os.StrictMode;

import com.amb.bo.IncidenceBo;

import java.util.ArrayList;
import java.net.*;
import java.io.*;
public class SocketProg {
    public String ipAddress="cloudbin.net";
//
//    public int sendUserData(RegisterBo bo){
//        try {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//            Socket socket=new Socket(ipAddress,8546);
//            ObjectOutputStream output=new ObjectOutputStream(socket.getOutputStream());
//            output.writeObject(bo);
//            ObjectInputStream input=new ObjectInputStream(socket.getInputStream());
//            int result= (int) input.readObject();
//            return result;
//        }catch (Exception e){e.printStackTrace();
//        return 0;
//        }
//
//    }
//
//    public RegisterBo sendLoginDetails(RegisterBo bo){
//        try {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//            Socket socket=new Socket(ipAddress,9867);
//            ObjectOutputStream output=new ObjectOutputStream(socket.getOutputStream());
//            output.writeObject(bo);
//            ObjectInputStream inputStream=new ObjectInputStream(socket.getInputStream());
//            return (RegisterBo) inputStream.readObject();
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public void sendIncidenceDetails(IncidenceBo bo){
//        try {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//            Socket socket=new Socket(ipAddress,9858);
//            ObjectOutputStream output=new ObjectOutputStream(socket.getOutputStream());
//            output.writeObject(bo);
//        }catch (Exception e){e.printStackTrace();}
//
//    }
//
//    public void sendPicDetails(PicDetailsBo bo){
//        try {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//            Socket socket=new Socket(ipAddress,9888);
//            ObjectOutputStream output=new ObjectOutputStream(socket.getOutputStream());
//            output.writeObject(bo);
//        }catch (Exception e){e.printStackTrace();}
//
//    }
//
//    public ArrayList<IncidenceBo> getPandingRequest(int status){//todo:start
//        try {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//            Socket socket=new Socket(ipAddress,9889);
//            ObjectOutputStream output=new ObjectOutputStream(socket.getOutputStream());
//            output.writeObject(status);
//            ObjectInputStream inputStream=new ObjectInputStream(socket.getInputStream());
//            ArrayList<IncidenceBo> list= (ArrayList<IncidenceBo>) inputStream.readObject();
//
//            return list;
//
 //       }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return null;
//    }//todo:end


public double distance(double lat1, double lon1, double lat2, double lon2) {
    double theta = lon1 - lon2;
    double dist = Math.sin(deg2rad(lat1))
            * Math.sin(deg2rad(lat2))
            + Math.cos(deg2rad(lat1))
            * Math.cos(deg2rad(lat2))
            * Math.cos(deg2rad(theta));
    dist = Math.acos(dist);
    dist = rad2deg(dist);
    dist = dist * 60 * 1.1515;
    return (dist);
}

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
