package com.sap.moc.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.MessageFormat;
import java.util.Locale;

import org.junit.Test;

import com.sap.moc.utils.ConstantReader;
import com.sap.moc.utils.ConstantUtil;
import com.sap.moc.utils.I18nUtil;
import com.sap.moc.vo.QRCode;

public class UtilityTest extends BaseJunit4Test {

	@Test
	public void testQRCode() {
		String qrCode = "10000";
		QRCode code1 = new QRCode(qrCode);

		assertFalse(code1.isValid());

		QRCode code2 = new QRCode("-1");
		assertFalse(code2.isValid());

		QRCode code3 = new QRCode("x");
		assertFalse(code3.isValid());

		QRCode code4 = new QRCode("1-x");
		assertFalse(code4.isValid());

		QRCode code5 = new QRCode("888@9");
		assertFalse(code5.isValid());

		QRCode code6 = new QRCode("b-5");
		assertFalse(code6.isValid());

		QRCode code7 = new QRCode("1000-9");
		assertTrue(code7.isValid());

		QRCode code8 = new QRCode("");
		assertFalse(code8.isValid());
	}

	@Test
	public void testEmunReader() {
		String key = ConstantUtil.consumeErrorType.INVALID_QRCODE.toString();
		String messageContent = I18nUtil.getKey("CONSUME_FAILED_CONTENT", Locale.CHINESE);

		String errorContent = I18nUtil.getKey(key, Locale.CHINESE);

		messageContent = MessageFormat.format(messageContent, errorContent);

		System.out.println(messageContent);
	}

	@Test
	public void testConstantReader() {
		String text = ConstantReader.readByKey("SAP_CONTACT");
		
		assertEquals("Fanny", text);
	}

}
