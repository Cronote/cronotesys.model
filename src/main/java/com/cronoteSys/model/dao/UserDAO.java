package com.cronoteSys.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;

import com.cronoteSys.model.vo.TeamUser;
import com.cronoteSys.model.vo.UserVO;

public class UserDAO extends GenericsDAO<UserVO, Integer> {

	public UserDAO() {
		super(UserVO.class);
	}

	public List<UserVO> listAll() {
		List<UserVO> users = new ArrayList<UserVO>();
		try {
			users = entityManager.createNativeQuery("SELECT * FROM tb_user", UserVO.class).getResultList();
			for (UserVO u : users) {
				for (TeamUser t : u.getTeamUser()) {
					t.setMember(null);
					t.getTeam().getTeamUser().clear();
				}
			}

		} catch (HibernateException e) {
			System.out.println("Problem on list " + e.getMessage());
		}
		return users;
	}

	public List<UserVO> listLoggedUsers(String ids, String id) {

		List<UserVO> users = new ArrayList<UserVO>();

		try {
			String where = ids != null ? ("where u.idUser in(" + ids + ") and u.idUser not in(" + id + ")") : "";
			String query = "select u from UserVO u " + where;
			users = entityManager.createQuery(query, UserVO.class).getResultList();

		} catch (HibernateException e) {
			System.out.println("List logged users " + e.getMessage());
		}
		return users;
	}

	public List<UserVO> findByNameOrEmail(String search, String loggedUserId) {
		String query = "from UserVO u where (lower(u.completeName) like :search1 "
				+ " or lower(u.login.email) like :search2) and u.idUser not in(" + loggedUserId + ")";
		List<UserVO> lst = entityManager.createQuery(query, UserVO.class).setParameter("search1", "%" + search + "%")
				.setParameter("search2", "%" + search + "%").getResultList();
		return lst;
	}
}
