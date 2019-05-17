import java.util.List;

import org.junit.Test;

import com.cronoteSys.filter.ActivityFilter;
import com.cronoteSys.model.dao.ActivityDAO;
import com.cronoteSys.model.dao.ProjectDAO;
import com.cronoteSys.model.dao.UserDAO;
import com.cronoteSys.model.vo.SimpleActivity;
import com.cronoteSys.model.vo.view.SimpleUser;

import junit.framework.TestCase;

public class AppTest  extends TestCase{

	@Test
	public void test1() {
		ActivityDAO actDAO = new ActivityDAO();
		ActivityFilter f = new ActivityFilter();
		f.setUser(new UserDAO().find(2));
		f.setProject(new ProjectDAO().find(2));
		List<SimpleActivity> actView = actDAO.getSimpleActivitiesView(f);
		for (SimpleActivity sa : actView) {
			System.out.println(sa);
		}
	}
	@Test
	public void test2() {
		UserDAO uDAO = new UserDAO();
		List<SimpleUser> actView = uDAO.listLoggedUsers("1,2","0");
	}
}
