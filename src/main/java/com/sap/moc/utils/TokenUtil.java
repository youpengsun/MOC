package com.sap.moc.utils;

import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;

import net.sf.json.JSONObject;

public class TokenUtil {

	// current access token will be expired at
	private static long expires_at;
	// access token
	private static String access_token;
	// default expires time : 7200s
	private final static long expires_in = 7200000;
	// When less than 30s, update the access token
	private final static long expires_close = 30000;

	// get access token
	public static String getAccessToken() {
		long now = System.currentTimeMillis();

		// when the access token is initial, update access token
		if (access_token == null) {
			updateAccessToken();
		}

		else if ((expires_at - now) < expires_close) {
			updateAccessToken();
		}

		return access_token;
	}

	public static void updateAccessToken() {
		// use the Access token request URL to get access token

		String access_token_url = ConstantReader.readByKey("ACCESS_TOKEN_URL");
		access_token_url = access_token_url.replaceAll("&1", ConstantReader.readSysParaByKey("CORPID"));
		access_token_url = access_token_url.replaceAll("&2", ConstantReader.readSysParaByKey("CORPSECRET"));
		try {
			// to delete when upload to server
			String DevMode = ConstantReader.readSysParaByKey("DEV_FLAG");
			if (DevMode.equals("1")) {
				System.setProperty("https.proxyHost", "proxy.sha.sap.corp");
				System.setProperty("https.proxyPort", "8080");
			}

			URL url = new URL(access_token_url);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			InputStream inputStream = connection.getInputStream();
			String string = IOUtils.toString(inputStream);
			JSONObject object = JSONObject.fromObject(string);
			Object at = object.get("access_token");

			access_token = at.toString();
			System.out.println("access_token:" + access_token);
			// use current time + 7200s to update expires_at
			expires_at = System.currentTimeMillis() + expires_in;
			System.out.println("expires at " + expires_at);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
