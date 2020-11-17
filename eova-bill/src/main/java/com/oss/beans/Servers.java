package com.oss.beans;

import com.oss.enums.Protocols;


public class Servers {
    private String ip;
    private String dns;
    private int port;
    private Protocols protocols;
    private String baseUrlAPI;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Protocols getProtocols() {
        return protocols;
    }

    public void setProtocols(Protocols protocols) {
        this.protocols = protocols;
    }

    public String getBaseUrlAPI() {
        return baseUrlAPI;
    }

    public void setBaseUrlAPI(String baseUrlAPI) {
        this.baseUrlAPI = baseUrlAPI;
    }

    public String getBaseUrlToken() {
        return baseUrlToken;
    }

    public void setBaseUrlToken(String baseUrlToken) {
        this.baseUrlToken = baseUrlToken;
    }

    private String baseUrlToken;

}
