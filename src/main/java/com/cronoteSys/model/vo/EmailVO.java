package com.cronoteSys.model.vo;

import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType
public class EmailVO {
	
	private String[] receiver;
	private String message;
	private String subject;
	
	public EmailVO(String[] receiver, String message, String subject) {
		this.receiver = receiver;
		this.message = message;
		this.subject = subject;
	}
	
	public String[] getReceiver() {
		return receiver;
	}
	public void setReceiver(String[] receiver) {
		this.receiver = receiver;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
}
