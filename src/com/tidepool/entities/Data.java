package com.tidepool.entities;

import java.sql.Date;

public class Data {
	private int id;
	private Date time;
	private int bg;
	private int insulin;
	private int userId;
	private int chatId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public int getBg() {
		return bg;
	}
	public void setBg(int bg) {
		this.bg = bg;
	}
	public int getInsulin() {
		return insulin;
	}
	public void setInsulin(int insulin) {
		this.insulin = insulin;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getChatId() {
		return chatId;
	}
	public void setChatId(int chatId) {
		this.chatId = chatId;
	}
	
	
	
	

}
