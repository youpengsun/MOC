package com.sap.moc.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConstantReader {

	public static String readByKey(String key) {
		return readConstantByKey(key, "/properties/constants.properties");
	}

	public static String readTimeByKey(String key) {
		return readConstantByKey(key, "/properties/time.properties");
	}

	public static String readSysParaByKey(String key) {
		return readConstantByKey(key, "/properties/system.properties");
	}

	private static String readConstantByKey(String key, String constantFile)  {
		String value = null;
		try {
			Properties props = new Properties();
			// Load the properties file
			InputStream inputStream = ConstantReader.class.getResourceAsStream(constantFile);
			
			if (inputStream != null) {
				props.load(inputStream);
				value = props.getProperty(key);
				inputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}
}
