package com.zeshanaslam.aycserver.handlers;

import java.io.IOException;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.zeshanaslam.aycserver.Main;
import com.zeshanaslam.aycserver.utils.SQLite;
import com.zeshanaslam.aycserver.utils.ServerData;

public class CreateVideoHandler implements HttpHandler {

	@Override
	public void handle(final HttpExchange httpExchange) throws IOException {
		Map<String, String> params = new ServerData().queryToMap(httpExchange.getRequestURI().getQuery()); 

		ServerData serverData = new ServerData();
		SQLite sqlite = Main.sqlite;
		String key = params.get("key"), name = params.get("title"), desc = params.get("desc"), fileid = params.get("fileid"), section = params.get("section"), year = params.get("year");

		if (key.equals(Main.configLoader.getString("editKey"))) {
			if (params.containsKey("ID")) {
				sqlite.updateVideoString(name, desc, fileid, section, year, Integer.parseInt(params.get("ID")));
				serverData.writeResponse(httpExchange, serverData.returnData(true, null, "Video successfully updated"));
			} else {
				JSONArray jsonArray = new JSONArray();
				jsonArray.put(new JSONObject().put("ID", sqlite.getLastID() + 1));
				
				sqlite.createVideo(name, desc, fileid, section, year);
				serverData.writeResponse(httpExchange, serverData.returnData(true, jsonArray));
			}
		} else {
			serverData.writeResponse(httpExchange, serverData.returnData(false, "7", "Not authorized to view this page"));
		}
	}
}
