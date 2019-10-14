package com.cronoteSys.util;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import com.cronoteSys.model.bo.ActivityBO;
import com.cronoteSys.model.vo.ActivityVO;

import javafx.application.Platform;

public class ActivityMonitor {

	private static List<ActivityVO> lstActivity = new ArrayList<ActivityVO>();

	private static Timer timer = new Timer(1000*60, new ActionListener() {

		public void actionPerformed(java.awt.event.ActionEvent e) {
			Platform.runLater(new Runnable() {
				public void run() {
					ActivityBO actBo = new ActivityBO();
					for (ActivityVO activity : lstActivity) {
						activity = actBo.updateRealTime(activity);
						new ActivityMonitor().notifyAllOnMonitorTickListeners(activity);

					}
				}
			});
		}
	});

	public static void addActivity(ActivityVO act) {
		boolean contains = false;
		for (ActivityVO activity : lstActivity) {
			contains = (activity.getId() == act.getId());

		}
		if (!contains)
			lstActivity.add(act);
		timer.setInitialDelay(0);
		timer.restart();
	}

	public static void removeActivity(ActivityVO activityToRemove) {
		ActivityVO activity_temp = activityToRemove;
		for (ActivityVO activity : lstActivity) {
			if (activity.getId() == activityToRemove.getId()) {
				activity_temp = activity;
			}
		}
		lstActivity.remove(activity_temp);
		new ActivityMonitor().notifyAllOnMonitorTickListeners(activityToRemove);
	}

	public static void stopMonitor() {
		timer.stop();
	}

	private static ArrayList<OnMonitorTick> listeners = new ArrayList<OnMonitorTick>();

	public interface OnMonitorTick {
		void onMonitorTicked(ActivityVO act);
	}

	public static void addOnMonitorTickListener(OnMonitorTick newListener) {
		listeners.add(newListener);
	}

	private void notifyAllOnMonitorTickListeners(ActivityVO act) {
		for (OnMonitorTick l : listeners) {
			l.onMonitorTicked(act);
		}
	}

}
