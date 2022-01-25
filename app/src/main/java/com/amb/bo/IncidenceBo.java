/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amb.bo;

import java.io.Serializable;

/**
 *
 * @author vaibhav
 */
public class IncidenceBo {
//    id,currentlocation,criticallevel,userid,incstatus,latitude,longitude

    private String id;
    private String currentlocation;
    private String criticallevel;
    private String userid;
    private String incstatus;
    private String latitude;
    private String longitude;


    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the currentlocation
     */
    public String getCurrentlocation() {
        return currentlocation;
    }

    /**
     * @param currentlocation the currentlocation to set
     */
    public void setCurrentlocation(String currentlocation) {
        this.currentlocation = currentlocation;
    }

    /**
     * @return the criticallevel
     */
    public String getCriticallevel() {
        return criticallevel;
    }

    /**
     * @param criticallevel the criticallevel to set
     */
    public void setCriticallevel(String criticallevel) {
        this.criticallevel = criticallevel;
    }

    /**
     * @return the userid
     */
    public String getUserid() {
        return userid;
    }

    /**
     * @param userid the userid to set
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    /**
     * @return the incstatus
     */
    public String getIncstatus() {
        return incstatus;
    }

    /**
     * @param incstatus the incstatus to set
     */
    public void setIncstatus(String incstatus) {
        this.incstatus = incstatus;
    }

    /**
     * @return the latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}
