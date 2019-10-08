package com.cronoteSys.filter;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author cabruno
 *
 */
public class ActivityFilter  {

	private Integer project;
	private Integer user;
	private Integer activity;

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
}
