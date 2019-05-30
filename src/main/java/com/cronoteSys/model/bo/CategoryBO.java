package com.cronoteSys.model.bo;

import java.util.List;

import com.cronoteSys.model.dao.ActivityDAO;
import com.cronoteSys.model.dao.CategoryDAO;
import com.cronoteSys.model.vo.CategoryVO;
import com.cronoteSys.util.RestUtil;

public class CategoryBO {

	public CategoryBO() {

	}

	public CategoryVO save(CategoryVO categoryVO) {
		if (RestUtil.isConnectedToTheServer()) {
			return (CategoryVO) RestUtil.post("saveCategory", CategoryVO.class, categoryVO);
		}
		return new CategoryDAO().saveOrUpdate(categoryVO);
	}

	public void update(CategoryVO categoryVO) {
		if (RestUtil.isConnectedToTheServer()) {
			RestUtil.post("saveCategory", CategoryVO.class, categoryVO);
		} else {
			new CategoryDAO().saveOrUpdate(categoryVO);
		}
	}

	public void delete(CategoryVO categoryVO) {
		if (RestUtil.isConnectedToTheServer()) {
			RestUtil.delete("deleteCategory", categoryVO.getId());
		}
		new CategoryDAO().delete(categoryVO.getId());
	}

	public boolean canBeDeleted(CategoryVO categoryVO) {
		int count = 0;
		if (RestUtil.isConnectedToTheServer()) {
			count = Integer.parseInt(RestUtil.get("countByCategory",Long.class).toString()); 
		} else {
			count = new ActivityDAO().countByCategory(categoryVO);
		}
		if (count > 0)
			return false;
		return true;
	}

	public List<CategoryVO> listAll() {
		if(RestUtil.isConnectedToTheServer()) {
			return (List<CategoryVO>) RestUtil.get("listAllCategory",List.class);
		}
		return new CategoryDAO().getList();
	}
}
