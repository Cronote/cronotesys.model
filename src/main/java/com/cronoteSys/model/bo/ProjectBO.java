package com.cronoteSys.model.bo;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cronoteSys.model.dao.AuditLogDAO;
import com.cronoteSys.model.dao.ProjectDAO;
import com.cronoteSys.model.interfaces.DatabaseLog;
import com.cronoteSys.model.vo.AuditLogVO;
import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.GsonUtil;
import com.cronoteSys.util.RestUtil;
import com.google.gson.reflect.TypeToken;

public class ProjectBO implements DatabaseLog {

	ProjectDAO projectDAO;

	@Override
	public void saveLog(String operation, Object obj) {
		AuditLogVO audit = new AuditLogVO();
		audit.setAction(operation);
		audit.setDateTime(LocalDateTime.now());
		audit.setTablename("tb_product");
		audit.setUser((UserVO) obj);
		new AuditLogDAO().saveOrUpdate(audit);
	}

	public ProjectBO() {
		projectDAO = new ProjectDAO();
	}

	public ProjectVO save(ProjectVO objProject) {
		objProject.setStats(0);
		objProject.setLastModification(LocalDateTime.now());
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.post("saveProject", objProject).readEntity(String.class);
			objProject = (ProjectVO) GsonUtil.fromJsonAsStringToObject(json, ProjectVO.class);
		} else {
			objProject = projectDAO.saveOrUpdate(objProject);

		}
		notifyAllProjectAddedListeners(objProject, "save");
		saveLog("Insert", objProject.getUserVO());
		return objProject;

	}

	public ProjectVO update(ProjectVO objProject, UserVO user) {
		objProject.setLastModification(LocalDateTime.now());
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.post("saveProject", objProject).readEntity(String.class);
			objProject = (ProjectVO) GsonUtil.fromJsonAsStringToObject(json, ProjectVO.class);
		} else {

			objProject = projectDAO.saveOrUpdate(objProject);
		}
		notifyAllProjectAddedListeners(objProject, "update");
		saveLog("update", user);
		return objProject;
	}

	public void delete(ProjectVO objProject, UserVO user) {
		int projectID = objProject.getId();
		if (projectID == 0)
			return;
		if (RestUtil.isConnectedToTheServer()) {
			RestUtil.delete("deleteProject", projectID);
		} else {
			projectDAO.delete(projectID);
		}
		notifyAllProjectDeletedListeners(objProject);
		saveLog("delete", user);
	}

	public List<ProjectVO> listAll(UserVO user) {
		List<ProjectVO> projects = new ArrayList<ProjectVO>();
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.get("getListProjectByUser?userid=" + user.getIdUser()).readEntity(String.class);
			Type projectListType = new TypeToken<List<ProjectVO>>() {
			}.getType();
			List<ProjectVO> lst = GsonUtil.getGsonWithJavaTime().fromJson(json, projectListType);
			return lst;
		} else {
			projects = projectDAO.getList(user.getIdUser());
		}
		if (!projects.isEmpty() && projects.get(0).getId() == 0)
			projects.remove(0);
		return projects;
	}

	public void lastModificationToNow(ProjectVO objProject, UserVO user) {
		objProject.setLastModification(LocalDateTime.now());
		update(objProject, user);
	}

	public void changeStatus(ProjectVO objProject, int status, UserVO user) {
		objProject.setStats(status);
		update(objProject, user);
	}

	private static ArrayList<OnProjectAddedI> projectAddedListeners = new ArrayList<OnProjectAddedI>();

	public interface OnProjectAddedI {
		void onProjectAddedI(ProjectVO proj, String mode);
	}

	public static void addOnProjectAddedIListener(OnProjectAddedI newListener) {
		projectAddedListeners.add(newListener);
	}

	public static void removeOnProjectAddedIListener(OnProjectAddedI newListener) {
		projectAddedListeners.remove(newListener);
	}

	private void notifyAllProjectAddedListeners(ProjectVO act, String mode) {
		for (OnProjectAddedI l : projectAddedListeners) {
			l.onProjectAddedI(act, mode);
		}
	}

	private static ArrayList<OnProjectDeletedI> projectDeletedListeners = new ArrayList<OnProjectDeletedI>();

	public interface OnProjectDeletedI {
		void onProjectDeleted(ProjectVO proj);
	}

	public static void addOnProjectDeletedListener(OnProjectDeletedI newListener) {
		projectDeletedListeners.add(newListener);
	}

	public static void removeOnProjectDeletedListener(OnProjectDeletedI newListener) {
		projectDeletedListeners.remove(newListener);
	}

	private void notifyAllProjectDeletedListeners(ProjectVO proj) {
		for (OnProjectDeletedI l : projectDeletedListeners) {
			l.onProjectDeleted(proj);
		}
	}

}
