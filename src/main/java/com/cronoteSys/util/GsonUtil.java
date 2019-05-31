package com.cronoteSys.util;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class GsonUtil {

	public static Gson getGsonWithJavaTime() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
			@Override
			public LocalDateTime deserialize(JsonElement json, Type type,
					JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
				return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString());
			}
		});
		gsonBuilder.registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
			@Override
			public LocalDate deserialize(JsonElement json, Type type,
					JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
				return LocalDate.parse(json.getAsJsonPrimitive().getAsString());
			}
		});
		gsonBuilder.registerTypeAdapter(Duration.class, new JsonDeserializer<Duration>() {
			@Override
			public Duration deserialize(JsonElement json, Type type,
					JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
				return Duration.parse(json.getAsJsonPrimitive().getAsString());
			}
		});

		return gsonBuilder.create();
	}


	public static Object fromJsonAsStringToObject(String json,Class<?> c) {
		
		return getGsonWithJavaTime().fromJson(json, c);
	}
}
