package com.cronoteSys.model.bo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import com.cronoteSys.model.dao.ProjectDAO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.RestUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class ProjectBO {

	ProjectDAO projectDAO;

	public ProjectBO() {
		projectDAO = new ProjectDAO();
	}

	public ProjectVO save(ProjectVO objProject) {
		objProject.setStats(0);
		objProject.setLastModification(LocalDateTime.now());
		if (RestUtil.isConnectedToTheServer()) {
			objProject = (ProjectVO) RestUtil.post("saveProject", ProjectVO.class, objProject);
		} else {
			objProject = projectDAO.saveOrUpdate(objProject);

		}
		notifyAllProjectAddedListeners(objProject);
		return objProject;

	}

	public ProjectVO update(ProjectVO objProject) {
		objProject.setLastModification(LocalDateTime.now());
		if (RestUtil.isConnectedToTheServer()) {
			return (ProjectVO) RestUtil.post("saveProject", ProjectVO.class, objProject);
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
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(RestUtil.host + "getListProjectByUser?user=" + user);
			List<ProjectVO> activityVOs = new ArrayList<ProjectVO>();
			String string = target.request().get().readEntity(String.class);
			System.out.println(string);
			JsonArray jsonObject = new JsonParser().parse(string).getAsJsonArray();
			for (int i = 0; i < jsonObject.size(); i++) {
				JsonElement element = jsonObject.get(i);
				System.out.println(element.getAsJsonObject().get("id").getAsInt());
			}
			return target.request().get(new ArrayList<ProjectVO>().getClass());
		} else {
			projects = projectDAO.getList(user);
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
