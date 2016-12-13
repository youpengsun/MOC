package com.sap.moc.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sap.moc.entity.Contract;
import com.sap.moc.entity.District;
import com.sap.moc.entity.Vendor;
import com.sap.moc.entity.VendorLine;
import com.sap.moc.exception.AdminException;
import com.sap.moc.exception.WeChatException;
import com.sap.moc.service.IVendorService;
import com.sap.moc.utils.QRCodeUtil;
import com.sap.moc.vo.Pager;
import com.sap.moc.vo.QRCode;
import com.sap.moc.vo.VendorWithContract;

@Controller
public class VendorController {

	@Autowired
	private IVendorService vendorService;

	@RequestMapping("/adminctrl/vendor/list")
	public @ResponseBody Map<String, List<Vendor>> getVendorList() {
		Map<String, List<Vendor>> map = new HashMap<String, List<Vendor>>();
		List<Vendor> list = vendorService.getVendorsWithStatus();
		map.put("vendors", list);
		return map;
	}

	@RequestMapping("/employeeuser/vendor/activelist")
	public @ResponseBody Map<String, List<Vendor>> getActiveVendorList() {
		Map<String, List<Vendor>> map = new HashMap<String, List<Vendor>>();
		List<Vendor> vendors = vendorService.getActiveVendors();
		Collections.sort(vendors,new Comparator<Vendor>(){  
            public int compare(Vendor v1, Vendor v2) {  
                return v1.getBusinessDistrict().getId().compareTo(v2.getBusinessDistrict().getId());  
            }  
        });  
		map.put("vendors", vendors);
		return map;
	}

	@RequestMapping("/employeeuser/vendor/page")
	public @ResponseBody Map<String, Pager<Vendor>> getActiveVendorPage(@RequestBody Pager<Vendor> pager) {
		Map<String, Pager<Vendor>> map = new HashMap<String, Pager<Vendor>>();
		map.put("pager", vendorService.getActiveVendorPager(pager));
		return map;

	}

	@RequestMapping("/adminctrl/vendor/save")
	public @ResponseBody Map<String, String> saveVendorContract(@RequestBody VendorWithContract vendorWithContract) {
		Map<String, String> map = new HashMap<String, String>();
		map = vendorService.saveVendorWithContract(vendorWithContract);
		return map;
		
	}	


	@RequestMapping("/adminctrl/vendor/edit")
	public @ResponseBody Map<String, String> editVendor(@RequestBody VendorWithContract vendorWithContract) {
		Map<String, String> map = new HashMap<String, String>();
		map = vendorService.updateVendorWithContract(vendorWithContract);
		return map;
	}

	// @Vendorline--------------------------------------------------------

	@RequestMapping("/adminctrl/vendorLine/{id}/list")
	public @ResponseBody Map<String, List<VendorLine>> getVendorLineList(@PathVariable("id") Integer id) {
		Map<String, List<VendorLine>> map = new HashMap<String, List<VendorLine>>();
		List<VendorLine> vendorLines = vendorService.getVendorLine(id);
		map.put("vendorLines", vendorLines);
		return map;
	}

