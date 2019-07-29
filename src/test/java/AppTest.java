import org.junit.Test;

import com.cronoteSys.model.dao.TeamDAO;
import com.cronoteSys.model.dao.UserDAO;
import com.cronoteSys.model.vo.TeamUser;
import com.cronoteSys.model.vo.TeamVO;

import junit.framework.TestCase;

public class AppTest extends TestCase {

	
	@Test
	public void testTeamNewRelationTable() {
		TeamDAO tdao = new TeamDAO();

		TeamVO newTeam = tdao.find(50L);
		TeamUser rel = new TeamUser();
		rel.setTeam(newTeam);
		rel.setMember(new UserDAO().find(2));
//		rel.setActivated(false);
		newTeam.addTeamUser(rel);

		tdao.saveOrUpdate(newTeam);
	}
}
