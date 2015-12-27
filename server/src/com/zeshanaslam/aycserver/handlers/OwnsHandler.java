package com.zeshanaslam.aycserver.handlers;

import java.io.IOException;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.zeshanaslam.aycserver.Main;
import com.zeshanaslam.aycserver.objects.LoginObject;
import com.zeshanaslam.aycserver.utils.Encryption;
import com.zeshanaslam.aycserver.utils.SQLite;
import com.zeshanaslam.aycserver.utils.ServerData;

public class OwnsHandler  implements HttpHandler {

	@Override
	public void handle(final HttpExchange httpExchange) throws IOException {
		Map<String, String> params = new ServerData().queryToMap(httpExchange.getRequestURI().getQuery()); 

		ServerData serverData = new ServerData();
		Encryption encryption = new Encryption();
		SQLite sqlite = Main.sqlite;
		String key = params.get("key"), username = params.get("user").toLowerCase(), password = params.get("pass"), year = params.get("year");

		if (key.equals(Main.configLoader.getString("editKey"))) {
			LoginObject loginObject = sqlite.getLoginData(username);
			
			if (encryption.checkPassword(password, loginObject.password)) {
				sqlite.updateYears(loginObject.videos + ", " + year, username);
				serverData.writeResponse(httpExchange, serverData.returnData(true, null, "Updated owned years successfully"));
			} else {
				serverData.writeResponse(httpExchange, serverData.returnData(false, "11", "Incorrect login"));
			}
		} else {
			serverData.writeResponse(httpExchange, serverData.returnData(false, "7", "Not authorized to view this page"));
		}
	}
}
