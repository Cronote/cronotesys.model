package com.cronoteSys.model.dao;

import java.util.List;

import javax.persistence.NoResultException;

import com.cronoteSys.model.vo.CategoryVO;
import com.cronoteSys.model.vo.UserVO;

public class CategoryDAO extends GenericsDAO<CategoryVO, Integer> {

	public CategoryDAO() {
		super(CategoryVO.class);
	}

	public CategoryVO findByDescriptionAndUser(String descript, UserVO user) {
		try {
			return (CategoryVO) entityManager.createQuery("From c where c.description = :desc and c.userVO= :user")
					.setParameter("desc", descript).setParameter("user", user).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<CategoryVO> listByDescriptionAndUser(String descript, UserVO user) {
		return entityManager.createQuery("From CategoryVO c where c.description like :desc and c.userVO= :user")
				.setParameter("desc","%" +descript+"%").setParameter("user", user).getResultList();
	}
}
