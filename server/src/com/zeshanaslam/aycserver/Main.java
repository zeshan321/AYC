package com.zeshanaslam.aycserver;

import com.sun.net.httpserver.HttpsServer;

import com.zeshanaslam.aycserver.handlers.*;
import com.zeshanaslam.aycserver.utils.ConfigLoader;
import com.zeshanaslam.aycserver.utils.TLSHandler;

import java.net.InetSocketAddress;

import org.parse4j.Parse;
	
public class Main {
	
	public static ConfigLoader configLoader;

    public static void main(String[] args) throws Exception {
    	// Initialize config loader
    	configLoader = new ConfigLoader();
    	    	
    	// Initialize parse
    	Parse.initialize(configLoader.getString("parseAPI"), configLoader.getString("parseRestAPI"));

    	// Start TLS Server
        HttpsServer server = HttpsServer.create(new InetSocketAddress(8000), 0);
        server.setHttpsConfigurator(new TLSHandler().createSSLContext());
        
        // Creates user accounts
        server.createContext("/register", new RegisterHandler());
        
        // Grabs section names from DB
        server.createContext("/section", new SectionHandler());
        
        // Grabs video info from DB
        server.createContext("/video", new VideoHandler());
        
        // Gives file download
        server.createContext("/download", new DownloadHandler());
        
        server.setExecutor(null);
        server.start();
    }
}