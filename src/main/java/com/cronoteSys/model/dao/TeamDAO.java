package com.cronoteSys.model.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;

import com.cronoteSys.model.bo.LoginBO;
import com.cronoteSys.model.vo.TeamUser;
import com.cronoteSys.model.vo.TeamVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.model.vo.relation.side.TeamMember;
import com.cronoteSys.model.vo.view.SimpleUser;

public class TeamDAO extends GenericsDAO<TeamVO, Long> {

	public TeamDAO() {
		super(TeamVO.class);
	}

	public TeamVO saveOrUpdate(TeamVO team) {
		TeamVO savedTeam = super.saveOrUpdate(team);
		savedTeam.setMembers(team.getMembers());
		savedTeam.switchMembersBetweenLists(true);
		System.out.println("size " + savedTeam.getTeamUser().size());
		savedTeam = super.saveOrUpdate(savedTeam);
//		fillMembers(team);
		return savedTeam;

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

			teams = entityManager.createNativeQuery("Select distinct t.id,t.name,t.description,t.id_user,t.teamColor"
					+ " from tb_team t" + " left join teamuser tu on t.id = tu.team " + " where t.id_user =" + userId
					+ " or tu.member in(" + userId + ")", TeamVO.class).getResultList();

		} catch (HibernateException e) {
			System.out.println("Problem on list " + e.getMessage());
		}
		return teams;
	}
}
