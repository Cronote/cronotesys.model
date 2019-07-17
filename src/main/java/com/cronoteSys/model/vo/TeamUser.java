package com.cronoteSys.model.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="team_user")
@XmlRootElement
public class TeamUser implements Serializable {

	private static final long serialVersionUID = 4872406733377838510L;

	private Long id;
	private UserVO member;
	private TeamVO team;

	// additional fields
//	private boolean inviteAccepted;
//	private LocalDateTime expiration;

	@Id
	@GeneratedValue
	@Column(name="id_team_member")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "member")	
	public UserVO getMember() {
		return member;
	}

	public void setMember(UserVO member) {
		this.member = member;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "team")	
	public TeamVO getTeam() {
		return team;
	}

	public void setTeam(TeamVO team) {
		this.team = team;
	}

//	public boolean isInviteAccepted() {
//		return inviteAccepted;
//	}
//
//	public void setInviteAccepted(boolean inviteAccepted) {
//		this.inviteAccepted = inviteAccepted;
//	}
	
//	@Column(name = "expiration")
//	public LocalDateTime getExpiration() {
//		return expiration;
//	}
//
//	public void setExpiration(LocalDateTime expiration) {
//		this.expiration = expiration;
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
//		result = prime * result + ((expiration == null) ? 0 : expiration.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
//		result = prime * result + (inviteAccepted ? 1231 : 1237);
		result = prime * result + ((member == null) ? 0 : member.hashCode());
		result = prime * result + ((team == null) ? 0 : team.hashCode());
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
		TeamUser other = (TeamUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		
		if (member == null) {
			if (other.member != null)
				return false;
		} else if (!member.equals(other.member))
			return false;
		if (team == null) {
			if (other.team != null)
				return false;
		} else if (!team.equals(other.team))
			return false;
		return true;
	}

	
	
}
