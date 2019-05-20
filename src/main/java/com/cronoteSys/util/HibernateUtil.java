/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cronoteSys.util;

import java.net.URI;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author bruno
 */
public class HibernateUtil {

	public static EntityManagerFactory factory = null;
	private static EntityManager entityManager = null;

	static {
		getEntityManager();
	}

	private static void init() {
		try {
			if (factory == null) {
//				factory = buildSessionFactory().createEntityManager();

			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static EntityManager getEntityManager() {
		if (entityManager == null) {
			entityManager = buildSessionFactory().createEntityManager();;
		}
		return entityManager; // Prove a parte de persistência
	}

	private static SessionFactory buildSessionFactory() {
		try {
			// Create the SessionFactory from hibernate.cfg.xml
			Configuration configuration = new Configuration();
			configuration.configure("hibernate.cfg.xml");

			configuration.setProperty("hibernate.connection.url", System.getenv("DATABASE_URL"));

			URI dbUri = new URI(System.getenv("DATABASE_URL"));

			String username = dbUri.getUserInfo().split(":")[0];
			String password = dbUri.getUserInfo().split(":")[1];
			String dbUrl = "jdbc:postgresql://" 
			+ dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
			configuration.setProperty("hibernate.connection.username", username);
			configuration.setProperty("hibernate.connection.password", password);
			configuration.setProperty("hibernate.connection.url", dbUrl);
			System.out.println("Hibernate Annotation Configuration loaded");

			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties()).build();
			System.out.println("Hibernate Annotation serviceRegistry created");

			SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

			return sessionFactory;
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
}
