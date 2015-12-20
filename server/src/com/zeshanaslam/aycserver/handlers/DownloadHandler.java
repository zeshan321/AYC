package com.zeshanaslam.aycserver.handlers;

import java.io.IOException;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.zeshanaslam.aycserver.utils.Encryption;
import com.zeshanaslam.aycserver.utils.ServerData;

public class DownloadHandler implements HttpHandler {

	@Override
	public void handle(final HttpExchange httpExchange) throws IOException {
		Map<String, String> params = new ServerData().queryToMap(httpExchange.getRequestURI().getQuery()); 

		final ServerData serverData = new ServerData();
		final Encryption encryption = new Encryption();
		final String username = params.get("user").toLowerCase(), password = params.get("pass"), requestedVideo = params.get("fileid"), year = params.get("year");

		
	}
}
