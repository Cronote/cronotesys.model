package com.cronoteSys.model.dao;

import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.CategoryVO;

public class ActivityDAO extends GenericsDAO<ActivityVO, Integer> {

	public ActivityDAO() {
		super(ActivityVO.class);
	}

	public Integer countByCategory(CategoryVO cat) {
		return entityManager.createQuery("From ActivityVO a where a.categoryVO = :category")
				.setParameter("category", cat)
				.getResultList().size();
	}
}