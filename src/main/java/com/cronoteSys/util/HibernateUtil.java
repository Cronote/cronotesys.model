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

	public static SessionFactory getSessionFactory() {
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
				e.printStackTrace();
			}
		}
		return sessionFactory;
	}

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
				e.printStackTrace();
			}
		}
//		try {
//			// Create the SessionFactory from standard (hibernate.cfg.xml)
//			// config file.
//			sessionFactory = new Configuration().configure("hibernate-remote.cfg.xml").buildSessionFactory();
//			entityManager = sessionFactory.createEntityManager();
//		} catch (Throwable ex1) {
//			// Log the exception.
//			System.err.println("Initial SessionFactory creation failed." + ex1);
//			try {
//				sessionFactory = new Configuration().configure("hibernate-local1.cfg.xml").buildSessionFactory();
//				entityManager = sessionFactory.createEntityManager();
//			} catch (Exception ex2) {
//				System.err.println("Initial SessionFactory creation failed. >>>>>>>>>>> 2" + ex1);
//				// TODO: handle exception
//			}
//		}
	}

	public static EntityManager getEntityManager() {
		if (entityManager == null) {
			entityManager = sessionFactory.createEntityManager();
		}
		return entityManager; // Prove a parte de persistência
	}
//
//	private static SessionFactory buildSessionFactory() {
//		try {
//			// Create the SessionFactory from hibernate.cfg.xml
//			Configuration configuration = new Configuration();
//			configuration.configure("hibernate.cfg.xml");
//			String s = System.getenv("DATABASE_URL");
//			System.out.println(s);
//
//			URI dbUri = new URI(s);
//
//			String username = dbUri.getUserInfo().split(":")[0];
//			String password = dbUri.getUserInfo().split(":")[1];
//			String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
//			System.out.println(dbUrl);
//			configuration.setProperty("hibernate.connection.username", username);
//			configuration.setProperty("hibernate.connection.password", password);
//			configuration.setProperty("hibernate.connection.url", dbUrl);
////			configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/MYAPP");
////			configuration.setProperty("hbm2ddl.auto", "update");
//			configuration.setProperty("hibernate.hbm2ddl.auto", "update");
//			System.out.println("Hibernate Annotation Configuration loaded");
//
//			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
//					.applySettings(configuration.getProperties()).build();
//			System.out.println("Hibernate Annotation serviceRegistry created");
//
//			SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
//
//			return sessionFactory;
//		} catch (Throwable ex) {
//			// Make sure you log the exception, as it might be swallowed
//			System.err.println("Initial SessionFactory creation failed." + ex);
//			throw new ExceptionInInitializerError(ex);
//		}
//	}
}
