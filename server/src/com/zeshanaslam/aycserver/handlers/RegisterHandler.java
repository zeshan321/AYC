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
import com.zeshanaslam.aycserver.utils.Encryption;
import com.zeshanaslam.aycserver.utils.ServerData;

public class RegisterHandler implements HttpHandler {

	@Override
	public void handle(final HttpExchange httpExchange) throws IOException {
		Map<String, String> params = new ServerData().queryToMap(httpExchange.getRequestURI().getQuery()); 

		final ServerData serverData = new ServerData();
		final Encryption encryption = new Encryption();
		final String username = params.get("user").toLowerCase(), password = encryption.encrypt(params.get("pass"));

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
		query.whereEqualTo("username", username);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objectList, ParseException e) {
				if (e == null) {
					if (objectList != null && objectList.size() > 0) {
						serverData.writeResponse(httpExchange, serverData.returnData(false, "1", "Username already registered"));
						return;
					}

					ParseObject userData = new ParseObject("Users");
					userData.put("username", username);
					userData.put("password", password);
					userData.saveInBackground(new SaveCallback() {

						@Override
						public void done(ParseException e) {
							if (e == null) {
								serverData.writeResponse(httpExchange, serverData.returnData(true, null, "User successfully registered"));
							} else {
								serverData.writeResponse(httpExchange, serverData.returnData(false, "2", "Registration failed"));
							}
						}

					});
				} else {
					e.printStackTrace();
				}
			}
		});
	}
}
