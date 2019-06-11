package com.cronoteSys.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.model.vo.view.SimpleUser;

public class UserDAO extends GenericsDAO<UserVO, Integer> {

	public UserDAO() {
		super(UserVO.class);
	}

	

	public List<UserVO> listAll() {
		try {
			List<UserVO> Pessoas;
			Pessoas = entityManager.createNativeQuery("SELECT * FROM tb_user", UserVO.class).getResultList();

			if (Pessoas.size() > 0) {
				return Pessoas;
			}

		} catch (HibernateException e) {
			System.out.println("Problem on list " + e.getMessage());
		}
		return null;
	}

	public List<SimpleUser> listLoggedUsers(String ids,String id) {
//		Session session = entityManager.unwrap(Session.class);
		String where = ids!=null? ("where u.idUser in(" + ids+") and u.idUser not in("+id+")"):"";
		String query = "select new com.cronoteSys.model.vo.view.SimpleUser"
				+ "(u.idUser, u.completeName, l.email) " + " from LoginVO l"
				+ " left join UserVO u on u.idUser=l.tbUser " + where;
		System.out.println(query);
		List<SimpleUser> users;
		users = entityManager.createQuery(query, SimpleUser.class).getResultList();

		return users;
	}
}
