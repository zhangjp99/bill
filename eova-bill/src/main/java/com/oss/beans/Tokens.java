package com.oss.beans;

public class Tokens {
    private String aat;
    private String uat;
    private String urt;
    private String shlibBorrower;
    private int aatusedcount=0;

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

    public int getAatusedcount() {
        return aatusedcount;
    }

    public void setAatusedcount(int aatusedcount) {
        this.aatusedcount = aatusedcount;
    }

    public int getUatusedcount() {
        return uatusedcount;
    }

    public void setUatusedcount(int uatusedcount) {
        this.uatusedcount = uatusedcount;
    }

    public String getShlibBorrower() {
		return shlibBorrower;
	}

	public void setShlibBorrower(String shlibBorrower) {
		this.shlibBorrower = shlibBorrower;
	}

	private int uatusedcount=0;
}
