package com.cronoteSys.model.vo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.xml.bind.annotation.XmlRootElement;

import com.cronoteSys.model.vo.view.SimpleUser;


@Entity
@Table(name = "tb_team")
@XmlRootElement
public class TeamVO implements java.io.Serializable {
	private static final long serialVersionUID = -632847161659623870L;

	private Long id;
	private String name;
	private String desc;
	private UserVO owner;
	private String teamColor;
	private List<UserVO> members;
	@Transient
	private List<SimpleUser> membersSimpleUser;

	private List<TeamUser> teamUsers = new ArrayList<TeamUser>();
	
	public TeamVO() {

	}

	public TeamVO(Long id, String name, String desc, UserVO owner, String teamColor, List<UserVO> members) {
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.owner = owner;
		this.teamColor = teamColor;
		this.members = members;
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "team_id_team_seq")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_user", referencedColumnName = "id_user")
	public UserVO getOwner() {
		return owner;
	}

	public void setOwner(UserVO owner) {
		this.owner = owner;
	}

	@Column(nullable = false)
	public String getTeamColor() {
		return teamColor;
	}

	public void setTeamColor(String teamColor) {
		this.teamColor = teamColor;
	}

//	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	@JoinTable(name = "tbrel_team_user", joinColumns = @JoinColumn(referencedColumnName = "id", name = "id_team"), inverseJoinColumns = @JoinColumn(referencedColumnName = "id_user", name = "id_member"))
//	public List<UserVO> getMembers() {
//		return members;
//	}
//
//	public void setMembers(List<UserVO> members) {
//		this.members = members;
//	}
	
	@OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
	public List<TeamUser> getTeamUsers() {
		return teamUsers;
	}

	public void setTeamUsers(List<TeamUser> teamUsers) {
		this.teamUsers = teamUsers;
	}
	
	public void addTeamUser(TeamUser teamUsers) {
		this.teamUsers.add(teamUsers);
	}

	
	@Transient
	public List<SimpleUser> getMembersSimpleUser() {
		return membersSimpleUser;
	}

	public void setMembersSimpleUser(List<SimpleUser> membersSimpleUser) {
		this.membersSimpleUser = membersSimpleUser;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((members == null) ? 0 : members.hashCode());
		result = prime * result + ((membersSimpleUser == null) ? 0 : membersSimpleUser.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((teamColor == null) ? 0 : teamColor.hashCode());
		result = prime * result + ((teamUsers == null) ? 0 : teamUsers.hashCode());
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
		TeamVO other = (TeamVO) obj;
		if (desc == null) {
			if (other.desc != null)
				return false;
		} else if (!desc.equals(other.desc))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (members == null) {
			if (other.members != null)
				return false;
		} else if (!members.equals(other.members))
			return false;
		if (membersSimpleUser == null) {
			if (other.membersSimpleUser != null)
				return false;
		} else if (!membersSimpleUser.equals(other.membersSimpleUser))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (teamColor == null) {
			if (other.teamColor != null)
				return false;
		} else if (!teamColor.equals(other.teamColor))
			return false;
		if (teamUsers == null) {
			if (other.teamUsers != null)
				return false;
		} else if (!teamUsers.equals(other.teamUsers))
			return false;
		return true;
	}

	
}
