/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amb.bo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author vaibhav
 */
public class PicDetailsBo  {
    //id,pic,inc_id,userid
    private String id;
    private ArrayList<byte[]> pic;
    private String inc_id;
    private String userid;

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
     * @return the inc_id
     */
    public String getInc_id() {
        return inc_id;
    }

    /**
     * @param inc_id the inc_id to set
     */
    public void setInc_id(String inc_id) {
        this.inc_id = inc_id;
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

    public void setPic(ArrayList<byte[]> pic) {
        this.pic = pic;
    }

    public ArrayList<byte[]> getPic() {
        return pic;
    }
}
