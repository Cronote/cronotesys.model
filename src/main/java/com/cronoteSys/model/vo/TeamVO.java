package com.cronoteSys.model.vo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

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

import com.cronoteSys.model.vo.relation.side.TeamMember;
import com.cronoteSys.model.vo.view.SimpleUser;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tb_team")
public class TeamVO implements java.io.Serializable {
	private static final long serialVersionUID = -632847161659623870L;

	private Long id;
	private String name;
	private String desc;
	private UserVO owner;
	private String teamColor;
	private transient List<TeamUser> teamUsers = new ArrayList<TeamUser>();

	private List<TeamMember> members = new ArrayList<TeamMember>();

	public TeamVO() {

	}

	public TeamVO(Long id, String name, String desc, UserVO owner, String teamColor) {
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.owner = owner;
		this.teamColor = teamColor;
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

	@OneToMany(mappedBy = "primaryKey.team", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<TeamUser> getTeamUser() {
		return teamUsers;
	}

	public void setTeamUser(List<TeamUser> teamUsers) {
		this.teamUsers = teamUsers;
		for (TeamUser tu : teamUsers) {
			getMembers().removeIf(new Predicate<TeamMember>() {

				@Override
				public boolean test(TeamMember t) {
					return t.getUser().getIdUser().equals(tu.getMember().getIdUser());
				}
			});
			getMembers().add(new TeamMember(tu.getMember(), tu.isInviteAccepted()));
		}

	}

	public void addTeamUser(TeamUser teamUsers) {
		this.teamUsers.add(teamUsers);
	}

	@Transient
	public List<TeamMember> getMembers() {
		return members;
	}

	public void setMembers(List<TeamMember> members) {
		this.members = members;

	}

	public void switchMembersBetweenLists(boolean toSave) {
		if (toSave) {
			getTeamUser().clear();
			for (TeamMember member : getMembers()) {
				TeamUser tu = new TeamUser(this, member.getUser(), member.isInviteAccepted());
				System.out.println(tu.getTeam());
				addTeamUser(tu);
			}
		} else {
			getMembers().clear();
			for (TeamUser tu : getTeamUser()) {
				getMembers().add(new TeamMember(tu.getMember(), tu.isInviteAccepted()));
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
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
		return true;
	}

}
