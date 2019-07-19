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

import javax.persistence.Transient;

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
			activityVO = (ActivityVO) GsonUtil.fromJsonAsStringToObject(json, ActivityVO.class);
		} else {
			activityVO = acDAO.saveOrUpdate(activityVO);
		}
		notifyAllActivityAddedListeners(activityVO, "save");
		return activityVO;
	}

	public ActivityVO update(ActivityVO activityVO) {
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.post("saveActivity", activityVO).readEntity(String.class);
			activityVO = (ActivityVO) GsonUtil.fromJsonAsStringToObject(json, ActivityVO.class);
		} else {
			activityVO = acDAO.saveOrUpdate(activityVO);
		}
		notifyAllActivityAddedListeners(activityVO, "update");
		return activityVO;
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
		ac.setRealtime(realTime);
		if (ac.getRealtime().compareTo(ac.getEstimatedTime()) > 0)
			ac = breakStatus(ac);
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.post("saveActivity", ac).readEntity(String.class);
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

	public Object[] timeToComplete(List<ActivityVO> lstActivities, Duration limitDuration) {
		Duration sum = Duration.ZERO;

		List<ActivityVO> countedDependencies = new ArrayList<ActivityVO>();
		boolean continueWhile = true;
		boolean activitiesBlowLimitDuration = true;
		while (continueWhile) {
			for (ActivityVO act : lstActivities) {
				/*
				 * We need to copy the dependencies to a temp list because we clean the list to
				 * know if we already counted all dependencies of a specific activity, and then
				 * we count it
				 */
				List<ActivityVO> dependenciesCopy = new ArrayList<ActivityVO>();
				for (ActivityVO a : act.getDependencies()) {
					ActivityVO aCopy = new ActivityVO(a);
					dependenciesCopy.add(aCopy);
				}
				dependenciesCopy.removeAll(countedDependencies);
				if (!countedDependencies.contains(act)) {
					if (dependenciesCopy.isEmpty()) {
						if (sum.plus(act.getEstimatedTime()).compareTo(limitDuration.plusSeconds(1)) > -1) {
							if (!activitiesBlowLimitDuration)
								continueWhile = false;
							activitiesBlowLimitDuration = false;
						} else {
							sum = sum.plus(act.getEstimatedTime());
							countedDependencies.add(act);
						}
					} else {
						continue;
					}
				}

			}
			if (countedDependencies.size() == lstActivities.size()) {
				continueWhile = false;
			}
		}
		Object[] ob = { sum, countedDependencies.size() };
		return ob;

	}

	private static ArrayList<OnActivityAddedI> activityAddedListeners = new ArrayList<OnActivityAddedI>();

	public interface OnActivityAddedI {
		void onActivityAddedI(ActivityVO act, String action);
	}

	public static void addOnActivityAddedIListener(OnActivityAddedI newListener) {
		activityAddedListeners.add(newListener);
	}

	public static void removeOnActivityAddedIListener(OnActivityAddedI newListener) {
		activityAddedListeners.remove(newListener);
	}

	private void notifyAllActivityAddedListeners(ActivityVO act, String action) {
		for (OnActivityAddedI l : activityAddedListeners) {
			l.onActivityAddedI(act, action);
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
