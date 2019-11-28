package com.cronoteSys.model.bo;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

import javax.ws.rs.core.GenericType;

import com.cronoteSys.model.dao.ActivityDAO;
import com.cronoteSys.model.dao.AuditLogDAO;
import com.cronoteSys.model.dao.CategoryDAO;
import com.cronoteSys.model.interfaces.DatabaseLog;
import com.cronoteSys.model.vo.AuditLogVO;
import com.cronoteSys.model.vo.CategoryVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.GsonUtil;
import com.cronoteSys.util.RestUtil;
import com.google.gson.reflect.TypeToken;

public class CategoryBO implements DatabaseLog {

	public CategoryBO() {

	}

	@Override
	public void saveLog(String operation, Object obj) {
		CategoryVO cat = (CategoryVO) obj;
		AuditLogVO audit = new AuditLogVO();
		audit.setAction(operation);
		audit.setTablename("tb_category");
		audit.setUser(cat.getUserVO());
		audit.setDateTime(LocalDateTime.now());
		new AuditLogDAO().saveOrUpdate(audit);

	}

	public CategoryVO save(CategoryVO categoryVO) {
		String operation = categoryVO.getId() == null ? "Insert" : "update";
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.post("saveCategory", categoryVO).readEntity(String.class);

			categoryVO = (CategoryVO) GsonUtil.fromJsonAsStringToObject(json, CategoryVO.class);
		} else {
			categoryVO = new CategoryDAO().saveOrUpdate(categoryVO);

		}
		saveLog(operation, categoryVO);
		return categoryVO;
	}


	public void delete(CategoryVO categoryVO) {
		if (RestUtil.isConnectedToTheServer()) {
			RestUtil.delete("deleteCategory", categoryVO.getId());
		} else {
			new CategoryDAO().delete(categoryVO.getId());
		}
		saveLog("Delete", categoryVO);
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
			String json = RestUtil.get("listCategoryByUsers?search=" + search + "&users=" + users)
					.readEntity(String.class);
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
