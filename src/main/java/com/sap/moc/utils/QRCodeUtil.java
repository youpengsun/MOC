package com.sap.moc.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sap.moc.entity.VendorLine;
import com.sap.moc.exception.MOCException;

public class QRCodeUtil {
	// this Util used to parse and generate QR code

	public static int[] parseScanInfo(String scanInfo) {

		int vendorId;
		int vendorLineId;

		String[] split = scanInfo.split("-");
		if (split[0].matches("[0-9]+") && split[1].matches("[0-9]+")) {
			vendorId = Integer.parseInt(split[0]);
			vendorLineId = Integer.parseInt(split[1]);
		} else {
			throw new MOCException(1001);
		}
		return new int[] { vendorId, vendorLineId };
	}

	public static String buildVenlineOpenId(VendorLine line) {
		String openId = String.valueOf(line.getVendor().getId()) + "-" + String.valueOf(line.getLineNO());
		return openId.trim();
	}

	private static final String IMAGEFORMAT = "png";

	private static BufferedImage encodeQrcode(String contents, BarcodeFormat format, int width, int height)
			throws Exception {
		/* hints is the additional configuration for QR Code */
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		// assign the character set which contents use
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		// assign the correction level for QR Code （L 7%、M 15%、Q 25%、H 30%）
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, format, width, height, hints);
		return toBufferedImage(bitMatrix);
	}

	private static BufferedImage toBufferedImage(BitMatrix matrix) throws Exception {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
			}
		}
		return image;
	}

	/**
	 * 
	 * @param image
	 * @param imgPath
	 * @param needCompress
	 * @throws Exception
	 */
	private static BufferedImage insertLogo(BufferedImage image, byte[] logoInByte) throws Exception {
		if (logoInByte.equals(null)) {
			return image;
		}
		/* read logo image */
		InputStream input = new ByteArrayInputStream(logoInByte);
		BufferedImage logo = ImageIO.read(input);
		/* read QR code image and construct Graphics */
		Graphics2D g = image.createGraphics();
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		/* draw logo image */
		g.drawImage(logo, imageWidth / 5 * 2, imageHeight / 5 * 2, imageWidth / 5, imageHeight / 5, null);
		/* draw a stroke */
		BasicStroke stroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		RoundRectangle2D.Float round = new RoundRectangle2D.Float(imageWidth / 5 * 2, imageHeight / 5 * 2,
				imageWidth / 5, imageHeight / 5, 15, 15);
		g.setColor(Color.white);
		g.setStroke(stroke);
		g.draw(round);
		g.dispose();
		image.flush();
		return image;
	}

	public static byte[] generateQrcode(String contents, int width, int height, byte[] logoInByte) throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		BufferedImage image = encodeQrcode(contents, BarcodeFormat.QR_CODE, width, height);
		if (logoInByte != null && logoInByte.length != 0) {
			image = insertLogo(image, logoInByte);
		}
		ImageIO.write(image, IMAGEFORMAT, output);
		return output.toByteArray();
	}

}
