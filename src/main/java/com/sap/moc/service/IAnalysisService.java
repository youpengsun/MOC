package com.sap.moc.service;

import java.text.ParseException;
import java.util.List;

import com.sap.moc.vo.QueryTime;
import com.sap.moc.vo.VendorConsume;

public interface IAnalysisService {

	public List<VendorConsume> getAnalysisResource(QueryTime begmo,QueryTime endmo) throws ParseException;

}
