package com.zeshanaslam.aycserver.handlers;

import java.io.IOException;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.zeshanaslam.aycserver.Main;
import com.zeshanaslam.aycserver.utils.SQLite;
import com.zeshanaslam.aycserver.utils.ServerData;

public class CreateYearHandler implements HttpHandler {

	@Override
	public void handle(final HttpExchange httpExchange) throws IOException {
		Map<String, String> params = new ServerData().queryToMap(httpExchange.getRequestURI().getQuery()); 

		ServerData serverData = new ServerData();
		SQLite sqlite = Main.sqlite;
		String key = params.get("key"), year = params.get("year");

		if (key.equals(Main.configLoader.getString("editKey"))) {
			if (sqlite.getSize(year).size() == 0) {
				sqlite.createYear(year);
				serverData.writeResponse(httpExchange, serverData.returnData(true, null, "Year successfully created"));
			} else {
				serverData.writeResponse(httpExchange, serverData.returnData(true, null, "Year already exists"));
			}
		} else {
			serverData.writeResponse(httpExchange, serverData.returnData(false, "7", "Not authorized to view this page"));
		}
	}
}
