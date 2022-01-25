/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*

package com.amb.bo;

import android.os.StrictMode;

import com.amb.bo.IncidenceBo;
import com.amb.bo.PicDetailsBo;
import com.amb.bo.RegisterBo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;




public class DbOper {

    Connection con = null;

    public DbOper() {
//        Connection con = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("com.mysql.jdbc.Driver").newInstance();
             con= DriverManager.getConnection("jdbc:mysql://cloudbin.net/ambulance","rootambulance","root@4321");
//            con = DriverManager.getConnection("jdbc:mysql://localhost/ambulance", "root", "root");
            System.out.println("Connection has been created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int insertUserDetails(RegisterBo bo) {
        try {//id,username,password,mobile,address,gender,hospitalname,imeino,drivinglicence,usertype
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            PreparedStatement ps = con.prepareStatement("insert into registration (username,password,mobile,address,gender,hospitalname,imeino,drivinglicence,usertype) values (?,?,?,?,?,?,?,?,?)");
            ps.setString(1, bo.getUsername());
            ps.setString(2, bo.getPassword());
            ps.setString(3, bo.getMobile());
            ps.setString(4, bo.getAddress());
            ps.setString(5, bo.getGender());
            ps.setString(6, bo.getHospitalname());
            ps.setString(7, bo.getImeino());
            ps.setString(8, bo.getDrivinglicence());
            ps.setString(9, bo.getUsertype());
            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DbOper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public RegisterBo getUserDetails(RegisterBo bo) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            RegisterBo rb = new RegisterBo();
            PreparedStatement ps = con.prepareStatement("select id,usertype from registration where username='" + bo.getUsername() + "'AND password='" + bo.getPassword() + "'");
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
                Logger.getLogger(DbOper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public int insertIncidenceDetails(IncidenceBo bo) {
        try {//id,currentlocation,criticallevel,userid,incstatus,latitude,longitude
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            PreparedStatement ps = con.prepareStatement("insert into incidence (currentlocation,criticallevel,userid,latitude,longitude) values (?,?,?,?,?)");
            ps.setString(1, bo.getCurrentlocation());
            ps.setString(2, bo.getCriticallevel());
            ps.setString(3, bo.getUserid());
            ps.setString(4, bo.getLatitude());
            ps.setString(5, bo.getLongitude());
//            ps.setString(4, bo.getIncstatus());
            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int insertPicDetails(PicDetailsBo bo) {
        try {//pic,inc_id
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Thread.sleep(100);
            PreparedStatement ps1=con.prepareStatement("Select max(id) from incidence");
            ResultSet rs = ps1.executeQuery();
            int id=0;
            while (rs.next()) {                
                id = rs.getInt(1);
            }
            PreparedStatement ps = con.prepareStatement("insert into detailspic (pic,inc_id,userid) values (?,?,?)");
            for (byte[] col : bo.getPic()) {
                ps.setBytes(1, col);
            ps.setInt(2, id);
            ps.setString(3, bo.getUserid());
             ps.executeUpdate();
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public ArrayList<IncidenceBo> getIncidenceDetails(int status) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            ArrayList<IncidenceBo> list = new ArrayList<>();
            PreparedStatement ps = con.prepareStatement("select incidence.id,currentlocation,criticallevel,username,incstatus,latitude,longitude from incidence   INNER JOIN registration on registration.id=incidence.userid where incstatus='"+status+"'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                IncidenceBo bo = new IncidenceBo();
                bo.setId(rs.getString(1));
                bo.setCurrentlocation(rs.getString(2));
                bo.setCriticallevel(rs.getString(3));
                bo.setUserid(rs.getString(4));
                bo.setIncstatus(rs.getString(5));
                bo.setLatitude(rs.getString(6));
                bo.setLongitude(rs.getString(7));
                list.add(bo);

            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public ArrayList<PicDetailsBo> getPicDetails(IncidenceBo bo){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            ArrayList<PicDetailsBo> list=new ArrayList<>();
            PreparedStatement ps=con.prepareStatement("Select id,pic,inc_id,userid from detailspic where inc_id='"+bo.getId()+"'AND userid='"+bo.getUserid()+"'");
            ResultSet rs = ps.executeQuery();
//            ArrayList<
//            while (rs.next()) {                
//                PicDetailsBo bo1=new PicDetailsBo();
//                bo1.setId(rs.getString(1));
//                bo1.setPic(rs.getBytes(2));
//                bo1.setInc_id(rs.getString(3));
//                bo1.setUserid(rs.getString(4));
//                list.add(bo1);
//                        
//            }
            return list;
        } catch (Exception e) {
        e.printStackTrace();
        return null;
        }
    }

}
*/
