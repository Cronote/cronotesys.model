package com.cronoteSys.model.bo;

import java.time.LocalDateTime;
import java.util.List;

import com.cronoteSys.model.dao.ProjectDAO;
import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.model.vo.UserVO;

public class ProjectBO {

	ProjectDAO projectDAO;

	public ProjectBO() {
		projectDAO = new ProjectDAO();
	}

	public ProjectVO save(ProjectVO objProject) {
		objProject.setStats(0);
		objProject.setLastModification(LocalDateTime.now());
		return projectDAO.saveOrUpdate(objProject);
	}

	public ProjectVO update(ProjectVO objProject) {
		objProject.setLastModification(LocalDateTime.now());
		return projectDAO.saveOrUpdate(objProject);
	}

	public void delete(ProjectVO objProject) {
		int projectID = objProject.getId();
		if (projectID == 0)
			return;
		projectDAO.delete(projectID);
	}

	public List<ProjectVO> listAll(UserVO user) {
		List<ProjectVO> projects = projectDAO.getList(user);
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
}
