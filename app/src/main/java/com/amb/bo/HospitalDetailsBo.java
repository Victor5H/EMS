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
public class HospitalDetailsBo   {
    //id,hospitalname,hospitaladdress,hoslat,hoslon,location\\
    private String id;
    private String hospitalname;
    private String hospitaladdress;
    private String hoslat;
    private String hoslon;
    private String location;

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
     * @return the hospitalname
     */
    public String getHospitalname() {
        return hospitalname;
    }

    /**
     * @param hospitalname the hospitalname to set
     */
    public void setHospitalname(String hospitalname) {
        this.hospitalname = hospitalname;
    }

    /**
     * @return the hospitaladdress
     */
    public String getHospitaladdress() {
        return hospitaladdress;
    }

    /**
     * @param hospitaladdress the hospitaladdress to set
     */
    public void setHospitaladdress(String hospitaladdress) {
        this.hospitaladdress = hospitaladdress;
    }

    /**
     * @return the hoslat
     */
    public String getHoslat() {
        return hoslat;
    }

    /**
     * @param hoslat the hoslat to set
     */
    public void setHoslat(String hoslat) {
        this.hoslat = hoslat;
    }

    /**
     * @return the hoslon
     */
    public String getHoslon() {
        return hoslon;
    }

    /**
     * @param hoslon the hoslon to set
     */
    public void setHoslon(String hoslon) {
        this.hoslon = hoslon;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

}
