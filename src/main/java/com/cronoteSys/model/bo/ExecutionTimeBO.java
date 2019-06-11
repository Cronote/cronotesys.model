package com.cronoteSys.model.bo;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import com.cronoteSys.model.dao.ExecutionTimeDAO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.ExecutionTimeVO;
import com.cronoteSys.util.GsonUtil;
import com.cronoteSys.util.RestUtil;
import com.google.gson.reflect.TypeToken;

public class ExecutionTimeBO {

	private ExecutionTimeDAO execDAO;

	public ExecutionTimeBO() {
		execDAO = new ExecutionTimeDAO();
	}

	public void delete(ExecutionTimeVO executionTimeVO) {
		execDAO.delete(executionTimeVO.getId());
	}

	public ExecutionTimeVO startExecution(ActivityVO ac) {
		if (execDAO.executionInProgressByUser(ac.getUserVO()) == 0) {
			ExecutionTimeVO exec = new ExecutionTimeVO();
			exec.setActivityVO(ac);
			exec.setStartDate(LocalDateTime.now());
			if (RestUtil.isConnectedToTheServer()) {
				String json = RestUtil.post("saveExecutionTime", exec).readEntity(String.class);
				return (ExecutionTimeVO) GsonUtil.fromJsonAsStringToObject(json, ExecutionTimeVO.class);
			}
			return execDAO.saveOrUpdate(exec);
		} else {
			System.out.println("Atividades simult‚neas n„o permitido");
			return null;
			// TODO: devolver mecanismos para avisar que o usuario n√£o pode executar
			// atividades simult√¢neas
		}
	}

	public ExecutionTimeVO finishExecution(ActivityVO ac) {
		ExecutionTimeVO executionTimeVO = new ExecutionTimeVO();
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.get("executionInProgress?activityID=" + ac.getId()).readEntity(String.class);
			executionTimeVO = (ExecutionTimeVO) GsonUtil.fromJsonAsStringToObject(json, ExecutionTimeVO.class);
		} else {
			executionTimeVO = execDAO.executionInProgress(ac.getId());
		}
		executionTimeVO.setFinishDate(LocalDateTime.now());
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.post("saveExecutionTime", executionTimeVO).readEntity(String.class);
			return (ExecutionTimeVO) GsonUtil.fromJsonAsStringToObject(json, ExecutionTimeVO.class);
		}
		return execDAO.saveOrUpdate(executionTimeVO);
	}

	public Duration getRealTime(ActivityVO act) {
		List<ExecutionTimeVO> lst = new ArrayList<ExecutionTimeVO>();
		if (RestUtil.isConnectedToTheServer()) {
			String json = RestUtil.get("listExecutionTimeByActivity?activityID=" + act.getId()).readEntity(String.class);
			Type executionTimeListType = new TypeToken<List<ExecutionTimeVO>>() {
			}.getType();
			System.out.println(json);
			lst = GsonUtil.getGsonWithJavaTime().fromJson(json, executionTimeListType);
			System.out.println(lst.size());
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
		System.out.println(sum);
		return sum;
	}

	public List<ExecutionTimeVO> listAll() {
		return execDAO.getList();
	}
}
