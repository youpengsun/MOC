package com.sap.moc.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

@SuppressWarnings("rawtypes")
public class CreateCSVUtil {
	
	private final static String DEFAULT_DELIMITER = ",";
	private final static String DEFAULT_END = "\r\n";
	private final static String SUFFIX = ".csv";
	
	public static File createCSVFile(ArrayList<HashMap<String,String>> data, LinkedHashMap<String, String> header, String fileName, String outputPath) throws IOException {
			
		File csvFile = null;
		BufferedWriter outputStream = null;
	
		csvFile = File.createTempFile(fileName, SUFFIX, new File(outputPath));
		outputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "UTF-8"));
		//write CSV file header
		for (Iterator propertyIterator = header.entrySet().iterator(); propertyIterator.hasNext();) {
			
			java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
			outputStream.write((String)propertyEntry.getValue());
			if (propertyIterator.hasNext()) {  
				outputStream.write(DEFAULT_DELIMITER);  
	        }  
		}			
		outputStream.write(DEFAULT_END);
		//write CSV file contents
		for (Iterator iterator = data.iterator(); iterator.hasNext();){
			HashMap row = (HashMap) iterator.next();
			//write a row
			for(Iterator propertyIterator = header.entrySet().iterator(); propertyIterator.hasNext();){
				java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next(); 
				String temp = (String) row.get((String)propertyEntry.getKey()) != null ? (String) row.get((String)propertyEntry.getKey()) : "";
				outputStream.write(temp);
				if (propertyIterator.hasNext()) {  
					outputStream.write(DEFAULT_DELIMITER); 
                }  	
			}
			outputStream.write(DEFAULT_END);			
		}		
		outputStream.close();
		return csvFile;
	}
	
	public static String createCSVFile(ArrayList<HashMap<String,String>> data, LinkedHashMap<String, String> header) {
		
		StringBuffer buffer = new StringBuffer();
		//write CSV file header
		for (Iterator propertyIterator = header.entrySet().iterator(); propertyIterator.hasNext();) {
			
			java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
			buffer.append((String)propertyEntry.getValue());
			if (propertyIterator.hasNext()) {  
	            buffer.append(DEFAULT_DELIMITER);  
	        }  
		}
		buffer.append(DEFAULT_END);
		//write CSV file contents
		for (Iterator iterator = data.iterator(); iterator.hasNext();){
			HashMap row = (HashMap) iterator.next();
			//write a row
			for(Iterator propertyIterator = header.entrySet().iterator(); propertyIterator.hasNext();){
				java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next(); 
				String temp = (String) row.get((String)propertyEntry.getKey()) != null ? (String) row.get((String)propertyEntry.getKey()) : "";
				buffer.append(temp);
				if (propertyIterator.hasNext()) {  
	                buffer.append(DEFAULT_DELIMITER); 
                }  	
			}
			buffer.append(DEFAULT_END);			
		}
		
		return buffer.toString();
	}
	


}
