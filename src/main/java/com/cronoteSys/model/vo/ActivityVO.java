package com.cronoteSys.model.vo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "tb_activity")
public class ActivityVO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String title;
	private String description;
	private Duration estimatedTime;
	private StatusEnum stats;
	private Duration realtime;
	private Integer priority;
	private LocalDateTime lastModification;
	private UserVO userVO;
	private ProjectVO projectVO;
	private CategoryVO categoryVO;
	private List<ActivityVO> dependencies;
	
	private transient boolean itsDependecy = false;
	
	/**
	 * empty constructor
	 * 
	 * */
	public ActivityVO() {

	}
	/**
	 * Constructor with not null properties
	 * @param idActivity -> an int
	 * @param title -> a String
	 * @param estimatedTime -> a {@link Duration} of time to do this activity
	 * @param stats -> a {@link #StatusEnum} wich is the status of this activity
	 * @param realtime -> a {@link #Duration} the real time used to have this activity done
	 * @param priority -> a int to describe the priority of this activity
	 * @param lastModification -> a {@link #LocalDateTime} to describe when was the last change in this register
	 * @param userVO -> {@link #UserVO} owner 
	 * @param projectVO -> {@link #ProjectVO} 
	 * @param categoryVO -> {@link #CategoryVO} 
	 * 
	 * */
	public ActivityVO(int idActivity, String title, Duration estimatedTime, StatusEnum stats, Duration realtime,
			Integer priority, LocalDateTime lastModification, UserVO userVO, ProjectVO projectVO,
			CategoryVO categoryVO) {
		this.id = idActivity;
		this.title = title;
		this.estimatedTime = estimatedTime;
		this.stats = stats;
		this.realtime = realtime;
		this.priority = priority;
		this.lastModification = lastModification;
		this.userVO = userVO;
		this.projectVO = projectVO;
		this.categoryVO = categoryVO;
	}

	/**
	 *  
	 * Constructor from a {@link #SimpleActivity} object
	 * 
	 * */
	public ActivityVO(SimpleActivity sa) {
		this.id = sa.getId();
		this.title = sa.getTitle();
		this.priority = sa.getPriority();
		this.categoryVO = sa.getCategoryVO();
	}
	
	/**
	 * Copy constructor
	 * Starts another instance of activity with the same values
	 * */
	public ActivityVO(ActivityVO activity) {
		this.id = activity.id;
		this.title = activity.title;
		this.description = activity.description;
		this.estimatedTime = activity.estimatedTime;
		this.stats = activity.stats;
		this.realtime = activity.realtime;
		this.priority = activity.priority;
		this.lastModification = activity.lastModification;
		this.categoryVO = activity.categoryVO;
		this.userVO = activity.userVO;
		this.projectVO = activity.projectVO;
		this.dependencies = activity.dependencies;
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

	@Column(name = "description", nullable = true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "estimated_time", nullable = false)
	public Duration getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(Duration estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	@Column(name = "stats", nullable = false)
	public StatusEnum getStats() {
		return stats;
	}

	public void setStats(StatusEnum stats) {
		this.stats = stats;
	}

	@Column(name = "real_time", nullable = true)
	public Duration getRealtime() {
		return realtime;
	}

	public void setRealtime(Duration realtime) {
		this.realtime = realtime;
	}

	@Column(name = "priority", nullable = false)
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	@Column(name = "last_modification")
	public LocalDateTime getLastModification() {
		return lastModification;
	}

	public void setLastModification(LocalDateTime lastModification) {
		this.lastModification = lastModification;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_user", referencedColumnName = "id_user")
	public UserVO getUserVO() {
		return userVO;
	}

	public void setUserVO(UserVO userVO) {
		this.userVO = userVO;
	}

	@ManyToOne(optional = true)
	@JoinColumn(name = "id_project", referencedColumnName = "id_project")
	public ProjectVO getProjectVO() {
		return projectVO;
	}

	public void setProjectVO(ProjectVO projectVO) {
		this.projectVO = projectVO;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_category", referencedColumnName = "id_category")
	public CategoryVO getCategoryVO() {
		return categoryVO;
	}

	public void setCategoryVO(CategoryVO categoryVO) {
		this.categoryVO = categoryVO;
	}

	@ManyToMany
	@JoinTable(name = "dependencies", joinColumns = { @JoinColumn(name = "id_activity") }, inverseJoinColumns = {
			@JoinColumn(name = "id_dependency") })
	public List<ActivityVO> getDependencies() {
		return dependencies != null ? dependencies : new ArrayList<ActivityVO>();
	}

	public void setDependencies(List<ActivityVO> dependencies) {
		this.dependencies = dependencies;
	}
	public void setDependenciesFromSimple(List<SimpleActivity> dependencies) {
		List<ActivityVO> lst = new ArrayList<ActivityVO>();
		for (SimpleActivity simple : dependencies) {
			lst.add(new ActivityVO(simple));
		}
		this.dependencies = lst;
	}
	
	@Transient
	public boolean itsDependency() {
		return itsDependecy;
	}
	
	public void setItsDependency(boolean its) {
		this.itsDependecy = its;
	}
	@Transient
	public String getEstimatedTimeAsString() {
		try {
			long seconds = getEstimatedTime().getSeconds();
			long absSeconds = Math.abs(seconds);
			String positive = String.format("%02d:%02d:%02d", absSeconds / 3600, (absSeconds % 3600) / 60,
					absSeconds % 60);
			return positive;
		} catch (Exception e) {
			// TODO: handle exception
			return String.format("%02d:%02d ", 0, 0);
		}
	}

	@Transient
	public String getRealtimeAsString() {
		try {
			long seconds = getRealtime().getSeconds();
			long absSeconds = Math.abs(seconds);
			String positive = String.format("%02d:%02d:%02d", absSeconds / 3600, (absSeconds % 3600) / 60,
					absSeconds % 60);
			return positive;
		} catch (Exception e) {
			// TODO: handle exception
			return String.format("%02d:%02d ", 0, 0);
		}
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoryVO == null) ? 0 : categoryVO.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((estimatedTime == null) ? 0 : estimatedTime.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastModification == null) ? 0 : lastModification.hashCode());
		result = prime * result + ((priority == null) ? 0 : priority.hashCode());
		result = prime * result + ((projectVO == null) ? 0 : projectVO.hashCode());
		result = prime * result + ((realtime == null) ? 0 : realtime.hashCode());
		result = prime * result + ((stats == null) ? 0 : stats.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((userVO == null) ? 0 : userVO.hashCode());
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
		ActivityVO other = (ActivityVO) obj;
		if (categoryVO == null) {
			if (other.categoryVO != null)
				return false;
		} else if (!categoryVO.equals(other.categoryVO))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (estimatedTime == null) {
			if (other.estimatedTime != null)
				return false;
		} else if (!estimatedTime.equals(other.estimatedTime))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastModification == null) {
			if (other.lastModification != null)
				return false;
		} else if (!lastModification.equals(other.lastModification))
			return false;
		if (priority == null) {
			if (other.priority != null)
				return false;
		} else if (!priority.equals(other.priority))
			return false;
		if (projectVO == null) {
			if (other.projectVO != null)
				return false;
		} else if (!projectVO.equals(other.projectVO))
			return false;
		if (realtime == null) {
			if (other.realtime != null)
				return false;
		} else if (!realtime.equals(other.realtime))
			return false;
		if (stats != other.stats)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (userVO == null) {
			if (other.userVO != null)
				return false;
		} else if (!userVO.equals(other.userVO))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ActivityVO [id=" + id + ", title=" + title + ", description=" + description + ", estimatedTime="
				+ estimatedTime + ", stats=" + stats + ", realtime=" + realtime + ", priority=" + priority
				+ ", lastModification=" + lastModification + ", userVO=" + userVO + ", projectVO=" + projectVO
				+ ", categoryVO=" + categoryVO + ", dependencies=" + dependencies + "]";
	}

	
	
}
