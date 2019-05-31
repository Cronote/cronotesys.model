package com.cronoteSys.util;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RestUtil {
	public static String host = "http://localhost:8080/myresource/";
//	public static String host = "https://cronote-api.herokuapp.com/myresource/";

	public static boolean isConnectedToTheServer() {
		try {
			String response = get("connection").readEntity(String.class);
			if (response.contains("SUCCESS")) {
//				System.out.println("Online");
				return true;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Response get(String link) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(host + link);
		Response response = target.request(MediaType.APPLICATION_JSON).get();
		return response;
	}

	public static Response post(String link, Object object) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(host + link);
		return target.request().post(Entity.entity(object, MediaType.APPLICATION_JSON));
	}

	public static Object put(String link, Class<?> clazz, Object object) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(host + link);
		return target.request().put(Entity.entity(object, MediaType.APPLICATION_JSON), clazz);
	}

	public static Object delete(String link, int id) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(host + link + "?id=" + id);
		return target.request().delete();
	}
}
