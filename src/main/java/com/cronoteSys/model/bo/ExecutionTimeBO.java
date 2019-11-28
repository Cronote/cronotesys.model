package com.cronoteSys.model.bo;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cronoteSys.model.dao.AuditLogDAO;
import com.cronoteSys.model.dao.ExecutionTimeDAO;
import com.cronoteSys.model.interfaces.DatabaseLog;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.AuditLogVO;
import com.cronoteSys.model.vo.ExecutionTimeVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.GsonUtil;
import com.cronoteSys.util.RestUtil;
import com.google.gson.reflect.TypeToken;

public class ExecutionTimeBO implements DatabaseLog {

	private ExecutionTimeDAO execDAO;

	@Override
	public void saveLog(String operation, Object obj) {
		AuditLogVO audit = new AuditLogVO();
		audit.setTablename("tb_execution_Time");
		audit.setAction(operation);
		audit.setDateTime(LocalDateTime.now());
		audit.setUser((UserVO) obj);
		new AuditLogDAO().saveOrUpdate(audit);
	}

	public ExecutionTimeBO() {
		execDAO = new ExecutionTimeDAO();
	}

	public void delete(ExecutionTimeVO executionTimeVO) {
		execDAO.delete(executionTimeVO.getId());
	}

	public ExecutionTimeVO startExecution(ActivityVO ac, UserVO executor) {
		if (execDAO.executionInProgressByUser(executor) == 0) {
			ExecutionTimeVO exec = new ExecutionTimeVO();
			exec.setActivityVO(ac);
			exec.setStartDate(LocalDateTime.now());
			if (RestUtil.isConnectedToTheServer()) {
				String json = RestUtil.post("saveExecutionTime", exec).readEntity(String.class);
				exec = (ExecutionTimeVO) GsonUtil.fromJsonAsStringToObject(json, ExecutionTimeVO.class);
			} else {
				exec = execDAO.saveOrUpdate(exec);
			}
			saveLog("Start execution, of activity: " + ac.getId() + " from status: " + ac.getStats().getDescription(),
					executor);
			return exec;
		} else {
			return null;
		}
	}

	public ExecutionTimeVO finishExecution(ActivityVO ac, UserVO executor) {
		ExecutionTimeVO executionTimeVO = new ExecutionTimeVO();
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.get("executionInProgress?activityID=" + ac.getId()).readEntity(String.class);
			executionTimeVO = (ExecutionTimeVO) GsonUtil.fromJsonAsStringToObject(json, ExecutionTimeVO.class);
		} else {
			executionTimeVO = execDAO.executionInProgress(ac.getId());
		}
		if (executionTimeVO != null) {
			executionTimeVO.setFinishDate(LocalDateTime.now());
			ExecutionTimeVO exec = null;
			if (RestUtil.isConnectedToTheServer()) {
				String json = RestUtil.post("saveExecutionTime", executionTimeVO).readEntity(String.class);
				exec = (ExecutionTimeVO) GsonUtil.fromJsonAsStringToObject(json, ExecutionTimeVO.class);
			} else {
				exec = execDAO.saveOrUpdate(executionTimeVO);
			}
			saveLog("Finishing execution, of activity: " + ac.getId() + " from status: " + ac.getStats().getDescription(),
					executor);
			return exec;
		} else {
			return null;
		}
	}

	public Duration getRealTime(ActivityVO act) {
		List<ExecutionTimeVO> lst = new ArrayList<ExecutionTimeVO>();
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.get("listExecutionTimeByActivity?activityID=" + act.getId())
					.readEntity(String.class);
			Type executionTimeListType = new TypeToken<List<ExecutionTimeVO>>() {
			}.getType();
			lst = GsonUtil.getGsonWithJavaTime().fromJson(json, executionTimeListType);
		} else {
			lst = execDAO.listByActivity(act.getId());
		}
		Duration sum = Duration.ZERO;
		if (lst.size() == 0)
			return sum;
		ExecutionTimeVO execInProgress = null;
		for (ExecutionTimeVO execution : lst) {
			if (execution.getFinishDate() == null) {
				execInProgress = execution;
				continue;
			}
			Duration d = Duration.between(execution.getStartDate(), execution.getFinishDate());
			sum = sum.plus(d);
		}
		if (execInProgress != null) {
			sum = sum.plus(Duration.between(execInProgress.getStartDate(), LocalDateTime.now()));
		}
		return sum;
	}

	public List<ExecutionTimeVO> listAll() {
		return execDAO.getList();
	}
}
