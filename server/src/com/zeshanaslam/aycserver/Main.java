package com.zeshanaslam.aycserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsServer;
import com.zeshanaslam.aycserver.handlers.RegisterHandler;
import com.zeshanaslam.aycserver.utils.ConfigLoader;
import com.zeshanaslam.aycserver.utils.TLSHandler;
import com.zeshanaslam.aycserver.utils.ServerData;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;

import org.parse4j.Parse;

public class Main {
	
	public static ConfigLoader configLoader;

    public static void main(String[] args) throws Exception {
    	// Initialize Config data
    	configLoader = new ConfigLoader();
    	    	
    	// Initialize parse
    	Parse.initialize(configLoader.getString("parseAPI"), configLoader.getString("parseRestAPI"));

    	// Start TLS Server
        HttpsServer server = HttpsServer.create(new InetSocketAddress(8000), 0);
        server.setHttpsConfigurator(new TLSHandler().createSSLContext());
        server.createContext("/test", new TestHandler());
        server.createContext("/register", new RegisterHandler());
        server.setExecutor(null);
        server.start();
    }


    static class TestHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
        	Map<String, String> params = new ServerData().queryToMap(httpExchange.getRequestURI().getQuery()); 
        	
            String response = "Successfully connected! " + params.get("test");
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}