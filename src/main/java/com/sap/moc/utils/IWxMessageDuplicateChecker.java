package com.sap.moc.utils;

public interface IWxMessageDuplicateChecker {
	/*
	 * @param messageId messageId需要根据上面讲的方式构造
	 * 
	 * @return 如果是重复消息，返回true，否则返回false
	 */
	public boolean isDuplicate(String messageId);
}
