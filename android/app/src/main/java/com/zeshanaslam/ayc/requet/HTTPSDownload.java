package com.zeshanaslam.ayc.requet;

import com.zeshanaslam.ayc.utils.NullHostNameVerifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HTTPSDownload {

    public void runConnectionDownload(final String inputURL, final File file, final DownloadCallBack callBack) {
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

                    InputStream inputStream = con.getInputStream();
                    OutputStream outputStream = new FileOutputStream(file);

                    byte data[] = new byte[1024];

                    long total = 0;
                    int count;
                    int fileLength = con.getContentLength();

                    while ((count = inputStream.read(data)) != -1) {
                        total += count;

                        int percent = (int) ((total * 100) / fileLength);
                        callBack.onProgress(percent);

                        if (percent == 100) {
                            callBack.onRequestComplete();
                        }

                        outputStream.write(data, 0, count);
                    }

                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    callBack.onRequestFailed();
                }
            }
        }.start();
    }
}
