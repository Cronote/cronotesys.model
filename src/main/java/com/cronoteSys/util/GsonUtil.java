package com.cronoteSys.util;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.cronoteSys.model.vo.TeamUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class GsonUtil {

	public static Gson getGsonWithJavaTime() {
		GsonBuilder gsonBuilder = new GsonBuilder();

		return gsonBuilder.create();
	}


	public static Object fromJsonAsStringToObject(String json,Class<?> c) {
		
		return getGsonWithJavaTime().fromJson(json, c);
	}
}
