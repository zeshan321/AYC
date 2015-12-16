package com.zeshanaslam.aycserver.handlers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;
import org.parse4j.callback.FindCallback;
import org.parse4j.callback.SaveCallback;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.zeshanaslam.aycserver.Main;
import com.zeshanaslam.aycserver.utils.ServerData;

public class CreateVideoHandler implements HttpHandler {

	@Override
	public void handle(final HttpExchange httpExchange) throws IOException {
		Map<String, String> params = new ServerData().queryToMap(httpExchange.getRequestURI().getQuery()); 
		
		final ServerData serverData = new ServerData();
		final String key = params.get("key"), title = params.get("title"), desc = params.get("desc"), fileid = params.get("fileid"), section = params.get("section");
		
		if (key.equals(Main.configLoader.getString("editKey"))) {
			
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Videos");
			query.whereEqualTo("fileid", fileid);
			query.findInBackground(new FindCallback<ParseObject>() {
				@Override
				public void done(List<ParseObject> objectList, ParseException e) {
					if (e == null) {
						if (objectList == null) {
							ParseObject userData = new ParseObject("Videos");
							userData.put("name", title);
							userData.put("desc", desc);
							userData.put("fileid", fileid);
							userData.put("section", section);
							userData.saveInBackground(new SaveCallback() {

								@Override
								public void done(ParseException e) {
									if (e == null) {
										serverData.writeResponse(httpExchange, serverData.returnData(true, null, "Video successfully created"));
									} else {
										serverData.writeResponse(httpExchange, serverData.returnData(false, "9", "Unable to create"));
									}
								}

							});
							return;
						}
						
						serverData.writeResponse(httpExchange, serverData.returnData(false, "10", "Create already exists"));
					} else {
						e.printStackTrace();
					}
				}
			});
		} else {
			serverData.writeResponse(httpExchange, serverData.returnData(false, "7", "Not authorized to view this page"));
		}
	}
}
