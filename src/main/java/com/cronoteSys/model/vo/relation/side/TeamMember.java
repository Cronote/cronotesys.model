package com.cronoteSys.model.vo.relation.side;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import com.cronoteSys.model.interfaces.ThreatingUser;
import com.cronoteSys.model.vo.UserVO;

public class TeamMember implements Serializable,ThreatingUser {

	private static final long serialVersionUID = 3072640654821599318L;

	private UserVO user;
	private boolean inviteAccepted;
	private LocalDateTime expiresAt;

	public TeamMember() {
		super();
	}

	public TeamMember(UserVO user, boolean inviteAccepted,LocalDateTime expiresAt) {
		super();
		this.user = user;
		this.inviteAccepted = inviteAccepted;
		this.expiresAt = expiresAt;
	}

	public UserVO getUser() {
		return user;
	}

	public void setUser(UserVO user) {
		this.user = user;
	}

	public boolean isInviteAccepted() {
		return inviteAccepted;
	}

	public void setInviteAccepted(boolean inviteAccepted) {
		this.inviteAccepted = inviteAccepted;
	}

	@Override
	public int hashCode() {
		return Objects.hash(inviteAccepted, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof TeamMember)) {
			return false;
		}
		TeamMember other = (TeamMember) obj;
		return inviteAccepted == other.inviteAccepted && Objects.equals(user, other.user);
	}

	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}

}
