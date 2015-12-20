package com.zeshanaslam.aycserver.handlers;

import java.io.IOException;

import org.json.JSONArray;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.zeshanaslam.aycserver.Main;
import com.zeshanaslam.aycserver.utils.SQLite;
import com.zeshanaslam.aycserver.utils.ServerData;

public class YearHandler implements HttpHandler {

	@Override
	public void handle(final HttpExchange httpExchange) throws IOException {
		ServerData serverData = new ServerData();
		SQLite sqlite = Main.sqlite;

		JSONArray jsonArray = new JSONArray();
		
		for (String sections: sqlite.getYears()) {
			jsonArray.put(sections);
		}
		
		serverData.writeResponse(httpExchange, serverData.returnData(true, jsonArray));
	}
}
