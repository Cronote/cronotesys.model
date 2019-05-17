package com.cronoteSys.model.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.cronoteSys.filter.ActivityFilter;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.CategoryVO;
import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.model.vo.SimpleActivity;
import com.cronoteSys.model.vo.UserVO;

public class ActivityDAO extends GenericsDAO<ActivityVO, Integer> {

	public ActivityDAO() {
		super(ActivityVO.class);
	}

	public Integer countByCategory(CategoryVO cat) {
		return entityManager.createQuery("From ActivityVO a where a.categoryVO = :category")
				.setParameter("category", cat).getResultList().size();
	}

	public List<ActivityVO> getList(UserVO user, ProjectVO proj) {
		Query q = entityManager.createQuery("From ActivityVO a where a.userVO = :user and a.projectVO = :proj");
		q.setParameter("user", user);
		q.setParameter("proj", proj);
		return q.getResultList();
	}


	public List<SimpleActivity> getSimpleActivitiesView(ActivityFilter filter) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		CriteriaQuery<SimpleActivity> criteria = builder.createQuery(SimpleActivity.class);
		Root<ActivityVO> root = criteria.from(ActivityVO.class);

		Path<Integer> idPath = root.get("id");
		Path<String> title = root.get("title");
		Path<Integer> priority = root.get("priority");
		Path<CategoryVO> category = root.get("categoryVO");

		criteria.select(builder.construct(SimpleActivity.class, idPath, title, priority, category));
		if (filter != null) {
			List<Predicate> p =  new ArrayList<Predicate>();
			if (filter.getUser() != null) {
				p.add(builder.equal(root.get("userVO"), filter.getUser()));
			}
			if (filter.getProject() != null) {
				p.add(builder.equal(root.get("projectVO"), filter.getProject()));
			}
			criteria.where(builder.and(p.toArray(new Predicate[p.size()])));
		}
		List<SimpleActivity> wrappers = entityManager.createQuery(criteria).getResultList();
		return wrappers;
	}
}