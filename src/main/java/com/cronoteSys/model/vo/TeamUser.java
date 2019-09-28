package com.cronoteSys.model.vo;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "teamUser")
@AssociationOverrides({
		@AssociationOverride(name = "primaryKey.member", joinColumns = @JoinColumn(name = "member", referencedColumnName = "id_user")),
		@AssociationOverride(name = "primaryKey.team", joinColumns = @JoinColumn(name = "team", referencedColumnName = "id")) })
public class TeamUser implements Serializable {
	private static final long serialVersionUID = -2309919369757688500L;

	// composite-id key
	private transient TeamUserId primaryKey = new TeamUserId();

	// additional fields
	private boolean inviteAccepted;
	private LocalDateTime expiresAt;

	public TeamUser() {

	}

	public TeamUser(TeamVO team, UserVO member, boolean inviteAccepted, LocalDateTime expiresAt) {
		this.primaryKey.setMember(member);
		this.primaryKey.setTeam(team);
		this.inviteAccepted = inviteAccepted;
		this.expiresAt = expiresAt;
	}

	@EmbeddedId
	public TeamUserId getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(TeamUserId primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Transient
	public UserVO getMember() {
		return getPrimaryKey().getMember();
	}

	public void setMember(UserVO user) {
		getPrimaryKey().setMember(user);
	}

	@Transient
	public TeamVO getTeam() {
		return getPrimaryKey().getTeam();
	}

	public void setTeam(TeamVO group) {
		getPrimaryKey().setTeam(group);
	}

	public boolean isInviteAccepted() {
		return inviteAccepted;
	}

	public void setInviteAccepted(boolean inviteAccepted) {
		this.inviteAccepted = inviteAccepted;
	}

	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (inviteAccepted ? 1231 : 1237);
		result = prime * result + ((primaryKey == null) ? 0 : primaryKey.hashCode());
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
		if (inviteAccepted != other.inviteAccepted)
			return false;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		return true;
	}

}