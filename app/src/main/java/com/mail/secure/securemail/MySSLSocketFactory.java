package com.mail.secure.securemail;

/**
 * Created by zeez on 10/27/2017.
 */


import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.net.SocketFactory;
import javax.net.ssl.*;


import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * هذي الكلاسات عشان يحلون مشكله الشهادة الي نتسخدمها في الاتصال
 */
 class MyTrustManager implements X509TrustManager {

    public void checkClientTrusted(X509Certificate[] cert, String authType) {
        // everything is trusted
    }

    public void checkServerTrusted(X509Certificate[] cert, String authType) {
        // everything is trusted
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}


/**
 * MySSLSocketFactory
 */
public class MySSLSocketFactory extends SSLSocketFactory {
    private SSLSocketFactory factory;

    public MySSLSocketFactory() {
        try {
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null,
                    new TrustManager[] { new MyTrustManager()},
                    null);
            factory = (SSLSocketFactory)sslcontext.getSocketFactory();
        } catch(Exception ex) {
            // ignore
        }
    }

    public static SocketFactory getDefault() {
        return new MySSLSocketFactory();
    }

    public Socket createSocket() throws IOException {
        return factory.createSocket();
    }

    public Socket createSocket(Socket socket, String s, int i, boolean flag)
            throws IOException {
        return factory.createSocket(socket, s, i, flag);
    }

    public Socket createSocket(InetAddress inaddr, int i,
                               InetAddress inaddr1, int j) throws IOException {
        return factory.createSocket(inaddr, i, inaddr1, j);
    }

    public Socket createSocket(InetAddress inaddr, int i)
            throws IOException {
        return factory.createSocket(inaddr, i);
    }

    public Socket createSocket(String s, int i, InetAddress inaddr, int j)
            throws IOException {
        return factory.createSocket(s, i, inaddr, j);
    }

    public Socket createSocket(String s, int i) throws IOException {
        return factory.createSocket(s, i);
    }

    public String[] getDefaultCipherSuites() {
        return factory.getDefaultCipherSuites();
    }

    public String[] getSupportedCipherSuites() {
        return factory.getSupportedCipherSuites();
    }
}