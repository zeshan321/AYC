package com.zeshanaslam.aycserver.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

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
		sslContext.init(keyManager.getKeyManagers(), new X509TrustManager[]{new X509TrustManager(){ 
			public void checkClientTrusted(X509Certificate[] chain, 
					String authType) throws CertificateException {} 
			public void checkServerTrusted(X509Certificate[] chain, 
					String authType) throws CertificateException {} 
			public X509Certificate[] getAcceptedIssuers() { 
				return new X509Certificate[0]; 
			}}}, new SecureRandom());

		return new HttpsConfigurator(sslContext);
	}
}
