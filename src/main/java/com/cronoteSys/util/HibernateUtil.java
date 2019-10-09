/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cronoteSys.util;

import javax.persistence.EntityManager;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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
		try {
			// Create the SessionFactory from standard (hibernate.cfg.xml)
			// config file.
			sessionFactory = new Configuration().configure("hibernate-remote.cfg.xml").buildSessionFactory();
			entityManager = sessionFactory.createEntityManager();
		} catch (Throwable ex1) {
			// Log the exception.
			System.err.println("Initial SessionFactory creation failed." + ex1);
			try {
				sessionFactory = new Configuration().configure("hibernate-local1.cfg.xml").buildSessionFactory();
				entityManager = sessionFactory.createEntityManager();
			} catch (Exception ex2) {
				System.err.println("Initial SessionFactory creation failed. >>>>>>>>>>> 2" + ex1);
				// TODO: handle exception
			}
		}
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
