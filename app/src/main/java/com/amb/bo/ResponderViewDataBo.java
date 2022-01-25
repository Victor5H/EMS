package com.amb.bo;

/**
 * Created by vaibhav on 2/26/2018.
 */

public class ResponderViewDataBo {
    //hospitaldetail.hoslon,hospitaldetail.hoslon,registration.id    incidence.id,currentlocation,criticallevel,username,incstatus,latitude,longitude
    private String hospitalDetailHoslat;
    private String hospitalDetailHoslon;
    private String registrationId;
    private String incidenceId;
    private String currentLocation;
    private String criticalLevel;
    private String username;
    private String incStatus;
    private String latitude;
    private String longitude;

    public String getHospitalDetailHoslat() {
        return hospitalDetailHoslat;
    }

    public void setHospitalDetailHoslat(String hospitalDetailHoslat) {
        this.hospitalDetailHoslat = hospitalDetailHoslat;
    }

    public String getHospitalDetailHoslon() {
        return hospitalDetailHoslon;
    }

    public void setHospitalDetailHoslon(String hospitalDetailHoslon) {
        this.hospitalDetailHoslon = hospitalDetailHoslon;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getIncidenceId() {
        return incidenceId;
    }

    public void setIncidenceId(String incidenceId) {
        this.incidenceId = incidenceId;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getCriticalLevel() {
        return criticalLevel;
    }

    public void setCriticalLevel(String criticalLevel) {
        this.criticalLevel = criticalLevel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIncStatus() {
        return incStatus;
    }

    public void setIncStatus(String incStatus) {
        this.incStatus = incStatus;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
