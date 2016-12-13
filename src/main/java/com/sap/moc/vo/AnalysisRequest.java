package com.sap.moc.vo;

import java.io.Serializable;

public class AnalysisRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -506908704101286818L;

	private QueryTime begda;
	private QueryTime endda;

//	private Map<Integer, Integer> consumeCount;

	public QueryTime getBegda() {
		return begda;
	}

	public void setBegda(QueryTime begda) {
		this.begda = begda;
	}

	public QueryTime getEndda() {
		return endda;
	}

	public void setEndda(QueryTime endda) {
		this.endda = endda;
	}

//	public Map<Integer, Integer> getConsumeCount() {
//		return consumeCount;
//	}
//
//	public void setConsumeCount(Map<Integer, Integer> consumeCount) {
//		this.consumeCount = consumeCount;
//	}

}
