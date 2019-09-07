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

	public String getTeamName(int id) {
		String team = entityManager.createNativeQuery("SELECT name	FROM public.tb_team WHERE id="+id+";").getResultList().get(0).toString();
		if(team.isEmpty()) {
			return null;
		}
		return team;
	}
	
	public boolean inviteAccepted2(int member, int team) {
		try {
			TeamVO t = entityManager.createQuery("select t from TeamVO t where t.id ="+ team, TeamVO.class).getSingleResult();
			for (TeamUser tu : t.getTeamUser()) {
				if (tu.getMember().getIdUser() == member) {
					tu.setInviteAccepted(true);
				}
			}
			t = saveOrUpdate(t);
			//			entityManager.createNativeQuery("UPDATE teamuser SET inviteaccepted=true WHERE member="+member+" AND team="+team+";").executeUpdate();
			return true; 
		}catch (HibernateException e) {
			e.printStackTrace();
		}
		return false;
	}
}
