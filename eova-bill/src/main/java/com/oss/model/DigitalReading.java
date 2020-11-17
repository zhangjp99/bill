package com.oss.model;

public class DigitalReading {
	
	/**
	 * 读者ID
	 */
	private String  borrowerId;
	
	/**
	 * 数字阅读图书量
	 */
	private Integer bookCount;
	
	/**
	 * 数字阅读期刊量
	 */
	private Integer magCount;
	/**
	 * 数字阅读报纸量
	 */
	private Integer newspaperCount;
	/**
	 * 数字阅读排名百分比
	 */
	private Float overPercent;
	/**
	 * 数字阅读次数最多的书名
	 */
	private String mostLoveReadBook;
	/**
	 * 阅读时间段：白天/晚上/午休
	 */
	private String readerTime;
	/**
	 * 减少碳排放数量
	 */
	private String saveCarbon;
	/**
	 * 数字阅读读者标签
	 */
	private String eTag;
	public String getBorrowerId() {
		return borrowerId;
	}
	public void setBorrowerId(String borrowerId) {
		this.borrowerId = borrowerId;
	}
	public Integer getBookCount() {
		return bookCount;
	}
	public void setBookCount(Integer bookCount) {
		this.bookCount = bookCount;
	}
	public Integer getMagCount() {
		return magCount;
	}
	public void setMagCount(Integer magCount) {
		this.magCount = magCount;
	}
	public Integer getNewspaperCount() {
		return newspaperCount;
	}
	public void setNewspaperCount(Integer newspaperCount) {
		this.newspaperCount = newspaperCount;
	}
	public Float getOverPercent() {
		return overPercent;
	}
	public void setOverPercent(Float overPercent) {
		this.overPercent = overPercent;
	}
	public String getMostLoveReadBook() {
		return mostLoveReadBook;
	}
	public void setMostLoveReadBook(String mostLoveReadBook) {
		this.mostLoveReadBook = mostLoveReadBook;
	}
	public String getReaderTime() {
		return readerTime;
	}
	public void setReaderTime(String readerTime) {
		this.readerTime = readerTime;
	}
	public String getSaveCarbon() {
		return saveCarbon;
	}
	public void setSaveCarbon(String saveCarbon) {
		this.saveCarbon = saveCarbon;
	}
	public String geteTag() {
		return eTag;
	}
	public void seteTag(String eTag) {
		this.eTag = eTag;
	}

}
