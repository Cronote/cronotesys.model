package com.cronoteSys.model.vo;

import java.time.LocalDateTime;
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
@Table(name = "tb_project")
public class ProjectVO implements java.io.Serializable {

	private static final long serialVersionUID = -3856789567100198726L;
	private Integer id;
	private String title;
	private String description;
	private LocalDateTime lastModification;
	private LocalDateTime startDate;
	private LocalDateTime finishDate;
	private Integer stats;
	private UserVO userVO;
	private TeamVO team;

	public ProjectVO() {

	}

	public ProjectVO(int idProject, String title, String description, LocalDateTime lastModification,
			LocalDateTime startDate, LocalDateTime finishDate, int stats, UserVO userVO, TeamVO team) {
		this.id = idProject;
		this.title = title;
		this.description = description;
		this.lastModification = lastModification;
		this.startDate = startDate;
		this.finishDate = finishDate;
		this.stats = stats;
		this.userVO = userVO;
		this.team = team;
	}

	@Id
	@Column(name = "id_project")
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

	@Column(name = "last_modification", nullable = false)
	public LocalDateTime getLastModification() {
		return lastModification;
	}

	public void setLastModification(LocalDateTime lastModification) {
		this.lastModification = lastModification;
	}

	@Column(name = "start_date", nullable = false)
	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	@Column(name = "finish_date", nullable = false)
	public LocalDateTime getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(LocalDateTime finishDate) {
		this.finishDate = finishDate;
	}

	@Column(name = "stats", nullable = false)
	public Integer getStats() {
		return stats;
	}

	public void setStats(Integer stats) {
		this.stats = stats;
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
	@JoinColumn(name = "id_team", referencedColumnName = "id")
	public TeamVO getTeam() {
		return team;
	}

	public void setTeam(TeamVO team) {
		this.team = team;
	}

	@Override
	public String toString() {
		return "ProjectVO [id=" + id + ", title=" + title + ", description=" + description + ", lastModification="
				+ lastModification + ", startDate=" + startDate + ", finishDate=" + finishDate + ", stats=" + stats
				+ ", userVO=" + userVO + "]";
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
		if (!(obj instanceof ProjectVO)) {
			return false;
		}
		ProjectVO other = (ProjectVO) obj;
		return Objects.equals(id, other.id);
	}

}
