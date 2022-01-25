package com.amb.bo;

/**
 * Created by vaibhav on 2/26/2018.
 */

public class ViewIncidenceResponder {
    private String id;
    private String userName;
    private String distance;
    private String criticalLevel;
    private String approxTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCriticalLevel() {
        return criticalLevel;
    }

    public void setCriticalLevel(String criticalLevel) {
        this.criticalLevel = criticalLevel;
    }

    public String getApproxTime() {
        return approxTime;
    }

    public void setApproxTime(String approxTime) {
        this.approxTime = approxTime;
    }
}
