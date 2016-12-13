package com.sap.moc.utils;

public class ConstantUtil {

	// Consume record Category type: Lunch and Dinner
	public static final String CONSUME_CATEGORY_LUNCH = "LH";
	public static final String CONSUME_CATEGORY_DINNER = "DR";
	public static final String CONSUME_CATEGORY_LUNCH_DESCRIPTION = "Lunch";
	public static final String CONSUME_CATEGORY_DINNER_DESCRIPTION = "Dinner";

	// Consume status type: 01-Successful; 00-Failed
	public static final String CONSUME_STATUS_SUCCESS = "01";
	public static final String CONSUME_STATUS_FAILED = "00";
	public static final String CONSUME_STATUS_SUCCESS_DESCRIPTION = "Successful";
	public static final String CONSUME_STATUS_FAILED_DESCRIPTION = "Failed";

	public enum consumeErrorType {
		INVALID_EE, INVALID_VENDOR, ALREADY_CONSUMED, NOT_AVAILABLE_TIME, OTHER, INVALID_QRCODE, CREATE_FAILED, INVALID_DATE, COUPON_CONSUMED, INACTIVE_EE, INACTIVE_VENDOR, INVALID_VENDORLINE
	};

	public enum vendorStatus {
		ACTIVE, INACTIVE
	};

	
	// Contact status indicator: 01-Active; 00-Terminated Contract
	public static final String CONTRACT_STATUS_ACTIVE = "01";
	public static final String CONTRACT_STATUS_INACTIVE = "00";
	public static final String CONTRACT_STATUS_ACTIVE_DESCRIPTION = "Active";
	public static final String CONTRACT_STATUS_INACTIVE_DESCRIPTION = "Terminated";

	// Vendor period indicator: M0-Monthly; W1-Bi-weekly ; W0: Weekly; D0: Daily
	public static final String VENDOR_PERIOD_MONTHLY = "M0";
	public static final String VENDOR_PERIOD_BIWEEKLY = "W1";
	public static final String VENDOR_PERIOD_WEEKLY = "W0";
	public static final String VENDOR_PERIOD_DAILY = "D0";
	public static final String VENDOR_PERIOD_MONTHLY_DESCRIPTION = "Monthly";
	public static final String VENDOR_PERIOD_BIWEEKLY_DESCRIPTION = "Bi-Weekly";
	public static final String VENDOR_PERIOD_WEEKLY_DESCRIPTION = "Weekly";
	public static final String VENDOR_PERIOD_DAILY_DESCRIPTION = "Daily";

	// Vendor type indicator: 00-Internal; 01-External
	public static final String VENDOR_TYPE_INTERNAL = "IN";
	public static final String VENDOR_TYPE_EXTERNAL = "EX";
	public static final String VENDOR_TYPE_INTERNAL_DESCRIPTION = "Internal";
	public static final String VENDOR_TYPE_EXTERNAL_DESCRIPTION = "External";

	// Employee status: 00-Inactive; 01-Active
	public static final String EMPLOYEE_ACTIVE = "01";
	public static final String EMPLOYEE_INACTIVE = "00";

	public static enum syncStatus {
		TOADD, TOUPDATE, TODELETE, SYNCED, TOACTIVATE, TODEACTIVATE, UNKNOWN
	}
	
	public static final String CALL_SERVICE_FAILED = "-99999";
	public static final String CALL_SERVICE_FAILED_DESCRIPTION = "Failed to call Wechat service";	
	public static final String NET_EXCEPTION = "-99998";
	
	// The code of wechat service if successful
	public static final String SUCCESS = "0";

	public static final String DATE_FORMAT_DEFAULT = "dd/MM/yyyy";

	public static final String COUPON_VALIDATE_STATUS_SUCCESS = "VALIDATE_SUCCESS";
	public static final String COUPON_VALIDATE_STATUS_WARNING = "VALIDATE_WARNING";
	public static final String COUPON_VALIDATE_STATUS_FAILED = "VALIDATE_FAILED";

	public static final String COUPON_SAVE_STATUS_SUCCESS = "SAVE_SUCCESS";
	public static final String COUPON_SAVE_STATUS_FAILED = "SAVE_FAILED";

	// Coupon registration type

	public static final String COUPON_TYPE_SELF_USE = "OW";
	public static final String COUPON_TYPE_FOR_OTHERS = "OT";
	public static final String COUPON_TYPE_FOR_BOTH = "BT";
	public static final String COUPON_TYPE_SELF_USE_TEXT = "For Self-Use";
	public static final String COUPON_TYPE_FOR_OTHERS_TEXT = "For Others' Use";

	// Coupon registration inquiry: misuse analysis and check
	public static final String COUPON_MISUESE_TYPE_SCANFORLUNCH = "SL";
	public static final String COUPON_MISUESE_TYPE_SCANFORLUNCH_TEXT = "Employee scanned for lunch at vendor: ";

}