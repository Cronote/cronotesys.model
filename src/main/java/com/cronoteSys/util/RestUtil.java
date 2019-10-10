package com.cronoteSys.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RestUtil {
//	public static String HOST = "http://localhost:8080/myresource/";
	public static String HOST = "https://cronote-api.herokuapp.com/myresource/";
	

	public static boolean isConnectedToTheServer() {
		boolean connectionOK = false;
		HttpURLConnection connection = null;
		try {
			URL u = new URL(HOST);
			connection = (HttpURLConnection) u.openConnection();
			connection.setRequestMethod("HEAD");
			connectionOK = connection.getResponseCode() == 200;
			// You can determine on HTTP return code received. 200 is success.
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return connectionOK;
	}

	public static Response get(String link) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(HOST + link);
		Response response = target.request(MediaType.APPLICATION_JSON).get();
		return response;
	}

	public static Response post(String link, Object object) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(HOST + link);
		return target.request().post(Entity.entity(object, MediaType.APPLICATION_JSON));
	}

	public static Object put(String link, Class<?> clazz, Object object) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(HOST + link);
		return target.request().put(Entity.entity(object, MediaType.APPLICATION_JSON), clazz);
	}

	public static Object delete(String link, int id) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(HOST + link + "?id=" + id);
		return target.request().delete();
	}

	public static Object delete(String link, long id) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(HOST + link + "?id=" + id);
		return target.request().delete();
	}
}
