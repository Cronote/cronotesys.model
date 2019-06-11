package com.cronoteSys.model.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.ExecutionTimeVO;
import com.cronoteSys.model.vo.UserVO;

public class ExecutionTimeDAO extends GenericsDAO<ExecutionTimeVO, Integer> {

	public ExecutionTimeDAO() {
		super(ExecutionTimeVO.class);
	}

	public List<ExecutionTimeVO> listByActivity(int activityID) {
		Query q = entityManager
				.createQuery("select p FROM ExecutionTimeVO p where p.activityVO.id=:activity ",ExecutionTimeVO.class);
		q.setParameter("activity", activityID);
		 List<ExecutionTimeVO> lst = q.getResultList();
		return lst;
	}

	public int executionInProgressByUser(UserVO userVO) {
		Query q = entityManager.createQuery("SELECT p FROM ExecutionTimeVO p WHERE p.finishDate = null and p.activityVO.userVO = :user",ExecutionTimeVO.class);
		q.setParameter("user", userVO);
		return q.getResultList().size();

	}

	public ExecutionTimeVO executionInProgress(int activityID) {
		Query q = entityManager.createQuery("SELECT p FROM ExecutionTimeVO p WHERE p.activityVO.id = :activity and p.finishDate = null",ExecutionTimeVO.class);
		q.setParameter("activity", activityID);
		try {
			return (ExecutionTimeVO) q.getSingleResult();
		} catch (NoResultException e) {
			e.printStackTrace();
			return null;
		}

	}

}
