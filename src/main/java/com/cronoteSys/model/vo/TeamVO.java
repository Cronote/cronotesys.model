package com.cronoteSys.model.vo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "tb_team")
@XmlRootElement
public class TeamVO implements java.io.Serializable {
	private static final long serialVersionUID = -632847161659623870L;

	private Long id;
	private String name;
	private String desc;
	private UserVO owner;
	private List<UserVO> members;

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

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "tbrel_team_user", joinColumns = @JoinColumn(referencedColumnName = "id", name = "id_team"), inverseJoinColumns = @JoinColumn(referencedColumnName = "id_user", name = "id_member"))
	public List<UserVO> getMembers() {
		return members;
	}

	public void setMembers(List<UserVO> members) {
		this.members = members;
	}
}
