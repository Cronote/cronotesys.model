import org.hibernate.Hibernate;
import org.junit.Test;

import com.cronoteSys.model.bo.LoginBO;
import com.cronoteSys.util.HibernateUtil;

import junit.framework.TestCase;

public class AppTest extends TestCase {

	@Test
	public void testHibernate() {
		LoginBO login = new LoginBO();
		assertEquals(true, login.loginExists("bruno.car.ambrosio@gmail.com")!=null);
	}
}
