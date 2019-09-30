package com.cronoteSys.model.vo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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

	public SimpleActivity(ActivityVO act) {
		this.id = act.getId();
		this.title = act.getTitle();
		this.priority = act.getPriority();
		this.categoryVO = act.getCategoryVO();
	}

	public static SimpleActivity fromActivity(ActivityVO act) {
		return new SimpleActivity(act);
	}

	public static List<SimpleActivity> fromList(List<ActivityVO> acts) {
		List<SimpleActivity> lst = new ArrayList<SimpleActivity>();
		for (ActivityVO act : acts) {
			lst.add(new SimpleActivity(act));
		}
		return lst;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoryVO == null) ? 0 : categoryVO.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((priority == null) ? 0 : priority.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		SimpleActivity other = (SimpleActivity) obj;
		if (categoryVO == null) {
			if (other.categoryVO != null)
				return false;
		} else if (!categoryVO.equals(other.categoryVO))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (priority == null) {
			if (other.priority != null)
				return false;
		} else if (!priority.equals(other.priority))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	
	

}
