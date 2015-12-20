package com.zeshanaslam.aycserver.handlers;

import java.io.IOException;
import java.util.Map;

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
			if (sqlite.videoExists(fileid)) {
				sqlite.updateVideoString(name, desc, fileid, section, year, Integer.parseInt(params.get("ID")));
				serverData.writeResponse(httpExchange, serverData.returnData(true, null, "Video successfully updated"));
			} else {
				sqlite.createVideo(name, desc, fileid, section, year);
				serverData.writeResponse(httpExchange, serverData.returnData(true, null, "Video successfully created"));
			}
		} else {
			serverData.writeResponse(httpExchange, serverData.returnData(false, "7", "Not authorized to view this page"));
		}
	}
}
