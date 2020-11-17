package com.oss.beans;

public class APIs {
    private String aat;
    private String uat;
    private String urt;
    private Parms parms;

    public String getAat() {
        return aat;
    }

    public void setAat(String aat) {
        this.aat = aat;
    }

    public String getUat() {
        return uat;
    }

    public void setUat(String uat) {
        this.uat = uat;
    }

    public String getUrt() {
        return urt;
    }

    public void setUrt(String urt) {
        this.urt = urt;
    }

    public Parms getParms() {
        return parms;
    }

    public void setParms(Parms parms) {
        this.parms = parms;
    }

    public String getCallbackname() {
        return callbackname;
    }

    public void setCallbackname(String callbackname) {
        this.callbackname = callbackname;
    }

    public String getApiid() {
        return apiid;
    }

    public void setApiid(String apiid) {
        this.apiid = apiid;
    }

    public String getApiname() {
        return apiname;
    }

    public void setApiname(String apiname) {
        this.apiname = apiname;
    }

    private String callbackname;
    private String apiid;
    private String apiname;
}
