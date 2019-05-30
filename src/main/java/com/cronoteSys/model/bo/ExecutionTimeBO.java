package com.cronoteSys.model.bo;

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
import com.cronoteSys.util.RestUtil;

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
			if(RestUtil.isConnectedToTheServer()) {
				return (ExecutionTimeVO) RestUtil.post("saveExecutionTime", ExecutionTimeVO.class, exec);
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
		if(RestUtil.isConnectedToTheServer()) {
//			executionTimeVO = RestUtil.get("executionInProgress").readEntity(ExecutionTimeVO.class);
			executionTimeVO = null;
		}else {
			executionTimeVO = execDAO.executionInProgress(ac);
		}
		executionTimeVO.setFinishDate(LocalDateTime.now());
		if(RestUtil.isConnectedToTheServer()) {
			return (ExecutionTimeVO) RestUtil.post("saveExecutionTime", ExecutionTimeVO.class, executionTimeVO);
		}
		return execDAO.saveOrUpdate(executionTimeVO);
	}

	public Duration getRealTime(ActivityVO act) {
		List<ExecutionTimeVO> lst = new ArrayList<ExecutionTimeVO>();
		if(RestUtil.isConnectedToTheServer()){
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(RestUtil.host+"listExecutionTimeByActivity?activity="+act);
//			lst =  (List<ExecutionTimeVO>) target.request().get();
			lst =  null;
		}else {
			lst = execDAO.listByActivity(act);
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
