package com.zeshanaslam.aycserver.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfigLoader {

	String fileContent;
	File file = new File("config.json");
	Map<String, String> configValues = new HashMap<>();

	public ConfigLoader(){
		try {
			fileContent = FileUtils.readFileToString(file, "utf-8");

			JSONObject jsonObject = new JSONObject(fileContent);
			JSONArray jsonArray = jsonObject.names();

			for (int i = 0; i < jsonArray.length(); i++) {
				String key = jsonArray.getString(i);

				configValues.put(key, jsonObject.getString(key));
			}

		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
	}

	public String getString(String key) {
		return configValues.get(key);
	}

	public int getInt(String key) {
		return Integer.parseInt(configValues.get(key));
	}

	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(configValues.get(key));
	}

	public boolean contains(String key) {
		return configValues.containsKey(key);
	}
}