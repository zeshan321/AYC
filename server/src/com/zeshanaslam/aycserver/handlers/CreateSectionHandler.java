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

public class CreateSectionHandler implements HttpHandler {

	@Override
	public void handle(final HttpExchange httpExchange) throws IOException {
		Map<String, String> params = new ServerData().queryToMap(httpExchange.getRequestURI().getQuery()); 
		
		final ServerData serverData = new ServerData();
		final String key = params.get("key"), name = params.get("name");
		
		if (key.equals(Main.configLoader.getString("editKey"))) {
			
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Sections");
			query.whereEqualTo("name", name);
			query.findInBackground(new FindCallback<ParseObject>() {
				@Override
				public void done(List<ParseObject> objectList, ParseException e) {
					if (e == null) {
						if (objectList == null) {
							ParseObject userData = new ParseObject("Sections");
							userData.put("name", name);
							userData.saveInBackground(new SaveCallback() {

								@Override
								public void done(ParseException e) {
									if (e == null) {
										serverData.writeResponse(httpExchange, serverData.returnData(true, null, "Section successfully created"));
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
