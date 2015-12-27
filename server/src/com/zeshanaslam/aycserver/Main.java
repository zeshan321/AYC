package com.zeshanaslam.aycserver;

import com.sun.net.httpserver.HttpsServer;

import com.zeshanaslam.aycserver.handlers.*;
import com.zeshanaslam.aycserver.utils.ConfigLoader;
import com.zeshanaslam.aycserver.utils.SQLite;
import com.zeshanaslam.aycserver.utils.TLSHandler;

import java.net.InetSocketAddress;

public class Main {

	public static ConfigLoader configLoader;
	public static SQLite sqlite;

	public static void main(String[] args) throws Exception {
		// Initialize config loader
		configLoader = new ConfigLoader();
		
		// Start DB connection
		sqlite = new SQLite();
		
		// Start TLS Server
		HttpsServer server = HttpsServer.create(new InetSocketAddress(8000), 0);
		server.setHttpsConfigurator(new TLSHandler().createTLSContext());

		// Creates user accounts
		server.createContext("/register", new RegisterHandler());
		
		// Grabs years from DB
		server.createContext("/year", new YearHandler());

		// Grabs section names from DB
		server.createContext("/section", new SectionHandler());

		// Grabs video info from DB
		server.createContext("/video", new VideoHandler());

		// Gives file download
		server.createContext("/download", new DownloadHandler());
		
		// Creates sections
		server.createContext("/createsection", new CreateSectionHandler());
		
		// Creates videos
		server.createContext("/createvideo", new CreateVideoHandler());
		
		// Creates year
		server.createContext("/createyear", new CreateYearHandler());
		
		// Creates videos
		server.createContext("/login", new LoginHandler());
		
		// Deletes sections, videos and years
		server.createContext("/delete", new DeleteHandler());
		
		// Adds years to users
		server.createContext("/owns", new OwnsHandler());

		server.setExecutor(null);
		server.start();
		
		System.out.println("AYCServer is running!");
	}
}