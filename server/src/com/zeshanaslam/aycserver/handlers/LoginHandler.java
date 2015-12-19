package com.zeshanaslam.aycserver.handlers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;
import org.parse4j.callback.FindCallback;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.zeshanaslam.aycserver.utils.Encryption;
import com.zeshanaslam.aycserver.utils.ServerData;

public class LoginHandler implements HttpHandler {

	@Override
	public void handle(final HttpExchange httpExchange) throws IOException {
		Map<String, String> params = new ServerData().queryToMap(httpExchange.getRequestURI().getQuery()); 

		final ServerData serverData = new ServerData();
		final Encryption encryption = new Encryption();
		final String username = params.get("user").toLowerCase(), password = params.get("pass");

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
		query.whereEqualTo("username", username);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objectList, ParseException e) {
				if (e == null) {
					if (objectList == null) {
						serverData.writeResponse(httpExchange, serverData.returnData(false, "11", "Incorrect login"));
						return;
					}
					
					if (encryption.checkPassword(password, objectList.get(0).getString("password"))) {
						JSONArray jsonArray = new JSONArray();
						JSONObject jsonObject = new JSONObject();
						
						jsonObject.put("admin", objectList.get(0).getBoolean("admin"));
						jsonObject.put("videos", objectList.get(0).getList("videos"));
						
						jsonArray.put(jsonObject);
						
						serverData.writeResponse(httpExchange, serverData.returnData(true, jsonArray));
					} else {
						serverData.writeResponse(httpExchange, serverData.returnData(false, "11", "Incorrect login"));
					}
				} else {
					e.printStackTrace();
				}
			}
		});
	}
}
