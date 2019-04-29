package com.cronoteSys.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.CategoryVO;
import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.model.vo.UserVO;

public class ActivityDAO extends GenericsDAO<ActivityVO, Integer> {

	public ActivityDAO() {
		super(ActivityVO.class);
	}

	public Integer countByCategory(CategoryVO cat) {
		return entityManager.createQuery("From ActivityVO a where a.categoryVO = :category")
				.setParameter("category", cat)
				.getResultList().size();
	}
	
	public List<ActivityVO> getList(UserVO user,ProjectVO proj) {
		Query q = entityManager
				.createQuery("From ActivityVO a where a.userVO = :user and a.projectVO = :proj");
		q.setParameter("user", user);
		q.setParameter("proj", proj);
		return q.getResultList();
	}
}