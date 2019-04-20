package com.cronoteSys.model.bo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cronoteSys.model.dao.ActivityDAO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.StatusEnum;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.ActivityMonitor;

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
		activityVO = acDAO.saveOrUpdate(activityVO);
		notifyAllActivityAddedListeners(activityVO);
		return activityVO;
	}

	public ActivityVO update(ActivityVO activityVO) {
		return acDAO.saveOrUpdate(activityVO);
	}

	public void delete(ActivityVO activityVO) {
		acDAO.delete(activityVO.getId());
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
		return acDAO.saveOrUpdate(ac);
	}

	public ActivityVO switchStatus(ActivityVO ac, StatusEnum stats) {
		ac.setStats(stats);
		if (ac.getRealtime().compareTo(ac.getEstimatedTime()) > 0)
			return breakStatus(ac);
		return acDAO.saveOrUpdate(ac);
	}

	public ActivityVO updateRealTime(ActivityVO ac) {
		Duration realTime = new ExecutionTimeBO().getRealTime(ac);
		ac.setRealtime(realTime);
		if (ac.getRealtime().compareTo(ac.getEstimatedTime()) > 0)
			ac = breakStatus(ac);
		return acDAO.saveOrUpdate(ac);

	}

	public List<ActivityVO> listAllByUser(UserVO user) {
		List<ActivityVO> lst = acDAO.getList(user);
		for (ActivityVO activityVO : lst) {
			if (StatusEnum.inProgress(activityVO.getStats())) {
				ActivityMonitor.addActivity(activityVO);
			}
		}
		return lst;
	}

	private static ArrayList<OnActivityAddedI> activityAddedListeners = new ArrayList<OnActivityAddedI>();

	public interface OnActivityAddedI {
		void onActivityAddedI(ActivityVO act);
	}

	public static void addOnActivityAddedIListener(OnActivityAddedI newListener) {
		activityAddedListeners.add(newListener);
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

	private void notifyAllactivityDeletedListeners(ActivityVO act) {
		for (OnActivityDeletedI l : activityDeletedListeners) {
			l.onActivityDeleted(act);
		}
	}
}
