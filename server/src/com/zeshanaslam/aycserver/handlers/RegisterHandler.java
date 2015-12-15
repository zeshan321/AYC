package com.zeshanaslam.aycserver.handlers;

import java.io.IOException;
import java.util.Map;

import org.parse4j.ParseException;
import org.parse4j.ParseObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.zeshanaslam.aycserver.utils.Encryption;
import com.zeshanaslam.aycserver.utils.ServerData;

public class RegisterHandler implements HttpHandler {

	@Override
	public void handle(final HttpExchange httpExchange) throws IOException {
		final ServerData serverData = new ServerData();
		Map<String, String> params = new ServerData().queryToMap(httpExchange.getRequestURI().getQuery()); 
		
		String username = params.get("user"), password = params.get("pass");
		
		Encryption encryption = new Encryption();
		System.out.println(encryption.encrypt(password));
		System.out.println(encryption.decrypt(encryption.encrypt(password)));
	}
}
