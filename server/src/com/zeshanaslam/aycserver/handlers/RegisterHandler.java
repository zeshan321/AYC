package com.zeshanaslam.aycserver.handlers;

import java.io.IOException;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.zeshanaslam.aycserver.Main;
import com.zeshanaslam.aycserver.utils.HashPassword;
import com.zeshanaslam.aycserver.utils.SQLite;
import com.zeshanaslam.aycserver.utils.ServerData;

public class RegisterHandler implements HttpHandler {

	@Override
	public void handle(final HttpExchange httpExchange) throws IOException {
		Map<String, String> params = new ServerData().queryToMap(httpExchange.getRequestURI().getQuery()); 

		ServerData serverData = new ServerData();
		HashPassword hash = new HashPassword();
		SQLite sqlite = Main.sqlite;
		
		String username = params.get("user").toLowerCase(), email = params.get("email"), password = params.get("pass");
		
		if (password.length() > 19) {
			serverData.writeResponse(httpExchange, serverData.returnData(false, "12", "Password length"));
			return;
		}
		
		password = hash.hashPassword(params.get("pass"));

		if (!sqlite.isRegistered(username)) {
			sqlite.registerUser(username, email, password, Main.configLoader.getString("defaultYear"));
			serverData.writeResponse(httpExchange, serverData.returnData(true, null, "User successfully registered"));
		} else {
			serverData.writeResponse(httpExchange, serverData.returnData(false, "2", "Already registered"));
		}
	}
}
