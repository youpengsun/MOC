package com.sap.moc.vo;

public class QRCode {

	private String scanCode;
	private int vendorId;
	private int vendorLineNo;

	public int getVendorId() {
		return vendorId;
	}

	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}

	public int getVendorLineNo() {
		return vendorLineNo;
	}

	public void setVendorLineId(int vendorLineId) {
		this.vendorLineNo = vendorLineId;
	}

	public String getScanCode() {
		return scanCode;
	}

	public QRCode() {
	}

	private boolean isFormatValid;

	public boolean isValid() {

		return isFormatValid;
	}
	public QRCode(String scanCode) {
		this.scanCode = scanCode;
		isFormatValid = parseScanCode(this.scanCode);
	}

	private boolean parseScanCode(String scanCode) {
		boolean validFlag = false;

		this.scanCode = scanCode;

		if (scanCode.matches("\\d+-\\d+")) {
			validFlag = true;
			String[] split = scanCode.split("-");
			this.vendorId = Integer.parseInt(split[0]);
			this.vendorLineNo = Integer.parseInt(split[1]);

		}
		return validFlag;
	}


}