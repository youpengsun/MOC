package com.sap.moc.utils;

import org.apache.commons.lang.math.NumberUtils;

public class TrimUtil {
	public static boolean trimUtil(String string) {
		if (string == null) {
			return false;
		} else if (string.trim().equals("")) {
			return false;
		} else {
			return true;
		}
	}

	public static int trimNumber(String code) {
		int result;
		String number = code.trim().replaceFirst("^0*", "");
		result = NumberUtils.toInt(number);
		return result;
	}
}
