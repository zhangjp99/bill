package com.oss.model;


import java.io.Serializable;

/**
 * Created by tangdonghai on 07/09/2017.
 */

public class Tss implements Serializable{

    private String code;
    private String oqx;
    private String level;
    private String name;
    private String ename;

    public String getCode() {
        return code;
    }

    public String getOqx() {
        return oqx;
    }

    public String getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public String getEname() {
        return ename;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setOqx(String oqx) {
        this.oqx = oqx;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }
}
