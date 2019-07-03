package com.cronoteSys.model.dao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.criterion.Order;

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

	public Integer countByCategory(int catId) {
		return entityManager.createQuery("From ActivityVO a where a.categoryVO.id = :category")
				.setParameter("category", catId).getResultList().size();
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
		criteria.where(makePredicates(filter, builder, root));
		List<SimpleActivity> wrappers = entityManager.createQuery(criteria).getResultList();
		return wrappers;
	}

	public List<ActivityVO> getFiltredList(ActivityFilter filter) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		CriteriaQuery<ActivityVO> criteria = builder.createQuery(ActivityVO.class);
		Root<ActivityVO> root = criteria.from(ActivityVO.class);

		criteria.select(root);
		criteria.where(makePredicates(filter, builder, root));
		criteria.orderBy(builder.desc(root.get("priority")));
		List<ActivityVO> wrappers = entityManager.createQuery(criteria).getResultList();
		wrappers.sort(new Comparator<ActivityVO>() {
			@Override
			public int compare(ActivityVO o1, ActivityVO o2) {
				int count1 = o1.getDependencies().size();
				int count2 = o2.getDependencies().size();
				if (count1 > count2)
					return 1;
				else if (count1 < count2)
					return -1;
				else
					return 0;
			}
		});
		return wrappers;
	}

	private Predicate makePredicates(ActivityFilter filter, CriteriaBuilder builder, Root<ActivityVO> root) {
		if (filter != null) {
			List<Predicate> p = new ArrayList<Predicate>();
			if (filter.getUser() != null) {
				p.add(builder.equal(root.get("userVO"), filter.getUser()));
			}
			if (filter.getProject() != null) {
				p.add(builder.equal(root.get("projectVO"), filter.getProject()));
			} else {
				p.add(builder.isNull(root.get("projectVO")));
			}
			return builder.and(p.toArray(new Predicate[p.size()]));
		}
		return null;
	}
}