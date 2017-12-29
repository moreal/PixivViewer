package me.moreal.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import me.moreal.network.HttpSocket;
import me.moreal.util.Util;

public class PixivParser extends Thread {

	private int illust_id = 0;
	private String url = null;

	private HttpSocket sock = null;
	private String page = null;

	private BufferedReader br = null;
	private HttpsURLConnection conn = null;

	public PixivParser(String url, int startnum) {
		this.illust_id = startnum;
		this.url = url;
		// sock = new HttpSocket(this.url, "/member_illust.php"); //
		// ?mode=medium&illust_id=61838494
	}

	public PixivParser(String url) {
		this(url, 0);
	}

	public void run() {
		// sock.setGetParameter("mode=medium&illust_id=" + illust_id);
		// System.out.println(sock.sendGet());

		// page = sock.readAll();
		while (true) {
			try {
				System.out.println("https://www.pixiv.net/member_illust.php?mode=medium&illust_id=" + illust_id);
				conn = (HttpsURLConnection) new URL(
						"https://www.pixiv.net/member_illust.php?mode=medium&illust_id=" + illust_id).openConnection();
				conn.setRequestProperty("Cookie", "login_ever=1");
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				page = "";

				while (br.ready())
					page += br.readLine();

				System.out.println(getImageLink() + " " + illust_id);
				
				Util.downloadImage(getImageLink(), "D:/TEST/","Hello.jpg");
			} catch (FileNotFoundException e) {
				// System.out.println("Null URL");
				//continue;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				++illust_id;
			}
		}
	}

	private int getGoodPoint() {
		return 0;
	}

	private String getAuthor() {
		Pattern p = Pattern.compile(
				"<img[^>]*alt=[\\\"']?([^>\\\"']+)[\\\"']width=[\\\"']?([^>\\\"']+)[\\\"']?height=[\\\"']?([^>\\\"']+)[\\\"']?data-src=[\\\"']?([^>\\\"']+)[\\\"']?[^>]*>");

		// Study : http://myeonguni.tistory.com/1555
		// ? 앞의 부분식의 0 혹은 1개;
		// [] 안에다가 문자열 삽입
		// [^] 안에 있는 문자열 제외
		// * 0개 이상 탐욕적으로

		// <img alt="幼女先輩" width="739" height="1100"
		// data-src="https://i.pximg.net/img-original/img/2017/03/10/17/51/31/61838494_p0.png"
		// class="original-image"
		// src="https://i.pximg.net/img-original/img/2017/03/10/17/51/31/61838494_p0.png">

		Matcher m = p.matcher(page);
		if (m.find()) {
			System.out.println("[DEBUG] There is one");
			return m.group(1);
		}
		return null;
	}

	private String getImageLink() {
		int start = page.indexOf("data-title=\"registerImage\"><img src=\"");
		int end = page.indexOf("\" alt=", start);

		if (start == -1)
			return null;
		//System.out.println(page.substring(start + 37, end));
		return page.substring(start + 37, end).replaceAll("_master[0-9]+", "").replaceAll("c/[a-zA-Z0-9]+/img-master",
				"img-original");
	}

	private String getTitle() {
		int start = page.indexOf("<title>");
		int end = page.indexOf("</title>", start);

		return page.substring(start + 7, end);
	}
}