	@RequestMapping("/adminctrl/vendorLine/save")
	public @ResponseBody Map<String, Object> saveVendorLine(@RequestBody VendorLine vendorLine) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean status = vendorService.saveVendorLineWOLineNo(vendorLine);
		if (!status) {
			map.put("status", false);
		} else {
			map.put("status", true);
			map.put("id", vendorLine.getId());
			map.put("line", vendorLine.getLineNO());
		}
		return map;
	}

	@RequestMapping("/adminctrl/vendorLines/save")
	public @ResponseBody Map<String, Object> saveVendorLines(@RequestBody List<VendorLine> vendorLines) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean returnStatus = true;
		for (VendorLine line : vendorLines) {
			boolean status = vendorService.saveVendorLine(line);
			if (!status) {
				returnStatus = false;
			}
		}
		map.put("status", returnStatus);
		return map;
	}

	@RequestMapping("/adminctrl/vendorLine/edit")
	public @ResponseBody Map<String, Object> editVendorLine(@RequestBody VendorLine vendorLine) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean status = vendorService.updateVendorLine(vendorLine);
		if (!status) {
			map.put("status", false);
		} else {
			map.put("status", true);
		}
		return map;
	}

	@RequestMapping("/adminctrl/vendor/del/{id}")
	public @ResponseBody Map<String, Boolean> del(@PathVariable("id") Integer id) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		Vendor ve = new Vendor();
		ve.setId(id);
		boolean status = vendorService.deleteVendor(ve);
		if (!status) {
			map.put("status", false);
		} else {
			map.put("status", true);
		}
		return map;
	}

	@RequestMapping("/adminctrl/vendor/logo")
	public void vendorLogo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("logo") MultipartFile logo, @RequestParam("id") String vendorId) {
		if (logo.getSize() > 0 && logo.getContentType().equals("image/png")) {
			try {
				//Logo under folder "vendorlogo" under webapps
				String path = request.getServletContext().getRealPath("").replace("moc", "vendorlogo");	
				String filename = vendorId + ".png";
				File file = new File(path, filename);
				logo.transferTo(file);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@RequestMapping("/adminctrl/vendorLine/del/{id}")
	public @ResponseBody Map<String, Boolean> delVendorLine(@PathVariable("id") Integer id) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		VendorLine ve = new VendorLine();
		ve.setId(id);
		boolean status = vendorService.deleteVendorLine(ve);
		if (!status) {
			map.put("status", false);
		} else {
			map.put("status", true);
		}
		return map;
	}

	@RequestMapping("/adminctrl/vendorline/qrcode")
	public void generateQrcode(@RequestParam("image") MultipartFile image, @RequestParam("code") String code,
			HttpServletResponse response) throws Exception {

		QRCode qrCode = new QRCode(code);
		if (qrCode.isValid()) {
			VendorLine vendorLine = vendorService.getVendorLinebyQRCode(qrCode);
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + URLEncoder.encode(vendorLine.getName() + "二维码.png", "UTF-8"));
			response.setContentType("application/x-png");
			OutputStream os = new BufferedOutputStream(response.getOutputStream());
			os.write(QRCodeUtil.generateQrcode(code, 300, 300, image.getBytes()));
			os.flush();
			os.close();
		} else {
			return;
		}
	}

	// @ for contract---------------------------------------------

	@RequestMapping("/adminctrl/contract/{id}")
	public @ResponseBody Map<String, Contract> getContract(@PathVariable("id") Integer id) {
		Map<String, Contract> map = new HashMap<String, Contract>();
		map.put("contract", vendorService.getContract(id));
		return map;
	}

	@RequestMapping("/adminctrl/contract/save")
	public @ResponseBody Map<String, Object> saveContract(@RequestBody Contract contract) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean status = vendorService.saveContract(contract);
		if (!status) {
			map.put("status", false);
		} else {
			map.put("status", true);
		}
		return map;
	}

	@RequestMapping("/adminctrl/contract/edit")
	public @ResponseBody Map<String, Object> editContract(@RequestBody Contract contract) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean status = vendorService.updateContract(contract);
		if (!status) {
			map.put("status", false);
		} else {
			map.put("status", true);
		}
		return map;
	}

	@RequestMapping("/adminctrl/contract/del/{id}")
	public @ResponseBody Map<String, Boolean> delContract(@PathVariable("id") Integer id) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		Contract contract = new Contract();
		contract.setId(id);
		boolean status = vendorService.deleteContract(contract);
		if (!status) {
			map.put("status", false);
		} else {
			map.put("status", true);
		}
		return map;
	}

	// @District-----------------------------------------------------------
	@RequestMapping("/adminctrl/district/list")
	public @ResponseBody Map<String, List<District>> getDistrictList() {
		Map<String, List<District>> map = new HashMap<String, List<District>>();
		map.put("districts", vendorService.getAllDistrict());
		return map;
	}

	@ExceptionHandler(Exception.class)
	public String handleException(Exception exception) {
		// handle exception
		exception.printStackTrace();
		return "error";
	}

	@ExceptionHandler(WeChatException.class)
	public String handleAdminException(AdminException exception) {
		// handle exception
		return "error";
	}

}
