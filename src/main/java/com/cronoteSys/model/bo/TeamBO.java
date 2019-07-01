/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cronoteSys.model.bo;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.List;

import com.cronoteSys.model.dao.TeamDAO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.TeamVO;
import com.cronoteSys.util.GsonUtil;
import com.cronoteSys.util.RestUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 *
 * @author bruno
 */
public class TeamBO {

	public TeamVO save(TeamVO team) {
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.post("saveTeam", team).readEntity(String.class);
			return (TeamVO) GsonUtil.fromJsonAsStringToObject(json, TeamVO.class);
		} else {
			return new TeamDAO().saveOrUpdate(team);
		}
	}

	public void update(TeamVO team) {
		new TeamDAO().saveOrUpdate(team);
	}

	public void delete(TeamVO team) {
		new TeamDAO().delete(team.getId());
	}

	public List<TeamVO> listAll() {
		return new TeamDAO().listAll();
	}

	public List<TeamVO> listByUserOwnerOrMember(int userId) {
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.get("getListTeamsByUser?userId=" + userId).readEntity(String.class);
			Type teamListType = new TypeToken<List<TeamVO>>() {
			}.getType();
			List<TeamVO> lst = GsonUtil.getGsonWithJavaTime().fromJson(json, teamListType);
			return lst;
		}
		return new TeamDAO().listByUserOwnerOrMember(userId);
	}

}
