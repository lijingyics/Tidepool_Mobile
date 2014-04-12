package com.tidepool.entities;

import java.sql.Date;


public class User {
	public enum Gender {
		FEMALE("F"), MALE("M");
	 
		private String gender;
	 
		private Gender(String g) {
			gender = g;
		}
	 
		public String getGender() {
			return gender;
		}
	}
	
	public enum Role {
		PATIENT("PAT"), PARENT("PAR");
	 
		private String role;
	 
		private Role(String r) {
			role = r;
		}
	 
		public String getRole() {
			return role;
		}
	}
	
	private int id;
	private String email;
	private String username;
	private String password;
	private String phoneNo;
	private Date dateOfBirth;
	private String gender;
	private String role;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	

}
