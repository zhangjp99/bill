package com.oss.beans;

public class APIRequests {
    private long time;
    private String apiname;
    private String requestparms;
    private String requesturls;
    private String response;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getApiname() {
        return apiname;
    }

    public void setApiname(String apiname) {
        this.apiname = apiname;
    }

    public String getRequestparms() {
        return requestparms;
    }

    public void setRequestparms(String requestparms) {
        this.requestparms = requestparms;
    }

    public String getRequesturls() {
        return requesturls;
    }

    public void setRequesturls(String requesturls) {
        this.requesturls = requesturls;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
