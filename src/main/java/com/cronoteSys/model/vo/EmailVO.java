package com.cronoteSys.model.vo;

import java.io.IOException;
import java.io.Serializable;

import javax.ws.rs.WebApplicationException;

import com.fasterxml.jackson.databind.ObjectMapper;


public class EmailVO implements Serializable {
	
	private static final long serialVersionUID = -874021342021543462L;
	private String[] receiver;
	private String message;
	private String subject;
	
	public static EmailVO valueOf(String jsonRepresentation) {
		ObjectMapper mapper = new ObjectMapper(); // Jackson's JSON marshaller
		EmailVO o = null;
		try {
			o = mapper.readValue(jsonRepresentation, EmailVO.class);
		} catch (IOException e) {
			throw new WebApplicationException();
		}
		return o;
	}
	
	public EmailVO(String[] receiver, String message, String subject) {
		this.receiver = receiver;
		this.message = message;
		this.subject = subject;
	}
	
	public EmailVO() {
		// TODO Auto-generated constructor stub
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
