package com.zeshanaslam.aycserver.handlers;

import java.io.IOException;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.zeshanaslam.aycserver.Main;
import com.zeshanaslam.aycserver.objects.LoginObject;
import com.zeshanaslam.aycserver.utils.Encryption;
import com.zeshanaslam.aycserver.utils.SQLite;
import com.zeshanaslam.aycserver.utils.ServerData;

public class LoginHandler implements HttpHandler {

	@Override
	public void handle(final HttpExchange httpExchange) throws IOException {
		Map<String, String> params = new ServerData().queryToMap(httpExchange.getRequestURI().getQuery()); 

		ServerData serverData = new ServerData();
		Encryption encryption = new Encryption();
		SQLite sqlite = Main.sqlite;
		String username = params.get("user").toLowerCase(), password = params.get("pass");

		LoginObject loginObject = sqlite.getLoginData(username);
		
		if (loginObject == null) {
			serverData.writeResponse(httpExchange, serverData.returnData(false, "11", "Incorrect login"));
			return;
		}
		
		if (encryption.checkPassword(password, loginObject.password)) {
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject = new JSONObject();
			
			jsonObject.put("admin", loginObject.admin);
			jsonObject.put("videos", loginObject.videos);
			
			jsonArray.put(jsonObject);
			
			serverData.writeResponse(httpExchange, serverData.returnData(true, jsonArray));
		} else {
			serverData.writeResponse(httpExchange, serverData.returnData(false, "11", "Incorrect login"));
		}
	}
}
