package com.tidepool.entities;

public class Alert {
	private long id;
	private String content;
	private long data_id;
	private boolean status;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getDataId() {
		return data_id;
	}
	public void setDataId(long dId) {
		this.data_id = dId;
	}
	public void setStatus(boolean s){
		status = s;
	}
	public boolean getStatus() {
		return status;
	}

}
