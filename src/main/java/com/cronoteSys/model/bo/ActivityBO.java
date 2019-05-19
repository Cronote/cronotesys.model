package com.cronoteSys.model.bo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import com.cronoteSys.filter.ActivityFilter;
import com.cronoteSys.model.dao.ActivityDAO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.model.vo.StatusEnum;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.ActivityMonitor;
import com.cronoteSys.util.RestUtil;

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
			activityVO = (ActivityVO) RestUtil.post("saveActivity", ActivityVO.class, activityVO);
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
		if (RestUtil.isConnectedToTheServer())
			return (ActivityVO) RestUtil.post("saveActivity", ActivityVO.class, ac);
		return acDAO.saveOrUpdate(ac);
	}

	public ActivityVO switchStatus(ActivityVO ac, StatusEnum stats) {
		ac.setStats(stats);
		if (ac.getRealtime().compareTo(ac.getEstimatedTime()) > 0)
			return breakStatus(ac);
		if (RestUtil.isConnectedToTheServer())
			return (ActivityVO) RestUtil.post("saveActivity", ActivityVO.class, ac);
		return acDAO.saveOrUpdate(ac);
	}

	public ActivityVO updateRealTime(ActivityVO ac) {
		Duration realTime = new ExecutionTimeBO().getRealTime(ac);
		ac.setRealtime(realTime);
		if (ac.getRealtime().compareTo(ac.getEstimatedTime()) > 0)
			ac = breakStatus(ac);
		if (RestUtil.isConnectedToTheServer())
			return (ActivityVO) RestUtil.post("saveActivity", ActivityVO.class, ac);
		return acDAO.saveOrUpdate(ac);

	}
	public List<ActivityVO> listAll(ActivityFilter filter) {
		if (RestUtil.isConnectedToTheServer()) {
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(RestUtil.host+"getActivityList?filter="+filter);
			return (List<ActivityVO>) target.request().get();
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
