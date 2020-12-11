package com.oss.Manager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.github.kevinsawicki.http.HttpRequest;
import com.oss.beans.APIRequests;
import com.oss.beans.APIs;
import com.oss.beans.Devs;
import com.oss.beans.OutputToken;
import com.oss.beans.Servers;
import com.oss.beans.Tokens;
import com.oss.beans.reqparm;
import com.oss.utils.APIConst;
import com.oss.utils.DateUtility;
import com.oss.utils.DateUtils;
import com.oss.utils.SHA;



/*主要功能：
* 1. 配置测试账号和测试参数，用于后续的接口测试环节。
* 2. 根据配置的账号获取相关的令牌。*/
public class TestManager {
    private String cardno;
    private String passwd;
    private String ibarcode;
    private String qrcode;

    public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public String getIbarcode() {
        return ibarcode;
    }

    public void setIbarcode(String ibarcode) {
        this.ibarcode = ibarcode;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    private String sid;

    public Tokens getTokens(Devs devs, Servers servers)
    {
        Tokens tokens=new Tokens();
        tokens.setAat(JSON.parseObject(doGet(Token_TokenAcquire_aat_01(devs,servers)), OutputToken.class).getAat());
        OutputToken out =  JSON.parseObject(doGet(Token_TokenAcquire_uat_01(tokens.getAat(), devs,servers)), OutputToken.class);
        tokens.setUat(out.getUat());
        if(out.getUs()!=null) {
        	tokens.setShlibBorrower(out.getUs().getShlibBorrower());
        }
        tokens.setAatusedcount(0);
        tokens.setUatusedcount(0);
        tokens.setUrt("");
        return tokens;
    }

    private String doGet(reqparm req) {
//		System.out.println("【令牌请求样例】：" + req.getUrl() + "?" + req.getParms());
        HttpRequest result = HttpRequest.get(req.getUrl()+req.getParms());
        // Accept all certificates
        result.trustAllCerts();
        // Accept all hostnames
        result.trustAllHosts();
        int code = result.code();
        String res = result.body();
        if (code == 200) {
			System.out.println(res);
        } else {
			System.out.println("errcode:" + code);
        }
        return res;
    }

    private String doSecret(String appid, String appsecret) {
        return SHA.sha256(appid + DateUtility.getDateHours2() + appsecret);
    }

    private reqparm Token_TokenAcquire_aat_01(Devs devs,Servers servers) {
        reqparm m = new reqparm();
        m.setUrl(servers.getBaseUrlToken());
        m.setParms("method=token_acquire&type=aat&appid=" + devs.getAppkey() + "&secret=" +doSecret(devs.getAppkey(),devs.getAppsecret()) + "&deviceid=" + devs.getDeviceid());
        m.setCallbackname("");
        return m;
    }

    private reqparm Token_TokenAcquire_uat_01(String uat,Devs devs,Servers servers) {
        reqparm m = new reqparm();
        m.setUrl(servers.getBaseUrlToken());
        m.setParms("method=token_acquire&type=uat&aat=" + uat + "&username=" + this.cardno + "&password=" + this.passwd
                + "&tokentype=uat_7");
        m.setCallbackname("");
        return m;
    }
    public void doAPIS_Loop(Servers servers,int loops)
    {
    	for(int i=0;i<loops;i++)
    	{
    		System.out.println("loop:"+i);
    		List<APIs> a =ParmManager.apIs;
            for(APIs api:a)
            {
                String parms=getMapKeys(api.getParms().mp);
               String urls= doAPI(api.getApiid(),api.getAat(),api.getUat(),api.getParms().mp,servers);
               long start=System.currentTimeMillis();
               String result=this.doUrl(urls);
               long end=System.currentTimeMillis();
               long delta=end-start;
                APIRequests apiRequests=new APIRequests();
                apiRequests.setResponse(result);
                apiRequests.setRequesturls(urls);
                apiRequests.setRequestparms(parms);
                apiRequests.setApiname(api.getApiname());
                apiRequests.setTime(delta);
                DebugManager.addResponse(apiRequests);
            }
    	}
    }

    public void doAPIS(Servers servers)
    {
    	doAPIS_Loop(servers,1);
    }

    private String doAPI(String apiid, String aat, String uat, HashMap<String, String> mp,Servers servers) {
        StringBuilder sb = new StringBuilder();
        sb.append(servers.getBaseUrlAPI()).append("&apiid=").append(apiid);
        if (!aat.isEmpty()) {
            sb.append("&aat=").append(aat);
        }
        if (!uat.isEmpty()) {
            sb.append("&uat=").append(uat);
        }
        if (!mp.isEmpty()) {
            sb.append("&parm=").append(this.getMapKeys(mp));
        }
        return sb.toString();
    }

    private String getMapKeys(HashMap<String, String> mp) {
        int cnt = 0;
        StringBuilder sb = new StringBuilder();
        Iterator iter = mp.entrySet().iterator();
        while (iter.hasNext()) {
            cnt++;
            if (cnt > 1) {
                sb.append("&");
            }
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            sb.append(key).append("=").append(val);
        }
        try {
            return URLEncoder.encode(sb.toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String doUrl(String url) {
        HttpRequest result = HttpRequest.get(url);
        result.trustAllCerts();
        result.trustAllHosts();
        String body = result.body();
        return body;
    }
    public HashMap<String, String> getPreparedParms(String apiid) {
        HashMap<String, String> mp = new HashMap<>();
        switch (apiid) {
            case APIConst.API_BOOKINFO_RFID:
                mp.put("sid", sid);
                break;
            case APIConst.API_BOOKRENEW2_RFID:
                mp.put("sid", sid);
                break;
            case APIConst.API_BOOKRENEW2:
                mp.put("sid", sid);
                break;
            case APIConst.API_IC_GETCARDSTATUS:
                mp.put("sid", sid);
                break;
            case APIConst.API_IC_GETREADERINFO:
                mp.put("sid", sid);
                break;
            case APIConst.API_IC_GETREADERNAME_STATUS:
                mp.put("sid", sid);
                break;
            case APIConst.API_BookCollection_RFID:
                mp.put("ibarcode", ibarcode);
                break;
            case APIConst.API_BOOKINFO_CNT_RFID:
                mp.put("sid", sid);
                break;
            case APIConst.API_QR_DECODE:
            	mp.put("q", qrcode);
            default:
                break;
        }
        return mp;
    }
}
