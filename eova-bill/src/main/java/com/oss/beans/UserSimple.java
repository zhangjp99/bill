package com.oss.beans;

public class UserSimple {

	public String nameCn;
	public String username;
	public String shlibLibId;
	public String shlibCardNo;
	public String shlibCardSid;
	public String shlibCardStatus;
	public String shlibCardFunction;
	public String shlibCardType;

	public String getNameCn() {
		return nameCn;
	}

	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getShlibLibId() {
		return shlibLibId;
	}

	public void setShlibLibId(String shlibLibId) {
		this.shlibLibId = shlibLibId;
	}

	public String getShlibCardNo() {
		return shlibCardNo;
	}

	public void setShlibCardNo(String shlibCardNo) {
		this.shlibCardNo = shlibCardNo;
	}

	public String getShlibCardSid() {
		return shlibCardSid;
	}

	public void setShlibCardSid(String shlibCardSid) {
		this.shlibCardSid = shlibCardSid;
	}

	public String getShlibCardStatus() {
		return shlibCardStatus;
	}

	public void setShlibCardStatus(String shlibCardStatus) {
		this.shlibCardStatus = shlibCardStatus;
	}

	public String getShlibCardFunction() {
		return shlibCardFunction;
	}

	public void setShlibCardFunction(String shlibCardFunction) {
		this.shlibCardFunction = shlibCardFunction;
	}

	public String getShlibCardType() {
		return shlibCardType;
	}

	public void setShlibCardType(String shlibCardType) {
		this.shlibCardType = shlibCardType;
	}

	// public String namePinyin;
	public String vpnType;
	public String identityType;
	public String identityNo="";

	public String getVpnType() {
		return vpnType;
	}

	public void setVpnType(String vpnType) {
		this.vpnType = vpnType;
	}

	public String getIdentityType() {
		return identityType;
	}

	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}

	public String getIdentityNo() {
		if (identityNo.length() == 18) {
			return identityNo.substring(0, 5) + "**" + identityNo.substring(16, 18);
		} else if (identityNo.isEmpty() || identityNo == null) {
			return "";
		} else {
			return identityNo.substring(0, 1)+"**";
		}
	}

	public void setIdentityNo(String identityNo) {
		this.identityNo = identityNo;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getShlibBorrower() {
		return shlibBorrower;
	}

	public void setShlibBorrower(String shlibBorrower) {
		this.shlibBorrower = shlibBorrower;
	}

	public String userType;
	public String shlibBorrower;

}
