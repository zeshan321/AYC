package com.zeshanaslam.aycserver.handlers;

import java.io.IOException;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.zeshanaslam.aycserver.Main;
import com.zeshanaslam.aycserver.objects.VideoObject;
import com.zeshanaslam.aycserver.utils.SQLite;
import com.zeshanaslam.aycserver.utils.ServerData;

public class VideoHandler implements HttpHandler {

	@Override
	public void handle(final HttpExchange httpExchange) throws IOException {
		Map<String, String> params = new ServerData().queryToMap(httpExchange.getRequestURI().getQuery()); 

		ServerData serverData = new ServerData();
		SQLite sqlite = Main.sqlite;
		
		JSONArray jsonArray = new JSONArray();
		for (VideoObject videoObject: sqlite.getVideos(params.get("year"), params.get("section"))) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name", videoObject.name);
			jsonObject.put("desc", videoObject.desc);
			jsonObject.put("fileid", videoObject.fileid);
			jsonObject.put("ID", videoObject.ID);

			jsonArray.put(jsonObject);
		}
		
		serverData.writeResponse(httpExchange, serverData.returnData(true, jsonArray));
	}
}
