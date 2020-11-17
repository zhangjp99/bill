package com.oss.beans;

import com.alibaba.fastjson.annotation.JSONField;



/**令牌生成后结果输出类，fastjson 排序输出
 * @author xtwl
 *
 */
public class OutputToken {

	public OutputToken() {
	}
	@JSONField(ordinal=1)
	public String issuetime;
	
	@JSONField(ordinal=2)
	public String aat;
	
	@JSONField(ordinal=3)
	public String expiretime_aat;
	
	@JSONField(ordinal=4)
	public String uat;
	
	@JSONField(ordinal=5)
	public String expiretime_uat;
	
	@JSONField(ordinal=6)
	public String urt;
	
	@JSONField(ordinal=7)
	public String expiretime_urt;
	
	@JSONField(ordinal=8)
	public UserSimple us;
	
	public UserSimple getUs() {
		return us;
	}
	public void setUs(UserSimple us) {
		this.us = us;
	}
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
	public String getIssuetime() {
		return issuetime;
	}
	public void setIssuetime(String issuetime) {
		this.issuetime = issuetime;
	}
	public String getExpiretime_aat() {
		return expiretime_aat;
	}
	public void setExpiretime_aat(String expiretime_aat) {
		this.expiretime_aat = expiretime_aat;
	}
	public String getExpiretime_uat() {
		return expiretime_uat;
	}
	public void setExpiretime_uat(String expiretime_uat) {
		this.expiretime_uat = expiretime_uat;
	}
	public String getExpiretime_urt() {
		return expiretime_urt;
	}
	public void setExpiretime_urt(String expiretime_urt) {
		this.expiretime_urt = expiretime_urt;
	}
	
}
