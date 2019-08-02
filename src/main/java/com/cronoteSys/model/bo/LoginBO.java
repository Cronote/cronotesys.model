/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cronoteSys.model.bo;

import java.util.List;

import javax.ws.rs.core.Response;

import com.cronoteSys.model.dao.LoginDAO;
import com.cronoteSys.model.vo.ExecutionTimeVO;
import com.cronoteSys.model.vo.LoginVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.GenHash;
import com.cronoteSys.util.GsonUtil;
import com.cronoteSys.util.RestUtil;

/**
 *
 * @author bruno
 */
public class LoginBO {

	public LoginVO save(LoginVO login) {
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.post("saveLogin", login).readEntity(String.class);
			return (LoginVO) GsonUtil.fromJsonAsStringToObject(json, LoginVO.class);
		} else {
			return new LoginDAO().saveOrUpdate(login);
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
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.post("login", login).readEntity(String.class);
			user = (UserVO) GsonUtil.fromJsonAsStringToObject(json, UserVO.class);
			System.out.println(json);
		} else {
			user = new LoginDAO().verifiedUser(login.getEmail(), login.getPasswd());
		}

		return (user != null && user.getStats() == 1) ? user : null;
	}

	public Long loginExists(String sEmail) {
		if (RestUtil.isConnectedToTheServer()) {
			String resp = RestUtil.get("email_exists?email=" + sEmail).readEntity(String.class);
			System.out.println(resp);
			return Long.valueOf(resp);
		}
		return new LoginDAO().loginExists(sEmail);
	}

	//XXX: not working
	public LoginVO getLogin(UserVO user) {
		return new LoginDAO().loginByUser(user);
	}

	public boolean changePassword(String email, String sPassPureText) {
		String passwordEncrypted = new GenHash().hashIt(sPassPureText);
		if (RestUtil.isConnectedToTheServer()) {
			String infos = email + ";" + passwordEncrypted;
			String json = RestUtil.post("passwordChange", infos).readEntity(String.class);
			Integer p = (Integer) GsonUtil.fromJsonAsStringToObject(json, Integer.class);

			return p > 0;
		} else {
			return new LoginDAO().changePassword(email, passwordEncrypted) > 0;
		}
	}
}
