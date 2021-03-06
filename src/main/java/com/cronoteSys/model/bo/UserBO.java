/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cronoteSys.model.bo;

import java.util.Date;
import java.util.List;

import com.cronoteSys.model.dao.UserDAO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.RestUtil;

/**
 *
 * @author bruno
 */
public class UserBO {
	private UserDAO userDao;

	public UserBO() {
		userDao = new UserDAO();
	}

	public UserVO save(UserVO user) {
		if (user.getRegisterDate() == null) user.setRegisterDate(new Date());
		if(RestUtil.isConnectedToTheServer()) {
			user = (UserVO) RestUtil.post("saveUser", UserVO.class, user);
		}
		user = userDao.saveOrUpdate(user);
		return user;
	}

//	public void update(UserVO user) {
//		userDao.saveOrUpdate(user);
//	}

//    public boolean activateOrInactivate(UserVO user) {
//        if (user.getStats() == 1) { //ativado
//            user.setStats(Byte.valueOf("0"));
//        } else { // desativado
//            user.setStats(Byte.valueOf("1"));
//        }
//        return new UserDAO().update(user);
//    }

	public void delete(UserVO user) {
		userDao.delete(user.getIdUser());

	}

	public List<UserVO> listAll() {
		return userDao.listAll();
	}
}
