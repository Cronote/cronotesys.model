package com.cronoteSys.model.bo;

import java.util.List;

import com.cronoteSys.model.dao.ActivityDAO;
import com.cronoteSys.model.dao.CategoryDAO;
import com.cronoteSys.model.vo.CategoryVO;

public class CategoryBO {

	public CategoryBO() {

	}

	public CategoryVO save(CategoryVO activityVO) {
		return new CategoryDAO().saveOrUpdate(activityVO);
	}

	public void update(CategoryVO activityVO) {
		new CategoryDAO().saveOrUpdate(activityVO);
	}

	public void delete(CategoryVO activityVO) {
		new CategoryDAO().delete(activityVO.getId());
	}

	public boolean canBeDeleted(CategoryVO categoryVO) {
		int count = new ActivityDAO().countByCategory(categoryVO);
		if (count > 0)
			return false;
		return true;
	}

	public List<CategoryVO> listAll() {
		return new CategoryDAO().getList();
	}
}
