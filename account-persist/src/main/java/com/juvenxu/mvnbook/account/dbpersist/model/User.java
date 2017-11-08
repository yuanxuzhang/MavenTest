package com.juvenxu.mvnbook.account.dbpersist.model;

public class User {
	
	private String fid;
	private String fname;
	private String femail;
	private String fpassword;
	private boolean factivivated;
	
	public String getFid() {
		return fid;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getFemail() {
		return femail;
	}
	public void setFemail(String femail) {
		this.femail = femail;
	}
	public String getFpassword() {
		return fpassword;
	}
	public void setFpassword(String fpassword) {
		this.fpassword = fpassword;
	}
	public boolean isFactivivated() {
		return factivivated;
	}
	public void setFactivivated(boolean factivivated) {
		this.factivivated = factivivated;
	}
	
	
}
