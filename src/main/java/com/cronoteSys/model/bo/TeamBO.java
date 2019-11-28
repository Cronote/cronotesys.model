/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cronoteSys.model.bo;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cronoteSys.model.dao.AuditLogDAO;
import com.cronoteSys.model.dao.ProjectDAO;
import com.cronoteSys.model.dao.TeamDAO;
import com.cronoteSys.model.interfaces.DatabaseLog;
import com.cronoteSys.model.vo.AuditLogVO;
import com.cronoteSys.model.vo.EmailVO;
import com.cronoteSys.model.vo.TeamUser;
import com.cronoteSys.model.vo.TeamVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.model.vo.relation.side.TeamMember;
import com.cronoteSys.util.GsonUtil;
import com.cronoteSys.util.RestUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 *
 * @author bruno
 */
public class TeamBO implements DatabaseLog {
	@Override
	public void saveLog(String operation, Object obj) {
		UserVO user = (UserVO) obj;
		AuditLogVO audit = new AuditLogVO();
		audit.setAction(operation);
		audit.setUser(user);
		audit.setTablename("tb_team");
		audit.setDateTime(LocalDateTime.now());
		new AuditLogDAO().saveOrUpdate(audit);

	}

	public void save(TeamVO team) {

		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.post("saveTeam", team).readEntity(String.class);
			team = (TeamVO) GsonUtil.fromJsonAsStringToObject(json, TeamVO.class);
		} else {
			team = new TeamDAO().saveOrUpdate(team);
		}
		sendEmail(team);
		saveLog("save", team.getOwner());
		notifyAllTeamAddedListeners(team, "added");
	}

	public void update(TeamVO team) {
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.post("saveTeam", team).readEntity(String.class);
			team = (TeamVO) GsonUtil.fromJsonAsStringToObject(json, TeamVO.class);
		} else {
			team = new TeamDAO().saveOrUpdate(team);
		}
		sendEmail(team);
		saveLog("update", team.getOwner());
		notifyAllTeamAddedListeners(team, "updated");
	}

	public void update(TeamVO team, String action, UserVO user) {
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.post("saveTeam", team).readEntity(String.class);
			team = (TeamVO) GsonUtil.fromJsonAsStringToObject(json, TeamVO.class);
		} else {
			team = new TeamDAO().saveOrUpdate(team);
		}
		if (action.equals("leaving")) {
			saveLog("update - leaving", user);
			notifyAllTeamDeletedListeners(team);
		}
	}

	public void delete(TeamVO team, UserVO user) {
		if (RestUtil.isConnectedToTheServer()) {
			RestUtil.delete("deleteTeam", team.getId());
		} else {
			new TeamDAO().delete(team.getId());
		}
		saveLog("delete", user);
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

			List<TeamMember> expiredMembers = new ArrayList<TeamMember>();
			List<TeamUser> expiredUsers = new ArrayList<TeamUser>();
			lst.forEach((t) -> {
				expiredMembers.clear();
				expiredUsers.clear();
				for (TeamMember tmi : t.getMembers()) {
					if (!tmi.isInviteAccepted() && tmi.getExpiresAt() != null) {
						if (tmi.getExpiresAt().isBefore(LocalDateTime.now()))
							expiredMembers.add(tmi);
					}

				}
				for (TeamUser tui : t.getTeamUser()) {
					if (!tui.isInviteAccepted() && tui.getExpiresAt() != null) {
						if (tui.getExpiresAt().isBefore(LocalDateTime.now()))
							expiredUsers.add(tui);
					}

				}
				t.getMembers().removeAll(expiredMembers);
				t.getTeamUser().removeAll(expiredUsers);
			});

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
		if (!team.getMembers().isEmpty()) {
			for (int i = 0; i < team.getMembers().size(); i++) {
				if (!team.getMembers().get(i).isInviteAccepted()) {
					receivers[i] = team.getMembers().get(i).getUser().getLogin().getEmail() + ";"
							+ team.getMembers().get(i).getUser().getIdUser() + ";" + team.getId() + ";"
							+ team.getName();
				}
			}
		}
		EmailVO emailVO = new EmailVO();
		emailVO.setReceiver(receivers);
		emailVO.setMessage("team" + ";teamid=" + team.getId());
		emailVO.setSubject("Convite para time");
		String emailEncoder = URLEncoder.encode(new Gson().toJson(emailVO));
		String rest = RestUtil.get("send_email?receivers=" + emailEncoder).readEntity(String.class);
	}

	public String getMemberIdArrayAsString(String loggedUserId, TeamVO team) {
		String ids = "";
		if (team != null) {
			ids = "(" + team.getOwner().getIdUser();
			if (team.getMembers().isEmpty()) {
				ids += ")";
			} else {
				for (TeamMember tm : team.getMembers()) {
					ids += "," + tm.getUser().getIdUser();
				}
				ids += ")";
			}
		} else {
			ids += "(" + loggedUserId + ")";
		}

		return ids;
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

	public String getTeamName(int id) {
		return new TeamDAO().getTeamName(id);
	}

	public List<TeamVO> searchByUserOwnerOrMember(String search, UserVO loggedUser) {
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil
					.get("getListTeamsBySearchWithUser?userId=" + loggedUser.getIdUser() + "&search=" + search)
					.readEntity(String.class);
			Type teamListType = new TypeToken<List<TeamVO>>() {
			}.getType();
			List<TeamVO> lst = GsonUtil.getGsonWithJavaTime().fromJson(json, teamListType);

			return lst;
		}
		return new TeamDAO().searchWithUserOwnerOrMember(search, loggedUser.getIdUser());
	}

}
