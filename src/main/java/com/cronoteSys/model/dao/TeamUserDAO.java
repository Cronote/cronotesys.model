package com.cronoteSys.model.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityTransaction;

import org.hibernate.HibernateException;

import com.cronoteSys.model.vo.TeamUser;

public class TeamUserDAO extends GenericsDAO<TeamUser, Serializable> {
	public TeamUserDAO() {
		super(TeamUser.class);
	}
	
	public boolean inviteAccepted(int member, int team) {
		try {
			EntityTransaction t = entityManager.getTransaction();
			t.begin();
			entityManager.createNativeQuery("UPDATE teamuser SET inviteaccepted=true WHERE member="+member+" AND team="+team+";").executeUpdate();
			t.commit();
			return true; 
		}catch (HibernateException e) {
			e.printStackTrace();
		}
		return false;
	}
}
