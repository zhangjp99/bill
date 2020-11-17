package com.oss.beans;

import com.oss.Manager.DevManager;

public class Devs {
    private String appkey;
    private String appsecret;
    private String deviceid;
    private boolean isRandomDeviceid;

    public boolean isRandomDeviceid() {
        return isRandomDeviceid;
    }

    public void setRandomDeviceid(boolean randomDeviceid) {
        isRandomDeviceid = randomDeviceid;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    public String getDeviceid() {
        if(isRandomDeviceid)
        {
           return  DevManager.getRandomDeviceid();
        }
        else {
            return deviceid;
        }
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public boolean isIsdebug() {
        return isdebug;
    }

    public void setIsdebug(boolean isdebug) {
        this.isdebug = isdebug;
    }

    public boolean isIsoutput() {
        return isoutput;
    }

    public void setIsoutput(boolean isoutput) {
        this.isoutput = isoutput;
    }

    private boolean isdebug;
    private boolean isoutput;
}
