/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cronoteSys.model.bo;

import java.util.List;

import javax.ws.rs.core.Response;

import com.cronoteSys.model.dao.LoginDAO;
import com.cronoteSys.model.vo.LoginVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.RestUtil;

/**
 *
 * @author bruno
 */
public class LoginBO {

	public LoginVO save(LoginVO login) {
		if(RestUtil.isConnectedToTheServer()) {
			return (LoginVO) RestUtil.post("saveLogin", LoginVO.class, login);
		}else {
			return null;
//			return new LoginDAO().saveOrUpdate(login);
		}
	}

	public void update(LoginVO login) {
		new LoginDAO().saveOrUpdate(login);
	}

	public void delete(LoginVO login) {
		new LoginDAO().delete(login.getIdLogin());
	}

	public List<LoginVO> listAll() {
		return new LoginDAO().listAll();
	}

	public UserVO login(LoginVO login) {
		UserVO user = null;
		if(RestUtil.isConnectedToTheServer()) {
			user = (UserVO) RestUtil.post("login", UserVO.class, login);
			System.out.println("aro");
		}else {
			user = new LoginDAO().verifiedUser(login.getEmail(), login.getPasswd());
		}
		return (user != null && user.getStats() == 1) ? user : null;
	}

	public Long loginExists(String sEmail) {
		if(RestUtil.isConnectedToTheServer()) {
			String resp = RestUtil.get("email_exists?email="+sEmail).readEntity(String.class);
			System.out.println(resp);
			return Long.valueOf(resp);
		}
		return new LoginDAO().loginExists(sEmail);
	}

	public boolean validatePassword(String passwd) {
		boolean bHaveNumber = false;
		boolean bHaveSpecialChar = false;
		for (char c : passwd.toCharArray()) {
			if ("0123456789".contains(c + "")) {
				bHaveNumber = true;
				break;
			}
		}
		if (!passwd.matches(".*[a-z]+.*"))
			return false; // n tem letra
		if (!passwd.matches(".*[A-Z]+.*"))
			return false; // n tem LETRA
		for (char c : passwd.toCharArray()) {
			if ("#@/\\\\%$&*!?<>.-_)(".contains(c + "")) {
				bHaveSpecialChar = true;
			}
		}
		if (!(bHaveNumber && bHaveSpecialChar))
			return false;
		return true;
	}
	
	public LoginVO getLogin(UserVO user) {
		return new LoginDAO().loginByUser(user);
	}
}
