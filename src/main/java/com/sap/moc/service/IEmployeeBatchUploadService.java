package com.sap.moc.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sap.moc.entity.Employee;
import com.sap.moc.vo.EmployeeUpload;

/**
 * @author I074115
 *
 */
public interface IEmployeeBatchUploadService {
  /*
  *Map
  *"insert":List of inserted employees
  *"update":List of updated employees 
  */
 // public List<EmployeeUpload> handleExcelUplaod(HttpServletRequest request,StringBuilder msg);
  public List<EmployeeUpload> handleExcelUpload(MultipartFile eeexcel,StringBuilder msg);
	
  //Final Upload
  public boolean doEmployeeDataUpload(List<Employee> list);
}
