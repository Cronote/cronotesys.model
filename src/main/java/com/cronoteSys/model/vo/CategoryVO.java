package com.cronoteSys.model.vo;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_category")
public class CategoryVO implements java.io.Serializable {

	private static final long serialVersionUID = 2254856704552101908L;

	private Integer id;
	private String description;
	private UserVO userVO;

	public CategoryVO() {

	}

	public CategoryVO(int idCategory, String description, UserVO userVO) {
		this.id = idCategory;
		this.description = description;
		this.userVO = userVO;
	}

	@Id
	@Column(name = "id_category", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "description", nullable = false)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_user", referencedColumnName = "id_user")
	public UserVO getUserVO() {
		return userVO;
	}

	public void setUserVO(UserVO userVO) {
		this.userVO = userVO;
	}

	@Override
	public String toString() {
		return "CategoryVO [id=" + id + ", description=" + description + ", userVO=" + userVO + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CategoryVO)) {
			return false;
		}
		CategoryVO other = (CategoryVO) obj;
		return Objects.equals(id, other.id);
	}



}
