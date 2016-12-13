package com.sap.moc.utils;

import org.junit.Test;

public class TokenUtilTest {

	@Test
	public void testGetAccessToken() throws InterruptedException {
		String DevMode = ConstantReader.readSysParaByKey("DEV_FLAG");
		if (DevMode.equals("1")) {
			System.getProperties().setProperty("https.proxyHost", "proxy");
			System.getProperties().setProperty("https.proxyPort", "8080");
		}
		String accessToken = TokenUtil.getAccessToken();
		;
		System.out.println(accessToken);
		Thread.sleep(100000);
		accessToken = TokenUtil.getAccessToken();
		System.out.println(accessToken);
	}

}
