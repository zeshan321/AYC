package com.zeshanaslam.aycserver.handlers;

import java.io.IOException;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.zeshanaslam.aycserver.Main;
import com.zeshanaslam.aycserver.objects.SectionObject;
import com.zeshanaslam.aycserver.objects.VideoObject;
import com.zeshanaslam.aycserver.utils.SQLite;
import com.zeshanaslam.aycserver.utils.ServerData;

public class DeleteHandler  implements HttpHandler {

	@Override
	public void handle(final HttpExchange httpExchange) throws IOException {
		Map<String, String> params = new ServerData().queryToMap(httpExchange.getRequestURI().getQuery()); 

		ServerData serverData = new ServerData();
		SQLite sqlite = Main.sqlite;

		String key = params.get("key"), type = params.get("type");
		
		if (key.equals(Main.configLoader.getString("editKey"))) {
			switch(type) {
			case "video":
				int ID = Integer.parseInt(params.get("ID"));
				
				sqlite.deleteVideo(ID);
				
				serverData.writeResponse(httpExchange, "Deleted video successfully");
			break;
			case "section":
				String section = params.get("section");
				
				sqlite.deleteSection(section);
				
				for (VideoObject videoObject: sqlite.getVideos(params.get("year"), section)) {
					sqlite.deleteVideo(videoObject.ID);
				}
				
				serverData.writeResponse(httpExchange, "Deleted section successfully");
			break;
			case "year":
				String year = params.get("year");
				
				sqlite.deleteYear(year);
				
				for (SectionObject sectionObject: sqlite.getSections(year)) {
					for (VideoObject videoObject: sqlite.getVideos(year, sectionObject.name)) {
						sqlite.deleteVideo(videoObject.ID);
					}
					
					sqlite.deleteSection(sectionObject.name);
				}
				
				serverData.writeResponse(httpExchange, "Deleted year successfully");
			break;
			}
		} else {
			serverData.writeResponse(httpExchange, serverData.returnData(false, "7", "Not authorized to view this page"));
		}
	}
}
