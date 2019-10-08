package com.cronoteSys.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.cronoteSys.model.vo.ProjectVO;

public class ProjectDAO extends GenericsDAO<ProjectVO, Integer> {

	public ProjectDAO() {
		super(ProjectVO.class);
	}

	public Integer countByTeam(long teamId) {
		return entityManager.createQuery("From ProjectVO p where p.team.id = :team").setParameter("team", teamId)
				.getResultList().size();

	}
	
	
	public List<ProjectVO> getList(int id,String teamIds ) {
		Query q = entityManager
				.createQuery("SELECT p FROM ProjectVO p WHERE p.userVO.id = :user OR p.team in " +teamIds );
		q.setParameter("user", id);
		return q.getResultList();
	}
}
