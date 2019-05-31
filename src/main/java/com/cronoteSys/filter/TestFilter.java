package com.cronoteSys.filter;

import java.io.IOException;
import java.io.Serializable;

import javax.ws.rs.WebApplicationException;
import javax.xml.bind.annotation.XmlRootElement;

import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.model.vo.UserVO;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author cabruno
 *
 */
public class TestFilter {

	/**
	 * 
	 */
	private Integer project;

	public Integer getProject() {
		return project;
	}

	public void setProject(Integer project) {
		this.project = project;
	}

	public static TestFilter valueOf(String jsonRepresentation) {
		ObjectMapper mapper = new ObjectMapper(); // Jackson's JSON marshaller
		TestFilter o = null;
		try {
			o = mapper.readValue(jsonRepresentation, TestFilter.class);
		} catch (IOException e) {
			throw new WebApplicationException();
		}
		return o;
	}
}
