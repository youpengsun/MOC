package com.sap.moc.utils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConstantReaderTest {

	@Before
	public void setUp() throws Exception {
		System.out.println("--SetUp");
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("--tearDown");
	}

	@Test
	public void testReadByKey() {
		String res = ConstantReader.readSysParaByKey("corpId");
		System.out.println(res);
		assertEquals("wxb7f1db8fd6aa9d68", res);

	}

	@Test
	public void testTrimNumber() {
		int value = TrimUtil.trimNumber("00123123");
		System.out.println(value);
		value = TrimUtil.trimNumber("  0123123");
		System.out.println(value);
		value = TrimUtil.trimNumber("012312300");
		System.out.println(value);
	}

}
