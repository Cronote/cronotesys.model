package com.cronoteSys.model.bo;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import com.cronoteSys.filter.ActivityFilter;
import com.cronoteSys.model.dao.ActivityDAO;
import com.cronoteSys.model.dao.AuditLogDAO;
import com.cronoteSys.model.interfaces.DatabaseLog;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.AuditLogVO;
import com.cronoteSys.model.vo.CategoryVO;
import com.cronoteSys.model.vo.StatusEnum;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.GsonUtil;
import com.cronoteSys.util.RestUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ActivityBO implements DatabaseLog {
	ActivityDAO acDAO;

	@Override
	public void saveLog(String operation, Object obj) {
		AuditLogVO audit = new AuditLogVO();
		audit.setAction(operation);
		audit.setDateTime(LocalDateTime.now());
		audit.setTablename("tb_activity");
		audit.setUser((UserVO) obj);
		new AuditLogDAO().saveOrUpdate(audit);
	}

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
		saveLog("Insert", activityVO.getUserVO());
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
		saveLog("update", activityVO.getUserVO());
		return activityVO;
	}

	public void delete(ActivityVO activityVO) {
		if (RestUtil.isConnectedToTheServer()) {
			RestUtil.delete("deleteActivity", activityVO.getId());
			notifyAllactivityDeletedListeners(activityVO);
		} else {
			acDAO.delete(activityVO.getId());
		}

		saveLog("delete", activityVO.getUserVO());
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
			ac = (ActivityVO) GsonUtil.fromJsonAsStringToObject(json, ActivityVO.class);
		} else {
			ac = acDAO.saveOrUpdate(ac);
		}
		saveLog("update - status breaked", ac.getUserVO());
		return ac;
	}

	public ActivityVO switchStatus(ActivityVO ac, StatusEnum stats, UserVO executor) {
		if (StatusEnum.inProgress(ac.getStats()) && !StatusEnum.inProgress(stats))
			new ExecutionTimeBO().finishExecution(ac, executor);
		ac.setStats(stats);
		if (ac.getRealtime().compareTo(ac.getEstimatedTime()) > 0)
			return breakStatus(ac);

		ActivityVO act = null;
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.post("saveActivity", ac).readEntity(String.class);
			act = (ActivityVO) GsonUtil.fromJsonAsStringToObject(json, ActivityVO.class);
		} else {
			act = acDAO.saveOrUpdate(ac);
		}
		saveLog("update - status changed", ac.getUserVO());
		return act;
	}

	public ActivityVO updateRealTime(ActivityVO ac) {
		Duration realTime = new ExecutionTimeBO().getRealTime(ac);
		ac.setRealtime(realTime);
		if (ac.getRealtime().compareTo(ac.getEstimatedTime()) > 0)
			ac = breakStatus(ac);
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.post("saveActivity", ac).readEntity(String.class);
			ac = (ActivityVO) GsonUtil.fromJsonAsStringToObject(json, ActivityVO.class);
		} else {
			ac = acDAO.saveOrUpdate(ac);
		}
		saveLog("update - realtime", ac.getUserVO());
		return ac;
	}

	public List<ActivityVO> listAll(ActivityFilter filter) {
		List<ActivityVO> lst = null;
		if (RestUtil.isConnectedToTheServer()) {
			String filterJsonEncoded = URLEncoder.encode(new Gson().toJson(filter));
			String json = RestUtil.get("getActivityList?filter=" + filterJsonEncoded).readEntity(String.class);
			Type activityListType = new TypeToken<List<ActivityVO>>() {
			}.getType();
			lst = GsonUtil.getGsonWithJavaTime().fromJson(json, activityListType);
		} else {
			lst = acDAO.getFiltredList(filter);
		}
		lst = orderList(lst);
		return lst;
	}

	private List<ActivityVO> orderList(List<ActivityVO> lst) {
		// finalizados
		lst.sort(new Comparator<ActivityVO>() {
			@Override
			public int compare(ActivityVO a1, ActivityVO a2) {
				if (StatusEnum.itsFinalized(a2.getStats()) && !StatusEnum.itsFinalized(a1.getStats())) {
					return 1;
				} else if (StatusEnum.itsFinalized(a1.getStats()) && !StatusEnum.itsFinalized(a2.getStats())) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		// n iniciados
		lst.sort(new Comparator<ActivityVO>() {
			@Override
			public int compare(ActivityVO a1, ActivityVO a2) {
				if (a2.getStats() == StatusEnum.NOT_STARTED && a1.getStats() != StatusEnum.NOT_STARTED) {
					return 1;
				} else if (a1.getStats() == StatusEnum.NOT_STARTED && a2.getStats() != StatusEnum.NOT_STARTED) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		// pausado
		lst.sort(new Comparator<ActivityVO>() {
			@Override
			public int compare(ActivityVO a1, ActivityVO a2) {
				if (StatusEnum.itsPaused(a2.getStats()) && !StatusEnum.itsPaused(a1.getStats())) {
					return 1;
				} else if (StatusEnum.itsPaused(a1.getStats()) && !StatusEnum.itsPaused(a2.getStats())) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		// progresso
		lst.sort(new Comparator<ActivityVO>() {
			@Override
			public int compare(ActivityVO a1, ActivityVO a2) {
				if (StatusEnum.inProgress(a2.getStats()) && !StatusEnum.inProgress(a1.getStats())) {
					return 1;
				} else if (StatusEnum.inProgress(a1.getStats()) && !StatusEnum.inProgress(a2.getStats())) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		// Mais proximo de estourar
		lst.sort(new Comparator<ActivityVO>() {
			@Override
			public int compare(ActivityVO a1, ActivityVO a2) {
				if ((StatusEnum.inProgress(a2.getStats()) || StatusEnum.itsPaused(a2.getStats()))
						&& (StatusEnum.inProgress(a1.getStats()) || StatusEnum.itsPaused(a1.getStats()))) {
					Duration d1 = a1.getEstimatedTime().minus(a1.getRealtime());
					Duration d2 = a2.getEstimatedTime().minus(a2.getRealtime());

					return d1.compareTo(d2);
				} else {
					return 0;
				}
			}
		});
		return lst;
	}

	public List<ActivityVO> listAllToBeDependency(ActivityFilter filter) {
		List<ActivityVO> lst = new ArrayList<ActivityVO>();
		if (RestUtil.isConnectedToTheServer()) {
			String filterJsonEncoded = URLEncoder.encode(new Gson().toJson(filter));
			String json = RestUtil.get("getActivityList?filter=" + filterJsonEncoded).readEntity(String.class);
			Type activityListType = new TypeToken<List<ActivityVO>>() {
			}.getType();
			lst = GsonUtil.getGsonWithJavaTime().fromJson(json, activityListType);
		} else {
			lst = acDAO.getFiltredList(filter);
		}
		// Remove done
		lst.removeIf(new Predicate<ActivityVO>() {
			@Override
			public boolean test(ActivityVO t) {
				return StatusEnum.itsFinalized(t.getStats());
			}
		});
		// Remove activities that will cause a dependency cycle
		if (filter.getActivity() != null) {
			List<ActivityVO> lstToRemove = new ArrayList<ActivityVO>();
			ActivityVO ac = null;
			for (ActivityVO a : lst) {
				if (a.getId() == filter.getActivity()) {
					ac = a;
				}
			}
			for (ActivityVO a : lst) {
				if (a.getDependencies().contains(ac)) {
					lstToRemove.add(a);
					continue;
				}
				for (ActivityVO a2 : a.getDependencies()) {
					if (a2.getDependencies().contains(ac)) {
						lstToRemove.add(a);
					}
				}
			}
			lst.removeAll(lstToRemove);
		}

		return lst;
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

	public Duration timeSuggestionFor(Integer user,Integer priority, CategoryVO category) {
		ActivityFilter filter = new ActivityFilter(null, null, user, category.getId(), priority);
		List<ActivityVO> lstMatchedItems = listAll(filter);
		if (lstMatchedItems.size() >= 3) {
			Duration d = Duration.ZERO;
			for (ActivityVO act : lstMatchedItems) {
				d = d.plus(act.getRealtime());

			}

			d = d.dividedBy(lstMatchedItems.size());
			return d;
		}

		return null;
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
