package me.moreal.network;

import java.io.IOException;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;

public class SSLSocket {
	SSLSocketFactory ssf = null;
	Socket s = null;
	
	// http://www.java2s.com/Tutorial/Java/0490__Security/HttpsSocketClient.htm
	// https://www.pixelstech.net/article/1445603357-A-HTTPS-client-and-HTTPS-server-demo-in-Java
	public SSLSocket(String url, int port) {
		try {
			ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
			s = ssf.createSocket(url, port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
