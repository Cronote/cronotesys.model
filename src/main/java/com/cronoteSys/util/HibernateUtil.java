/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cronoteSys.util;

import java.util.Properties;

import javax.persistence.EntityManager;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.CategoryVO;
import com.cronoteSys.model.vo.ExecutionTimeVO;
import com.cronoteSys.model.vo.LoginVO;
import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.model.vo.TeamUser;
import com.cronoteSys.model.vo.TeamVO;
import com.cronoteSys.model.vo.UserVO;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author bruno
 */
public class HibernateUtil {

	private static EntityManager entityManager = null;

	private static SessionFactory sessionFactory;


	static {
		if (sessionFactory == null) {
			try {
				Configuration configuration = new Configuration();
				// Hibernate settings equivalent to hibernate.cfg.xml's properties
				Properties settings = new Properties();
				settings.put(Environment.DRIVER, "org.postgresql.Driver");
				settings.put(Environment.URL,
						"jdbc:postgresql://ec2-107-20-251-130.compute-1.amazonaws.com:5432/df7gvg8l8kbuaf");
				settings.put(Environment.USER, "icowfbullsqzij");
				settings.put(Environment.PASS, "31befc28665481a9be4b2f8eb4a3cc7c92f552fd19986c420203fbef25d6ffe3");
				settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
				settings.put(Environment.SHOW_SQL, "true");
				settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
				settings.put(Environment.HBM2DDL_AUTO, "update");

				configuration.setProperties(settings);
				configuration.addAnnotatedClass(LoginVO.class);
				configuration.addAnnotatedClass(UserVO.class);
				configuration.addAnnotatedClass(TeamVO.class);
				configuration.addAnnotatedClass(CategoryVO.class);
				configuration.addAnnotatedClass(ProjectVO.class);
				configuration.addAnnotatedClass(ActivityVO.class);
				configuration.addAnnotatedClass(ExecutionTimeVO.class);
				configuration.addAnnotatedClass(TeamUser.class);

				ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
						.applySettings(configuration.getProperties()).build();

				sessionFactory = configuration.buildSessionFactory(serviceRegistry);
			} catch (Exception e) {
				System.out.println("Deu erro nessa porra");
				e.printStackTrace();
			}
		}
	}

	public static EntityManager getEntityManager() {
		if (entityManager == null) {
			entityManager = sessionFactory.createEntityManager();
		}
		return entityManager; // Prove a parte de persistência
	}
}
