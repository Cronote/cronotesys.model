package com.cronoteSys.model.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;

import com.cronoteSys.model.vo.TeamVO;
import com.cronoteSys.model.vo.UserVO;

public class TeamDAO extends GenericsDAO<TeamVO, Long> {

	public TeamDAO() {
		super(TeamVO.class);
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

	public List<TeamVO> listByUser(UserVO userId) {
		try {
			List<TeamVO> teams = null;
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();

			CriteriaQuery<TeamVO> criteria = builder.createQuery(TeamVO.class);
			Root<TeamVO> root = criteria.from(TeamVO.class);
//			Join<RelTeamUser, TeamVO> join = root.join("id", JoinType.INNER);
			// Root<TeamVO> root = criteria.from(TeamVO.class);
//			Join<TeamVO, RelTeamUser> join = root.join("members", JoinType.INNER);
//			List<Integer> myList = new ArrayList<Integer>();
//			myList.add(3);
//			Expression<Integer> exp = root.get("id");
			criteria.select(root);
//			Predicate predicate = exp.in(myList);
//			criteria.where(builder.and(builder.equal(join.get("id_team"), 3)));
			teams = entityManager.createQuery(criteria).getResultList();
			System.out.println(teams.size());
			return teams;
		} catch (HibernateException e) {
			System.out.println("Problem on list " + e.getMessage());
		}
		return null;
	}
}
