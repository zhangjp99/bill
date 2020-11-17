package com.oss.Manager;

import java.util.Random;

import com.oss.beans.Devs;

public class DevManager {
    private static Random random=new Random(System.currentTimeMillis());

    public static String getRandomDeviceid()
    {
        return "RandomId_"+random.nextInt(1000);
    }

    public Devs getDev(String appkey,String appsecret,String deviceid,boolean isDebug,boolean isOutput,boolean isDeviceid)
    {
        Devs devs=new Devs();
        devs.setAppkey(appkey);
        devs.setAppsecret(appsecret);
        devs.setDeviceid(deviceid);
        devs.setIsdebug(isDebug);
        devs.setIsoutput(isOutput);
        devs.setRandomDeviceid(isDeviceid);
        return devs;
    }
}

