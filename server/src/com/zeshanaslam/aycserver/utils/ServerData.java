package com.zeshanaslam.aycserver.utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;


public class ServerData {

	public void writeResponse(HttpExchange t, String response) {
		try {
			t.sendResponseHeaders(200, response.length());

			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.flush();
			os.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeFile(HttpExchange t, File response) {
		try {
			t.sendResponseHeaders(200, response.length());

			OutputStream os = t.getResponseBody();
			os.write(Files.readAllBytes(response.toPath()));
			os.flush();
			os.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Map<String, String> queryToMap(String query){
		Map<String, String> result = new HashMap<String, String>();
		for (String param : query.split("&")) {
			String pair[] = param.split("=");
			if (pair.length > 1) {
				result.put(pair[0], pair[1]);
			} else {
				result.put(pair[0], "");
			}
		}
		return result;
	}

	public String returnData(boolean status, String errorCode, String detail) {
		JSONObject jsonObject = null;
		String data = null;

		try {
			jsonObject = new JSONObject();
			jsonObject.put("succeed", status);
			if (!status) {
				jsonObject.put("code", errorCode);
			}
			jsonObject.put("info", detail);

			data = jsonObject.toString(2);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return data;
	}

	public String returnData(boolean status, JSONArray array) {
		JSONObject jsonObject = null;
		String data = null;

		try {
			jsonObject = new JSONObject();
			jsonObject.put("succeed", status);
			jsonObject.put("info", array);

			data = jsonObject.toString(2);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return data;
	}
}
