package com.cronoteSys.filter;

import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.model.vo.UserVO;

public class ActivityFilter {

	private ProjectVO project;
	private UserVO user;

	public ActivityFilter() {
	}

	public ActivityFilter(ProjectVO project, UserVO user) {
		super();
		this.project = project;
		this.user = user;
	}

	public ProjectVO getProject() {
		return project;
	}

	public void setProject(ProjectVO project) {
		this.project = project;
	}

	public UserVO getUser() {
		return user;
	}

	public void setUser(UserVO user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "ActivityFilter [project=" + project + ", user=" + user + "]";
	}

	
}
