package com.zeshanaslam.aycserver.handlers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;
import org.parse4j.callback.FindCallback;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.zeshanaslam.aycserver.Main;
import com.zeshanaslam.aycserver.utils.Encryption;
import com.zeshanaslam.aycserver.utils.ServerData;

public class DownloadHandler implements HttpHandler {

	@Override
	public void handle(final HttpExchange httpExchange) throws IOException {
		Map<String, String> params = new ServerData().queryToMap(httpExchange.getRequestURI().getQuery()); 

		final ServerData serverData = new ServerData();
		final Encryption encryption = new Encryption();
		final String username = params.get("user").toLowerCase(), password = params.get("pass");
		final String requestedVideo = params.get("fileid");
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
		query.whereEqualTo("username", username);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objectList, ParseException e) {
				if (e == null) {
					if (objectList == null) {
						serverData.writeResponse(httpExchange, serverData.returnData(false, "5", "User not found"));
						return;
					}

					if (encryption.checkPassword(password, objectList.get(0).getString("password"))) {
						List<String> videoList = objectList.get(0).getList("videos");
						if (videoList != null && videoList.contains(requestedVideo)) {
							// Add support for file types
							serverData.writeFile(httpExchange, new File(Main.configLoader.getString("filePath") + requestedVideo));
						} else {
							serverData.writeResponse(httpExchange, serverData.returnData(false, "7", "Not authorized to download"));
						}
					} else {
						serverData.writeResponse(httpExchange, serverData.returnData(false, "6", "Invalid password"));
					}
				} else {
					e.printStackTrace();
				}
			}
		});
	}
}
