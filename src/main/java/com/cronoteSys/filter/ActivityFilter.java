package com.cronoteSys.filter;

import java.io.IOException;
import java.util.Objects;

import javax.ws.rs.WebApplicationException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author cabruno
 *
 */
public class ActivityFilter {

	private Integer project;
	private Integer user;
	private Integer activity;
	private Integer category;
	private Integer priority;

	public ActivityFilter() {
		// TODO Auto-generated constructor stub
	}

	public static ActivityFilter valueOf(String jsonRepresentation) {
		ObjectMapper mapper = new ObjectMapper(); // Jackson's JSON marshaller
		ActivityFilter o = null;
		try {
			o = mapper.readValue(jsonRepresentation, ActivityFilter.class);
		} catch (IOException e) {
			throw new WebApplicationException();
		}
		return o;
	}

	public ActivityFilter(Integer project, Integer user) {
		super();
		this.project = project;
		this.user = user;
	}

	public ActivityFilter(Integer project, Integer user, Integer activity, Integer category, Integer priority) {
		super();
		this.project = project;
		this.user = user;
		this.activity = activity;
		this.category = category;
		this.priority = priority;
	}

	public Integer getProject() {
		return project;
	}

	public void setProject(Integer project) {
		this.project = project;
	}

	public Integer getUser() {
		return user;
	}

	public void setUser(Integer user) {
		this.user = user;
	}

	public Integer getActivity() {
		return activity;
	}

	public void setActivity(Integer activity) {
		this.activity = activity;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	@Override
	public int hashCode() {
		return Objects.hash(activity, category, priority, project, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ActivityFilter)) {
			return false;
		}
		ActivityFilter other = (ActivityFilter) obj;
		return Objects.equals(activity, other.activity) && Objects.equals(category, other.category)
				&& Objects.equals(priority, other.priority) && Objects.equals(project, other.project)
				&& Objects.equals(user, other.user);
	}

}
