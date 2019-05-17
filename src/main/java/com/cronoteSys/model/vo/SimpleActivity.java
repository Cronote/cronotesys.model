package com.cronoteSys.model.vo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Immutable;

//@Entity
//@Immutable
public class SimpleActivity implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String title;
	private Integer priority;
	private CategoryVO categoryVO;

	public SimpleActivity() {

	}

	public SimpleActivity(int idActivity, String title, Integer priority, CategoryVO categoryVO) {
		this.id = idActivity;
		this.title = title;
		this.priority = priority;
		this.categoryVO = categoryVO;
	}

	@Id
	@Column(name = "id_activity", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "title", nullable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "priority", nullable = false)
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_category")
	public CategoryVO getCategoryVO() {
		return categoryVO;
	}

	public void setCategoryVO(CategoryVO categoryVO) {
		this.categoryVO = categoryVO;
	}

	@Override
	public String toString() {
		return "ActivityView [id=" + id + ", title=" + title + ", priority=" + priority + ", categoryVO=" + categoryVO
				+ "]";
	}

}
