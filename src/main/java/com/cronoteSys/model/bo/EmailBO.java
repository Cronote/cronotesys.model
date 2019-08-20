package com.cronoteSys.model.bo;

import java.net.URLEncoder;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

import com.cronoteSys.model.vo.EmailVO;
import com.cronoteSys.util.RestUtil;
import com.google.gson.Gson;

public class EmailBO {
	private final String sUser = "cronotesys@gmail.com";
	private final String sPass = "qfkmpdycmduuzfmr";
	private final int iSmtpPort = 465;
	private final String sHostName = "smtp.googlemail.com";
	public boolean genericEmail(EmailVO emailVO) throws EmailException {
		String emailEncoder = URLEncoder.encode(new Gson().toJson(emailVO));
		System.out.println("send_email?receivers="+emailEncoder);
		String string =  RestUtil.get("send_email?receivers="+emailEncoder).readEntity(String.class);
		System.out.println(string+"HE DO THE PONG DANCE");
		if(string.contains("true")) {
			return true;
		}else {
			return false;
		}
}
	
	
	
	public void sendEmail(Email email) {
		CompletableFuture.runAsync(() ->{
			try {
				email.send();
			} catch (EmailException e) {
				e.printStackTrace();
			}
		});
	}
}
