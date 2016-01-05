package com.zeshanaslam.ayc.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HTTPSManager {

    public void runConnection(final String inputURL, final HTTPSCallBack callBack) {
        // Add check for connection and see if user is on data or wifi

        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(inputURL);

                    TrustManager trm = new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {

                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    };

                    SSLContext sc = SSLContext.getInstance("SSL");
                    sc.init(null, new TrustManager[]{trm}, null);

                    HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                    con.setSSLSocketFactory(sc.getSocketFactory());
                    con.setHostnameVerifier(new NullHostNameVerifier());

                    InputStream ins = con.getInputStream();
                    InputStreamReader isr = new InputStreamReader(ins);

                    BufferedReader in = new BufferedReader(isr);
                    String inputLine;

                    StringBuilder stringBuilder = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        stringBuilder.append(inputLine);
                    }

                    in.close();

                    callBack.onRequestComplete(stringBuilder.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    callBack.onRequestFailed();
                }
            }
        }.start();
    }

    public void runConnectionDownload(final String inputURL, final HTTPSCallBack callBack) {
        // Add check for connection and see if user is on data or wifi

        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(inputURL);

                    TrustManager trm = new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {

                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    };

                    SSLContext sc = SSLContext.getInstance("SSL");
                    sc.init(null, new TrustManager[]{trm}, null);

                    HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                    con.setSSLSocketFactory(sc.getSocketFactory());
                    con.setHostnameVerifier(new NullHostNameVerifier());

                    InputStream ins = con.getInputStream();

                    callBack.onRequestComplete(ins);
                } catch (Exception e) {
                    e.printStackTrace();
                    callBack.onRequestFailed();
                }
            }
        }.start();
    }
}
