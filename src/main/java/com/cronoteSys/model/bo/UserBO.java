/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cronoteSys.model.bo;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cronoteSys.model.dao.AuditLogDAO;
import com.cronoteSys.model.dao.UserDAO;
import com.cronoteSys.model.interfaces.DatabaseLog;
import com.cronoteSys.model.vo.AuditLogVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.GsonUtil;
import com.cronoteSys.util.RestUtil;
import com.google.gson.reflect.TypeToken;

/**
 *
 * @author bruno
 */
public class UserBO implements DatabaseLog {
	private UserDAO userDao;

	@Override
	public void saveLog(String operation, Object obj) {
		
		AuditLogVO audit = new AuditLogVO();
		audit.setTablename("tb_user");
		audit.setDateTime(LocalDateTime.now());
		audit.setAction(operation);
		audit.setUser((UserVO) obj);

		new AuditLogDAO().saveOrUpdate(audit);
	}

	public UserBO() {
		userDao = new UserDAO();
	}

	public UserVO save(UserVO user) {
		String operation = "";
		if (user.getRegisterDate() == null)
			user.setRegisterDate(LocalDateTime.now());

		if (user.getIdUser() == null) {
			operation = "Insert";
		} else {
			operation = "Update user: " + user.getIdUser();

		}
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.post("saveUser", user).readEntity(String.class);
			user = (UserVO) GsonUtil.fromJsonAsStringToObject(json, UserVO.class);
		} else {
			user = userDao.saveOrUpdate(user);
		}
		saveLog(operation, user);
		return user;
	}

	public void delete(UserVO user) {
		userDao.delete(user.getIdUser());
		saveLog("Delete user: " + user.getIdUser(), user);

	}

	public List<UserVO> listAll() {
		return userDao.listAll();
	}

	public List<UserVO> findByNameOrEmail(String search, String loggedUserId) {
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.get("listByNameOrEmail?search=" + search.toLowerCase() + "&not=" + loggedUserId)
					.readEntity(String.class);
			Type simpleUserListType = new TypeToken<List<UserVO>>() {
			}.getType();
			List<UserVO> lst = GsonUtil.getGsonWithJavaTime().fromJson(json, simpleUserListType);
			return lst;
		}
		return userDao.findByNameOrEmail(search.toLowerCase(), loggedUserId);
	}

	public List<UserVO> listLoggedUsers(String idsIn, String idsOut) {
		List<UserVO> users = new ArrayList<UserVO>();
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.get("listLoggedUsers?idsIn=" + idsIn + "&not=" + idsOut).readEntity(String.class);
			Type simpleUserListType = new TypeToken<List<UserVO>>() {
			}.getType();
			users = GsonUtil.getGsonWithJavaTime().fromJson(json, simpleUserListType);
			return users;
		}
		return userDao.listLoggedUsers(idsIn, idsOut);
	}

}
