package com.pojo;

public class User {
	String user_first_name;
	String user_last_name;
	String user_contact_number;
	String user_email_id;
	Address userAddress;

	public User(String user_first_name, String user_last_name, String user_contact_number, String user_email_id,
			Address userAddress) {
		super();
		this.user_first_name = user_first_name;
		this.user_last_name = user_last_name;
		this.user_contact_number = user_contact_number;
		this.user_email_id = user_email_id;
		this.userAddress = userAddress;
	}

	/*
	 * public int getUser_id() { return user_id; } public void setUser_id(int
	 * user_id) { this.user_id = user_id; }
	 */
	public String getUser_first_name() {
		return user_first_name;
	}

	public void setUser_first_name(String user_first_name) {
		this.user_first_name = user_first_name;
	}

	public String getUser_last_name() {
		return user_last_name;
	}

	public void setUser_last_name(String user_last_name) {
		this.user_last_name = user_last_name;
	}

	public String getUser_contact_number() {
		return user_contact_number;
	}

	public void setUser_contact_number(String user_contact_number) {
		this.user_contact_number = user_contact_number;
	}

	public String getUser_email_id() {
		return user_email_id;
	}

	public void setUser_email_id(String user_email_id) {
		this.user_email_id = user_email_id;
	}

	public Address getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(Address userAddress) {
		this.userAddress = userAddress;
	}

	@Override
	public String toString() {
		return "User [user_first_name=" + user_first_name + ", user_last_name=" + user_last_name
				+ ", user_contact_number=" + user_contact_number + ", user_email_id=" + user_email_id + ", userAddress="
				+ userAddress + "]";
	}
	
	

}
