package com.sap.moc.utils;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import com.sap.moc.service.impl.EmailPasswordAuthenticator;

public class SendEmail {

	public static String send(String to, String fileName, byte[] attachmentByte) throws Exception {
		System.setProperty("mail.mime.splitlongparameters","false");
		Properties properties = new Properties();
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.host", ConstantReader.readByKey("EMAIL_HOST"));
		properties.setProperty("mail.smtp.ssl.enable", "true");
		properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.smtp.socketFactory.fallback", "true");
		properties.setProperty("mail.smtp.port", "465");
		properties.setProperty("mail.smtp.socketFactory.port", "465");
		Authenticator authenticator = null;
		authenticator = new EmailPasswordAuthenticator(ConstantReader.readByKey("EMAIL_NAME"),
				ConstantReader.readByKey("EMAIL_PASSWORD"));
		// debug
//		properties.setProperty("mail.debug", "true");
		try {
			Session session = Session.getDefaultInstance(properties, authenticator);
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(ConstantReader.readByKey("EMAIL_FROM")));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject
			message.setSubject(fileName, "UTF-8");
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent("详情见附件！","text/html;charset=UTF-8");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			// attachment
			BodyPart attachment = new MimeBodyPart();
			attachment = new MimeBodyPart();
			ByteArrayDataSource source = new ByteArrayDataSource(attachmentByte,
					"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			attachment.setDataHandler(new DataHandler(source));
			attachment.setFileName(MimeUtility.encodeText(fileName +".xlsx", "UTF-8", null));	
			multipart.addBodyPart(attachment);
			message.setContent(multipart);
			
			// send message
			Transport.send(message);
			return "Send email successfully!";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Failed to send email!";
		}
	}

}
