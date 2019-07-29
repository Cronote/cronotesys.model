package com.cronoteSys.model.vo;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;
@Embeddable
public class TeamUserId implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1904666241413651799L;
	private  UserVO member;
	private  TeamVO team;

	@ManyToOne(cascade = CascadeType.ALL)
	public UserVO getMember() {
		return member;
	}

	public void setMember(UserVO member) {
		this.member = member;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	public TeamVO getTeam() {
		return team;
	}

	public void setTeam(TeamVO team) {
		this.team = team;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		TeamUserId other = (TeamUserId) obj;
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

	@Override
	public String toString() {
		return "TeamUserId [member=" + member.getIdUser() + ", team=" + team.getId() + "]";
	}


}
