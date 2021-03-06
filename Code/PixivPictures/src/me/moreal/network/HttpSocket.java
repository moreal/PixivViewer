package me.moreal.network;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;

public class HttpSocket {
	private Socket sock = null;
	
	private InputStream iStream = null;
	private OutputStream oStream = null;
	
	private String url = null;
	private String path = null;
	private String getParam = null;
	private String postParam = null;

	public HttpSocket(String url, String path) {
			this.url = url;
			this.path = path.equals("")?"/":path;
			
			try {
				this.sock = new Socket(url, 80);
				this.iStream = sock.getInputStream();
				this.oStream = sock.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public void setGetParameter(String param) {
		if (param.equals("")) this.getParam = "";
		else this.getParam = "?"+param;
		System.out.println("[DEBUG] set GET Parameter : " + getParam);
	}
	
	public void setPostParameter(String param) {
		this.postParam = param;
	}
	
	public String getGetParameter() {
		return getParam;
	}
	
	public String getPostParameter() {
		return postParam;
	}
	
	public boolean sendPost() {
		try {
			String s = String.format("POST %s HTTP/1.1\r\n", path + "?" + getParam);
			s += "Host: " + url + "\r\n";
			oStream.write(s.getBytes(), 0, s.length());
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public boolean sendGet() {
		try {
			String s = String.format("GET %s HTTP/1.1\r\n", path + getParam);
			System.out.println("[DEBUG] " + s);
			s += "Host: " + url + "\r\n";
			s += "Cookie: PHPSESSID=2dac37e78f9a326cda748a79401af27f\r\n";
			//s += "Connection: keep-alive\r\n";
			//s += "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64)\r\n";// AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36\r\n";
			//s += "Accept: text/html\r\n";
			//s += "Accept-Encoding: gzip, deflate\r\n";
			//s += "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7\r\n";
			s+="\r\n";
			System.out.println(s);
			oStream.write(s.getBytes(), 0, s.length());
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public String readAll() {
		try {
			byte[] b = new byte[1024];
			System.out.println("Read Start");
			iStream.read(b);
			System.out.println("Read End");
			return new String(b);
		} catch (IOException e) {
			return null;
		}
	}
}
