package com.cronoteSys.model.vo;
// Generated 25/06/2018 22:33:40 by Hibernate Tools 4.3.1

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.cronoteSys.model.vo.view.SimpleUser;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * TbUser generated by hbm2java
 */
@Entity
@Table(name = "tb_user")
public class UserVO implements java.io.Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Integer idUser;
	private String completeName;
	private LocalDate birthDate;
	private LocalDateTime registerDate;
	private String emailRecover;
	private byte stats;
	private String avatarPath;
	private transient List<TeamUser> teamUsers = new ArrayList<TeamUser>();

	public UserVO() {
	}

	public UserVO(String completeName, LocalDate birthDate, byte stats) {
		this.completeName = completeName;
		this.birthDate = birthDate;
		this.stats = stats;
	}

	public UserVO(SimpleUser su) {
		this.idUser = su.getIdUser();
		this.completeName = su.getCompleteName();
	}

	public UserVO(Integer id) {
		this.idUser = id;
	}

	public UserVO(Integer id, String completeName, LocalDate birthDate, byte stats) {
		this.idUser = id;
		this.completeName = completeName;
		this.birthDate = birthDate;
		this.stats = stats;
	}

	public UserVO(String completeName, LocalDate birthDate, LocalDateTime registerDate, String emailRecover, byte stats,
			String avatarPath) {
		this.completeName = completeName;
		this.birthDate = birthDate;
		this.registerDate = registerDate;
		this.emailRecover = emailRecover;
		this.stats = stats;
		this.avatarPath = avatarPath;
	}

	@Id
	@Column(name = "id_user")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "user_id_user_seq")
	public Integer getIdUser() {
		return this.idUser;
	}

	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}

	@Column(name = "complete_name", nullable = false)
	public String getCompleteName() {
		return this.completeName;
	}

	public void setCompleteName(String completeName) {
		this.completeName = completeName;
	}

	@Column(name = "birth_date", nullable = true)
	public LocalDate getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	@Column(name = "register_date")
	public LocalDateTime getRegisterDate() {
		return this.registerDate;
	}

	public void setRegisterDate(LocalDateTime registerDate) {
		this.registerDate = registerDate;
	}

	@Column(name = "email_recover")
	public String getEmailRecover() {
		return this.emailRecover;
	}

	public void setEmailRecover(String emailRecover) {
		this.emailRecover = emailRecover;
	}

	@Column(name = "stats", nullable = false)
	public byte getStats() {
		return this.stats;
	}

	public void setStats(byte stats) {
		this.stats = stats;
	}

	@Column(name = "avatar_path")
	public String getAvatarPath() {
		return this.avatarPath;
	}

	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}
	
	@OneToMany(mappedBy = "primaryKey.member", 
			cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<TeamUser> getTeamUser() {
		return teamUsers;
	}

	public void setTeamUser(List<TeamUser> teamUsers) {
		this.teamUsers = teamUsers;
	}
	
	public void addTeamUser(TeamUser teamUsers) {
		this.teamUsers.add(teamUsers);
	}	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((avatarPath == null) ? 0 : avatarPath.hashCode());
		result = prime * result + ((birthDate == null) ? 0 : birthDate.hashCode());
		result = prime * result + ((completeName == null) ? 0 : completeName.hashCode());
		result = prime * result + ((emailRecover == null) ? 0 : emailRecover.hashCode());
		result = prime * result + ((idUser == null) ? 0 : idUser.hashCode());
		result = prime * result + ((registerDate == null) ? 0 : registerDate.hashCode());
		result = prime * result + stats;
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
		UserVO other = (UserVO) obj;
		if (avatarPath == null) {
			if (other.avatarPath != null)
				return false;
		} else if (!avatarPath.equals(other.avatarPath))
			return false;
		if (birthDate == null) {
			if (other.birthDate != null)
				return false;
		} else if (!birthDate.equals(other.birthDate))
			return false;
		if (completeName == null) {
			if (other.completeName != null)
				return false;
		} else if (!completeName.equals(other.completeName))
			return false;
		if (emailRecover == null) {
			if (other.emailRecover != null)
				return false;
		} else if (!emailRecover.equals(other.emailRecover))
			return false;
		if (idUser == null) {
			if (other.idUser != null)
				return false;
		} else if (!idUser.equals(other.idUser))
			return false;
		if (registerDate == null) {
			if (other.registerDate != null)
				return false;
		} else if (!registerDate.equals(other.registerDate))
			return false;
		if (stats != other.stats)
			return false;
		return true;
	}

}
