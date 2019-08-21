/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cronoteSys.model.bo;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.cronoteSys.model.bo.ActivityBO.OnActivityAddedI;
import com.cronoteSys.model.bo.ActivityBO.OnActivityDeletedI;
import com.cronoteSys.model.dao.GenericsDAO;
import com.cronoteSys.model.dao.ProjectDAO;
import com.cronoteSys.model.dao.TeamDAO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.EmailVO;
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

	public void save(TeamVO team) {

		if (RestUtil.isConnectedToTheServer()) {
			sendEmail(team);
			String json = RestUtil.post("saveTeam", team).readEntity(String.class);
			team = (TeamVO) GsonUtil.fromJsonAsStringToObject(json, TeamVO.class);
		} else {
			team = new TeamDAO().saveOrUpdate(team);
		}
		notifyAllTeamAddedListeners(team, "added");
	}

	public void update(TeamVO team) {
		if (RestUtil.isConnectedToTheServer()) {
			sendEmail(team);
			String json = RestUtil.post("saveTeam", team).readEntity(String.class);
			team = (TeamVO) GsonUtil.fromJsonAsStringToObject(json, TeamVO.class);
		} else {
			team = new TeamDAO().saveOrUpdate(team);
		}
		notifyAllTeamAddedListeners(team, "updated");
	}

	public void update(TeamVO team, String action) {
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.post("saveTeam", team).readEntity(String.class);
			team = (TeamVO) GsonUtil.fromJsonAsStringToObject(json, TeamVO.class);
		} else {
			team = new TeamDAO().saveOrUpdate(team);
		}
		if (action.equals("leaving"))
			notifyAllTeamDeletedListeners(team);
	}

	public void delete(TeamVO team) {
		if (RestUtil.isConnectedToTheServer()) {
			RestUtil.delete("deleteTeam", team.getId());
		} else {
			new TeamDAO().delete(team.getId());
		}
		notifyAllTeamDeletedListeners(team);
	}

	public List<TeamVO> listAll() {
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.get("listAllTeam").readEntity(String.class);
			Type teamListType = new TypeToken<List<TeamVO>>() {
			}.getType();
			List<TeamVO> lst = GsonUtil.getGsonWithJavaTime().fromJson(json, teamListType);
			return lst;
		}
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

	public long countProjects(TeamVO t) {
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.get("countProjectByTeam?teamId=" + t.getId()).readEntity(String.class);
			return Long.valueOf(json);
		}
		return new ProjectDAO().countByTeam(t.getId());
	}
	
	private void sendEmail(TeamVO team) {
		String[] receivers = new String[team.getMembers().size()];
		if(!team.getMembers().isEmpty()) {
			for (int i = 0; i < team.getMembers().size(); i++) {
				if(!team.getMembers().get(i).isInviteAccepted()) {
					receivers[i] = team.getMembers().get(i).getUser().getLogin().getEmail()+";"+team.getMembers().get(i).getUser().getIdUser()+";"+team.getId();
				}
			}
		}
		EmailVO emailVO = new EmailVO();
		emailVO.setReceiver(receivers);
		emailVO.setMessage("team"+";teamid="+team.getId());
		emailVO.setSubject("Convite para time");
		String emailEncoder = URLEncoder.encode(new Gson().toJson(emailVO));
		String rest = RestUtil.get("send_email?receivers="+emailEncoder).readEntity(String.class);
	}

	private static ArrayList<OnTeamAddedI> teamAddedListeners = new ArrayList<OnTeamAddedI>();

	public interface OnTeamAddedI {
		void onTeamAddedI(TeamVO team, String action);
	}

	public static void addOnTeamAddedIListener(OnTeamAddedI newListener) {
		teamAddedListeners.add(newListener);
	}

	public static void removeOnTeamAddedIListener(OnTeamAddedI newListener) {
		teamAddedListeners.remove(newListener);
	}

	private void notifyAllTeamAddedListeners(TeamVO team, String action) {
		for (OnTeamAddedI l : teamAddedListeners) {
			l.onTeamAddedI(team, action);
		}
	}

	private static ArrayList<OnTeamDeletedI> teamDeletedListeners = new ArrayList<OnTeamDeletedI>();

	public interface OnTeamDeletedI {
		void onTeamDeleted(TeamVO team);
	}

	public static void addOnTeamDeletedListener(OnTeamDeletedI newListener) {
		teamDeletedListeners.add(newListener);
	}

	public static void removeOnTeamDeletedListener(OnTeamDeletedI newListener) {
		teamDeletedListeners.remove(newListener);
	}

	private void notifyAllTeamDeletedListeners(TeamVO team) {
		for (OnTeamDeletedI l : teamDeletedListeners) {
			l.onTeamDeleted(team);
		}
	}
	
	

}
