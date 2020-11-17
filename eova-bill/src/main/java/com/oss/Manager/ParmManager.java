package com.oss.Manager;

import java.util.ArrayList;
import java.util.List;

import com.oss.beans.APIs;
import com.oss.beans.Parms;
import com.oss.beans.Tokens;

public class ParmManager {
    public static List<APIs> apIs = new ArrayList<>();

    public static void addAPI(String apiname, String apiid, String callbackname, Tokens tokens,TestManager t)
    {
        APIs a = new APIs();
        a.setAat(tokens.getAat());
        a.setApiid(apiid);
        a.setApiname(apiname);
        a.setCallbackname(callbackname);
        a.setParms(new Parms(t.getPreparedParms(apiid)));
        a.setUrt(tokens.getUrt());
        a.setUat(tokens.getUat());
        apIs.add(a);
    }
}
