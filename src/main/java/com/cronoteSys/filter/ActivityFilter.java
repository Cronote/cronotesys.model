package com.cronoteSys.filter;

import javax.xml.bind.annotation.XmlRootElement;

import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.model.vo.UserVO;

/**
 * @author cabruno
 *
 */
@XmlRootElement
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((project == null) ? 0 : project.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActivityFilter other = (ActivityFilter) obj;
		if (project == null) {
			if (other.project != null)
				return false;
		} else if (!project.equals(other.project))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	
	

	
}
