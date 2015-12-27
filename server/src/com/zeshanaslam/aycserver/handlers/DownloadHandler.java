package com.zeshanaslam.aycserver.handlers;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.zeshanaslam.aycserver.Main;
import com.zeshanaslam.aycserver.objects.LoginObject;
import com.zeshanaslam.aycserver.utils.Encryption;
import com.zeshanaslam.aycserver.utils.SQLite;
import com.zeshanaslam.aycserver.utils.ServerData;

public class DownloadHandler implements HttpHandler {

	@Override
	public void handle(final HttpExchange httpExchange) throws IOException {
		Map<String, String> params = new ServerData().queryToMap(httpExchange.getRequestURI().getQuery()); 

		ServerData serverData = new ServerData();
		Encryption encryption = new Encryption();
		SQLite sqlite = Main.sqlite;
		
		String username = params.get("user").toLowerCase(), password = params.get("pass"), ID = params.get("ID");

		LoginObject loginObject = sqlite.getLoginData(username);
		
		if (encryption.checkPassword(password, loginObject.password)) {
			List<String> yearsList = Arrays.asList(loginObject.videos.split(", "));
			
			if (loginObject.admin || yearsList.contains(sqlite.getVideoByID(Integer.parseInt(ID)))) {
				File folder = new File(Main.configLoader.getString("filePath"));
				File[] listOfFiles = folder.listFiles();

				for (File file : listOfFiles) {
				    if (file.isFile()) {
				        if (stripExtension(file.getName()).equals(ID)) {
							serverData.writeFile(httpExchange, file);
							return;
				        }
				    }
				}
				
				serverData.writeResponse(httpExchange, serverData.returnData(false, "7", "Not authorized to view this page"));
			} else {
				serverData.writeResponse(httpExchange, serverData.returnData(false, "7", "Not authorized to view this page"));
			}
		} else {
			serverData.writeResponse(httpExchange, serverData.returnData(false, "11", "Incorrect login"));
		}
	}
	
	public String stripExtension (String str) {
        if (str == null) return null;
        
        int pos = str.lastIndexOf(".");
        
        if (pos == -1) return str;

        return str.substring(0, pos);
    }
}
