package com.cronoteSys.model.dao;

import com.cronoteSys.model.vo.ProjectVO;

public class ProjectDAO extends GenericsDAO<ProjectVO, Integer> {

	public ProjectDAO() {
		super(ProjectVO.class);
	}

	public Integer countByTeam(long teamId) {
		return entityManager.createQuery("From ProjectVO p where p.team.id = :team").setParameter("team", teamId)
				.getResultList().size();

	}
}
