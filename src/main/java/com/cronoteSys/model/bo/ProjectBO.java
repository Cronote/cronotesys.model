package com.cronoteSys.model.bo;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cronoteSys.model.dao.ProjectDAO;
import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.GsonUtil;
import com.cronoteSys.util.RestUtil;
import com.google.gson.reflect.TypeToken;

public class ProjectBO {

	ProjectDAO projectDAO;

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
		notifyAllProjectAddedListeners(objProject);
		return objProject;

	}

	public ProjectVO update(ProjectVO objProject) {
		objProject.setLastModification(LocalDateTime.now());
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.post("saveProject", objProject).readEntity(String.class);
			return (ProjectVO) GsonUtil.fromJsonAsStringToObject(json, ProjectVO.class);
		}
		return projectDAO.saveOrUpdate(objProject);
	}

	public void delete(ProjectVO objProject) {
		int projectID = objProject.getId();
		if (projectID == 0)
			return;
		if (RestUtil.isConnectedToTheServer()) {
			RestUtil.delete("deleteProject", projectID);
		} else {
			projectDAO.delete(projectID);
		}
		notifyAllProjectDeletedListeners(objProject);
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

	public void lastModificationToNow(ProjectVO objProject) {
		objProject.setLastModification(LocalDateTime.now());
		update(objProject);
	}

	public void changeStatus(ProjectVO objProject, int status) {
		objProject.setStats(status);
		update(objProject);
	}

	private static ArrayList<OnProjectAddedI> projectAddedListeners = new ArrayList<OnProjectAddedI>();

	public interface OnProjectAddedI {
		void onProjectAddedI(ProjectVO proj);
	}

	public static void addOnProjectAddedIListener(OnProjectAddedI newListener) {
		projectAddedListeners.add(newListener);
	}

	public static void removeOnProjectAddedIListener(OnProjectAddedI newListener) {
		projectAddedListeners.remove(newListener);
	}

	private void notifyAllProjectAddedListeners(ProjectVO act) {
		for (OnProjectAddedI l : projectAddedListeners) {
			l.onProjectAddedI(act);
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
