package com.zeshanaslam.aycserver.handlers;

import java.io.IOException;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.zeshanaslam.aycserver.Main;
import com.zeshanaslam.aycserver.objects.SectionObject;
import com.zeshanaslam.aycserver.utils.SQLite;
import com.zeshanaslam.aycserver.utils.ServerData;

public class SectionHandler implements HttpHandler {

	@Override
	public void handle(final HttpExchange httpExchange) throws IOException {
		Map<String, String> params = new ServerData().queryToMap(httpExchange.getRequestURI().getQuery()); 

		ServerData serverData = new ServerData();
		SQLite sqlite = Main.sqlite;

		JSONArray jsonArray = new JSONArray();
		
		for (SectionObject sectionObject: sqlite.getSections(params.get("year"))) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name", sectionObject.name);
			jsonObject.put("ID", sectionObject.ID);
			
			jsonArray.put(jsonObject);
		}
		
		serverData.writeResponse(httpExchange, serverData.returnData(true, jsonArray));
	}
}
