package com.sap.moc.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;

public class HttpUtil {
	private static final Logger logger = Logger.getLogger(HttpUtil.class);
	private static HttpsURLConnection connection;

	public static String doGet(String requestURL) {
		BufferedReader in = null;
		try {
			String DevMode = ConstantReader.readSysParaByKey("DEV_FLAG");
			if (DevMode.equals("1")) {
				System.setProperty("https.proxyHost", "proxy.sha.sap.corp");
				System.setProperty("https.proxyPort", "8080");
			}
			URL url = new URL(requestURL);
			connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			// true indicates the server returns response
			connection.setDoInput(true);

			InputStream inputStream = connection.getInputStream();
			in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String line;
			StringBuffer result = new StringBuffer();
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
			return result.toString();
		} catch (Exception ex) {
			logger.error("DoGet URL:" + requestURL + ",Exception:" + ex.getMessage());
			return null;
		} 
//		finally {
//			if (in != null) {
//				try {
//					in.close();
//				} catch (Exception ex) {
//					logger.error("DoGet URL:" + requestURL + ", Close input stream exception:" + ex.getMessage());
//				}
//			}
//			if (connection != null) {
//				connection.disconnect();
//			}
//		}
	}

	public static String doPost(String requestURL, Map<String, String> params, String data) {
		BufferedReader in = null;
		StringBuffer output = new StringBuffer();
		try {
			String DevMode = ConstantReader.readSysParaByKey("DEV_FLAG");
			if (DevMode.equals("1")) {
				System.setProperty("https.proxyHost", "proxy.sha.sap.corp");
				System.setProperty("https.proxyPort", "8080");
			}
			URL url = new URL(requestURL);
			connection = (HttpsURLConnection) url.openConnection();
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Charset", "UTF-8");
			connection.setRequestProperty("Connection", "keep-alive");
//			connection.setRequestProperty("Content-Length", String.valueOf(data.length()));
//			connection.setConnectTimeout(50000);
			// true indicates the server returns response
			connection.setDoInput(true);
			// true indicates POST request
			connection.setDoOutput(true);
			if (params != null && params.size() > 0) {
				Iterator<String> paramIterator = params.keySet().iterator();
				while (paramIterator.hasNext()) {
					String key = paramIterator.next();
					String value = params.get(key);
					connection.setRequestProperty(key, value);
				}

			}
			// send POST data
			if (data != null) {
				output.append(data);
			}

			
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
			writer.write(output.toString());
			writer.flush();
//			connection.getOutputStream().close();
			InputStream inputStream = connection.getInputStream();

			in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String line;
			StringBuffer result = new StringBuffer();
			while ((line = in.readLine()) != null) {
				result.append(line);
			}

			return result.toString();
			
		} catch (Exception ex) {
			logger.error("DoPost URL:" + requestURL + ", Exception:" + ex.getMessage());
			return null;
		}
//		finally {
//			if (in != null) {
//				try {
//					in.close();
//				} catch (Exception ex) {
//					logger.error("DoPost URL:" + requestURL + ", Close input stream exception:" + ex.getMessage());
//				}
//			}
//			if (connection != null) {
//				connection.disconnect();
//				
//			}
//		}

	}
}