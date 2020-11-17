package com.oss.model;

public class BorrowerRequest {
	
	/**
	 * 读者ID
	 */
	private String borrowerId;
	/**
	 * 性别
	 */
	private String sex;
	/**
	 * 出生年月
	 */
	private String birth;
	/**
	 * 读者类型，成人or少儿
	 */
	private String rtype;
	/**
	 * 星座
	 */
	private String horoscope;
	public String getBorrowerId() {
		return borrowerId;
	}
	public void setBorrowerId(String borrowerId) {
		this.borrowerId = borrowerId;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getRtype() {
		return rtype;
	}
	public void setRtype(String rtype) {
		this.rtype = rtype;
	}
	public String getHoroscope() {
		return horoscope;
	}
	public void setHoroscope(String horoscope) {
		this.horoscope = horoscope;
	}



}
