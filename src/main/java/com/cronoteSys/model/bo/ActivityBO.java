package com.cronoteSys.model.bo;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import com.cronoteSys.filter.ActivityFilter;
import com.cronoteSys.model.dao.ActivityDAO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.StatusEnum;
import com.cronoteSys.util.GsonUtil;
import com.cronoteSys.util.RestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class ActivityBO {
	ActivityDAO acDAO;

	public ActivityBO() {
		acDAO = new ActivityDAO();
	}

	public ActivityVO save(ActivityVO activityVO) {
		if (activityVO.getId() == null) {
			activityVO.setStats(StatusEnum.NOT_STARTED);
		}
		activityVO.setRealtime(Duration.ZERO);
		activityVO.setLastModification(LocalDateTime.now());
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.post("saveActivity", activityVO).readEntity(String.class);
			return (ActivityVO) GsonUtil.fromJsonAsStringToObject(json, ActivityVO.class);
		} else {
			activityVO = acDAO.saveOrUpdate(activityVO);
		}
		notifyAllActivityAddedListeners(activityVO);
		return activityVO;
	}

	public ActivityVO update(ActivityVO activityVO) {
		return acDAO.saveOrUpdate(activityVO);
	}

	public void delete(ActivityVO activityVO) {
		if (RestUtil.isConnectedToTheServer()) {
			RestUtil.delete("deleteActivity", activityVO.getId());
			notifyAllactivityDeletedListeners(activityVO);
		} else {
			acDAO.delete(activityVO.getId());
		}
		notifyAllactivityDeletedListeners(activityVO);
	}

	public ActivityVO breakStatus(ActivityVO ac) {
		StatusEnum stats = ac.getStats();
		switch (stats) {
		case NORMAL_IN_PROGRESS:
			stats = StatusEnum.BROKEN_IN_PROGRESS;
			break;
		case NORMAL_PAUSED:
			stats = StatusEnum.BROKEN_PAUSED;
			break;
		case NORMAL_FINALIZED:
			stats = StatusEnum.BROKEN_FINALIZED;
			break;
		default:
			break;

		}
		ac.setStats(stats);
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.post("saveActivity", ac).readEntity(String.class);
			return (ActivityVO) GsonUtil.fromJsonAsStringToObject(json, ActivityVO.class);
		}
		return acDAO.saveOrUpdate(ac);
	}

	public ActivityVO switchStatus(ActivityVO ac, StatusEnum stats) {
		ac.setStats(stats);
		if (ac.getRealtime().compareTo(ac.getEstimatedTime()) > 0)
			return breakStatus(ac);
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.post("saveActivity", ac).readEntity(String.class);
			return (ActivityVO) GsonUtil.fromJsonAsStringToObject(json, ActivityVO.class);
		}
		return acDAO.saveOrUpdate(ac);
	}

	public ActivityVO updateRealTime(ActivityVO ac) {
		Duration realTime = new ExecutionTimeBO().getRealTime(ac);
		System.out.println(realTime);
		ac.setRealtime(realTime);
		if (ac.getRealtime().compareTo(ac.getEstimatedTime()) > 0)
			ac = breakStatus(ac);
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.post("saveActivity", ac).readEntity(String.class);
			System.out.println(json);
			return (ActivityVO) GsonUtil.fromJsonAsStringToObject(json, ActivityVO.class);
		}
		return acDAO.saveOrUpdate(ac);

	}

	public List<ActivityVO> listAll(ActivityFilter filter) {
		if (RestUtil.isConnectedToTheServer()) {
			String filterJsonEncoded = URLEncoder.encode(new Gson().toJson(filter));
			String json = RestUtil.get("getActivityList?filter=" + filterJsonEncoded).readEntity(String.class);
			Type activityListType = new TypeToken<List<ActivityVO>>() {
			}.getType();
			List<ActivityVO> lst = GsonUtil.getGsonWithJavaTime().fromJson(json, activityListType);
			return lst;
		}
		return acDAO.getFiltredList(filter);
	}

	private static ArrayList<OnActivityAddedI> activityAddedListeners = new ArrayList<OnActivityAddedI>();

	public interface OnActivityAddedI {
		void onActivityAddedI(ActivityVO act);
	}

	public static void addOnActivityAddedIListener(OnActivityAddedI newListener) {
		activityAddedListeners.add(newListener);
	}

	public static void removeOnActivityAddedIListener(OnActivityAddedI newListener) {
		activityAddedListeners.remove(newListener);
	}

	private void notifyAllActivityAddedListeners(ActivityVO act) {
		for (OnActivityAddedI l : activityAddedListeners) {
			l.onActivityAddedI(act);
		}
	}

	private static ArrayList<OnActivityDeletedI> activityDeletedListeners = new ArrayList<OnActivityDeletedI>();

	public interface OnActivityDeletedI {
		void onActivityDeleted(ActivityVO act);
	}

	public static void addOnActivityDeletedListener(OnActivityDeletedI newListener) {
		activityDeletedListeners.add(newListener);
	}

	public static void removeOnActivityDeletedListener(OnActivityDeletedI newListener) {
		activityDeletedListeners.remove(newListener);
	}

	private void notifyAllactivityDeletedListeners(ActivityVO act) {
		for (OnActivityDeletedI l : activityDeletedListeners) {
			l.onActivityDeleted(act);
		}
	}
}
