package com.zeshanaslam.aycserver.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import com.sun.net.httpserver.HttpsConfigurator;
import com.zeshanaslam.aycserver.Main;

public class TLSHandler {

	public HttpsConfigurator createTLSContext() throws NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, KeyStoreException, KeyManagementException, UnrecoverableKeyException {
		ConfigLoader configLoader = Main.configLoader;

		char[] ksPassword = configLoader.getString("ksPassword").toCharArray();
		char[] ctPassword = configLoader.getString("ctPassword").toCharArray();

		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(new FileInputStream("keystore.jks"), ksPassword);

		KeyManagerFactory keyManager = KeyManagerFactory.getInstance("SunX509");
		keyManager.init(keyStore, ctPassword);

		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(keyManager.getKeyManagers(), null, null);

		return new HttpsConfigurator(sslContext);
	}
}
