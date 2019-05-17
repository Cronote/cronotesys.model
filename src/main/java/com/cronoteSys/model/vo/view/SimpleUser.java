package com.cronoteSys.model.vo.view;

import org.hibernate.annotations.Immutable;

@Immutable
public class SimpleUser implements java.io.Serializable {
	private static final long serialVersionUID = 7246780223621961050L;
	private Integer idUser;
	private String completeName;
	private String email;

	public SimpleUser() {
	}
	
	public SimpleUser(Integer iduser, String completeName, String email) {
		super();
		this.idUser = iduser;
		this.completeName = completeName;
		this.email = email;
	}
	
	public SimpleUser(SimpleUser su) {
		this.idUser = su.idUser;
		this.completeName = su.completeName;
		this.email = su.email;
	}

	public Integer getIdUser() {
		return idUser;
	}

	public void setId(Integer idUser) {
		this.idUser = idUser;
	}

	public String getCompleteName() {
		return completeName;
	}

	public void setCompleteName(String completeName) {
		this.completeName = completeName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "SimpleUser [id=" + idUser + ", name=" + completeName + ", email=" + email + "]";
	}

	
	
}
