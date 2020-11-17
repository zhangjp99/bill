package com.oss.model;


import java.util.List;

/**
 * Created by tangdonghai on 07/09/2017.
 */

public class Qx {

    private String oqx;
    private String code;
    private String name;
    private String ename;
    private List<Tss> tss;


    public String getOqx() {
        return oqx;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getEname() {
        return ename;
    }

    public List<Tss> getTss() {
        return tss;
    }

    public void setOqx(String oqx) {
        this.oqx = oqx;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public void setTss(List<Tss> tss) {
        this.tss = tss;
    }
}
