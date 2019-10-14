package com.cronoteSys.model.bo;

import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.core.GenericType;

import com.cronoteSys.model.dao.ActivityDAO;
import com.cronoteSys.model.dao.CategoryDAO;
import com.cronoteSys.model.vo.CategoryVO;
import com.cronoteSys.model.vo.TeamVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.GsonUtil;
import com.cronoteSys.util.RestUtil;
import com.google.gson.reflect.TypeToken;

public class CategoryBO {

	public CategoryBO() {

	}

	public CategoryVO save(CategoryVO categoryVO) {
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.post("saveCategory", categoryVO).readEntity(String.class);

			return (CategoryVO) GsonUtil.fromJsonAsStringToObject(json, CategoryVO.class);
		}
		return new CategoryDAO().saveOrUpdate(categoryVO);
	}

	public void update(CategoryVO categoryVO) {
		if (RestUtil.isConnectedToTheServer()) {
			RestUtil.post("saveCategory", categoryVO);
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
			count = Integer.parseInt(
					RestUtil.get("countByCategory?categoryID=" + categoryVO.getId()).readEntity(String.class));
		} else {
			count = new ActivityDAO().countByCategory(categoryVO.getId());
		}
		if (count > 0)
			return false;
		return true;
	}

	public List<CategoryVO> listByUsers(String search, String users) {
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.get("listCategoryByUsers?search=" + search + "&users=" + users).readEntity(String.class);
			Type categoryListType = new TypeToken<List<CategoryVO>>() {
			}.getType();

			return GsonUtil.getGsonWithJavaTime().fromJson(json, categoryListType);

		}
		return new CategoryDAO().listByDescriptionAndUser("", users);
	}

	public List<CategoryVO> listByUser(UserVO user) {
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.get("listCategoryByUser?userID=" + user.getIdUser()).readEntity(String.class);
			Type categoryListType = new TypeToken<List<CategoryVO>>() {
			}.getType();

			return GsonUtil.getGsonWithJavaTime().fromJson(json, categoryListType);

		}
		return new CategoryDAO().getList(user.getIdUser());
	}

	public List<CategoryVO> listAll() {
		if (RestUtil.isConnectedToTheServer()) {
			return RestUtil.get("listAllCategory").readEntity(new GenericType<List<CategoryVO>>() {
			});
		}
		return new CategoryDAO().getList();
	}
}
