package com.sap.moc.vo;


import java.util.Map;

public class Ordering {
	
  public static enum sort{ASCENDING, DESCENDING};
  
  public Map<String, sort> orderCriteria;

/**
 * @return the sortCriteria
 */
public Map<String, sort> getOrderCriteria() {
	return orderCriteria;
}

/**
 * @param sortCriteria the sortCriteria to set
 */
public void setSortCriteria(Map<String, sort> orderCriteria) {
	this.orderCriteria = orderCriteria;
}
  
  
}
