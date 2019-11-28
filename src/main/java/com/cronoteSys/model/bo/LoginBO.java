/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cronoteSys.model.bo;

import java.time.LocalDateTime;
import java.util.List;

import com.cronoteSys.model.dao.AuditLogDAO;
import com.cronoteSys.model.dao.LoginDAO;
import com.cronoteSys.model.interfaces.DatabaseLog;
import com.cronoteSys.model.vo.AuditLogVO;
import com.cronoteSys.model.vo.LoginVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.GenHash;
import com.cronoteSys.util.GsonUtil;
import com.cronoteSys.util.RestUtil;

/**
 *
 * @author bruno
 */
public class LoginBO implements DatabaseLog {
	@Override
	public void saveLog(String operation, Object obj) {

		AuditLogVO audit = new AuditLogVO();
		audit.setTablename("tb_user");
		audit.setDateTime(LocalDateTime.now());
		audit.setAction(operation);
		audit.setUser((UserVO) obj);

		new AuditLogDAO().saveOrUpdate(audit);
	}

	public LoginVO save(LoginVO login) {
		login.setEmail(login.getEmail().toLowerCase());
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.post("saveLogin", login).readEntity(String.class);
			login = (LoginVO) GsonUtil.fromJsonAsStringToObject(json, LoginVO.class);
		} else {
			login = new LoginDAO().saveOrUpdate(login);
		}

		saveLog("Insert", null);
		return login;
	}

	public void update(LoginVO login) {
		new LoginDAO().saveOrUpdate(login);
		saveLog("update", null);
	}

	public void delete(LoginVO login) {
		new LoginDAO().delete(login.getIdLogin());
		saveLog("delete", null);
	}

	public List<LoginVO> listAll() {
		return new LoginDAO().listAll();
	}

	public UserVO login(LoginVO login) {
		UserVO user = null;
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.post("login", login).readEntity(String.class);
			user = (UserVO) GsonUtil.fromJsonAsStringToObject(json, UserVO.class);
		} else {
			user = new LoginDAO().verifiedUser(login.getEmail(), login.getPasswd());
		}
		if (user != null) {
			saveLog("User logged in > id: " + user.getLogin().getIdLogin(), user);
		}
		return (user != null && user.getStats() == 1) ? user : null;
	}

	public Long loginExists(String sEmail) {
		Long result = 0L;
		if (RestUtil.isConnectedToTheServer()) {
			String resp = RestUtil.get("email_exists?email=" + sEmail).readEntity(String.class);
			result = Long.valueOf(resp);
		} else {
			result = new LoginDAO().loginExists(sEmail);
		}

		return result;
	}

	// XXX: not working
	public LoginVO getLogin(UserVO user) {
		return new LoginDAO().loginByUser(user);
	}

	public boolean changePassword(String email, String sPassPureText) {
		String passwordEncrypted = new GenHash().hashIt(sPassPureText);
		boolean result = false;
		if (RestUtil.isConnectedToTheServer()) {
			String infos = email + ";" + passwordEncrypted;
			String json = RestUtil.post("passwordChange", infos).readEntity(String.class);
			Integer p = (Integer) GsonUtil.fromJsonAsStringToObject(json, Integer.class);

			result = p > 0;
		} else {
			result = new LoginDAO().changePassword(email, passwordEncrypted) > 0;
		}
		saveLog("update - password change", null);
		return result;
	}
}
