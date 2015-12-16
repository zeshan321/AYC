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
import com.zeshanaslam.aycserver.utils.ServerData;

public class VideoHandler implements HttpHandler {

	@Override
	public void handle(final HttpExchange httpExchange) throws IOException {
		Map<String, String> params = new ServerData().queryToMap(httpExchange.getRequestURI().getQuery()); 

		final ServerData serverData = new ServerData();

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Videos");
		query.whereEqualTo("section", params.get("section"));
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objectList, ParseException e) {
				if (e == null) {
					if (objectList == null) {
						serverData.writeResponse(httpExchange, serverData.returnData(true, null, "No videos found"));
						return;
					}

					JSONArray jsonArray = new JSONArray();

					for (ParseObject parseObject: objectList) {						
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("name", parseObject.getString("name"));
						jsonObject.put("desc", parseObject.getString("desc"));
						jsonObject.put("fileid", parseObject.getString("fileid"));

						jsonArray.put(jsonObject);
					}

					serverData.writeResponse(httpExchange, serverData.returnData(true, jsonArray));
				} else {
					serverData.writeResponse(httpExchange, serverData.returnData(false, "4", "Failed to retrieve videos"));
				}
			}
		});
	}
}
