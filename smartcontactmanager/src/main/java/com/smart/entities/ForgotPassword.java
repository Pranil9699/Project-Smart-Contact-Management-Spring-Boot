package com.smart.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class ForgotPassword {

	@Id
	private String email;
	private int otp;
	public ForgotPassword(String email, int otp) {
		super();
		this.email = email;
		this.otp = otp;
	}
	public ForgotPassword() {
		super();
		
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getOtp() {
		return otp;
	}
	public void setOtp(int otp) {
		this.otp = otp;
	}
	@Override
	public String toString() {
		return "ForgotPassword [email=" + email + ", otp=" + otp + "]";
	}
	
	
}
