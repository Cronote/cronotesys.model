package com.cronoteSys.util;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RestUtil {
	public static boolean isConnectedToTheServer() {
		String response = response("http://localhost:8081/Test/webapi/myresource/connection").readEntity(String.class);
		if(response.equalsIgnoreCase("success")) {
			return true;
		}else {
			return false;
		}
	}
	
	public static Response response(String link) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(link);
		Response response = target.request(MediaType.APPLICATION_JSON).get();
		return response;
	}
	
	
	public static Object post(String link, Class<?> clazz, Object object) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8081/Test/webapi/myresource/login");
		return target.request().post(Entity.entity(object, MediaType.APPLICATION_JSON),clazz);
	}
}
