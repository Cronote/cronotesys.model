package com.cronoteSys.model.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;

import com.cronoteSys.model.bo.LoginBO;
import com.cronoteSys.model.vo.TeamVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.model.vo.view.SimpleUser;

public class TeamDAO extends GenericsDAO<TeamVO, Long> {

	public TeamDAO() {
		super(TeamVO.class);
	}

	public TeamVO saveOrUpdate(TeamVO team) {
		team = super.saveOrUpdate(team);
		fillMembers(team);
		return team;

	}

	public List<TeamVO> listAll() {
		try {
			List<TeamVO> teams;
			teams = entityManager.createQuery("Select t from TeamVO t", TeamVO.class).getResultList();

			if (teams.size() > 0) {
				return teams;
			}

		} catch (HibernateException e) {
			System.out.println("Problem on list " + e.getMessage());
		}
		return null;
	}

	public List<TeamVO> listByUserOwnerOrMember(int userId) {
		List<TeamVO> teams = new ArrayList<TeamVO>();
		try {

			teams = entityManager
					.createNativeQuery("Select distinct t.id,t.name,t.description,t.id_user,t.teamColor"
							+ " from tb_team t" + " left join tbrel_team_user tbr on t.id = tbr.id_team "
							+ " where t.id_user =" + userId + " or tbr.id_member in(" + userId + ")", TeamVO.class)
					.getResultList();
			for (TeamVO t : teams) {
				fillMembers(t);
			}

		} catch (HibernateException e) {
			System.out.println("Problem on list " + e.getMessage());
		}
		return teams;
	}

	private void fillMembers(TeamVO t) {
//		List<SimpleUser> sus = new ArrayList<SimpleUser>();
//		for (UserVO user : t.getMembers()) {
//			SimpleUser su = new SimpleUser();
//			su.setId(user.getIdUser());
//			su.setCompleteName(user.getCompleteName());
//			su.setEmail(new LoginDAO().getEmailFromUser(user.getIdUser()));
//			sus.add(su);
//		}
//		SimpleUser su = new SimpleUser();
//		su.setId(t.getOwner().getIdUser());
//		su.setCompleteName(t.getOwner().getCompleteName());
//		su.setEmail(new LoginDAO().getEmailFromUser(t.getOwner().getIdUser()));
//		sus.add(su);
//		t.setMembersSimpleUser(sus);
	}
}
