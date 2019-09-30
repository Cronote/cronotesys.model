package com.cronoteSys.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {

	public static Gson getGsonWithJavaTime() {
		GsonBuilder gsonBuilder = new GsonBuilder();

		return gsonBuilder.create();
	}


	public static Object fromJsonAsStringToObject(String json,Class<?> c) {
		
		return getGsonWithJavaTime().fromJson(json, c);
	}
}
