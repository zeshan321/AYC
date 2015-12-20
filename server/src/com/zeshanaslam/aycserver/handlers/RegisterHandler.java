package com.zeshanaslam.aycserver.handlers;

import java.io.IOException;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.zeshanaslam.aycserver.Main;
import com.zeshanaslam.aycserver.utils.Encryption;
import com.zeshanaslam.aycserver.utils.SQLite;
import com.zeshanaslam.aycserver.utils.ServerData;

public class RegisterHandler implements HttpHandler {

	@Override
	public void handle(final HttpExchange httpExchange) throws IOException {
		Map<String, String> params = new ServerData().queryToMap(httpExchange.getRequestURI().getQuery()); 

		ServerData serverData = new ServerData();
		Encryption encryption = new Encryption();
		SQLite sqlite = Main.sqlite;
		String username = params.get("user").toLowerCase(), password = encryption.encrypt(params.get("pass"));

		if (!sqlite.isRegistered(username)) {
			sqlite.registerUser(username, password, "");
			serverData.writeResponse(httpExchange, serverData.returnData(true, null, "User successfully registered"));
		} else {
			serverData.writeResponse(httpExchange, serverData.returnData(false, "2", "Already registered"));
		}
	}
}
