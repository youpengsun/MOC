package com.sap.moc.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sap.moc.entity.User;
import com.sap.moc.service.IUserService;
import com.sap.moc.utils.ConstantReader;
import com.sap.moc.utils.I18nUtil;
import com.sap.moc.utils.UserLoginUtil;
import com.sap.moc.vo.ResetPassword;

@Controller
@SuppressWarnings("unchecked")
public class UserController {

	@Autowired
	private IUserService userService;

	@RequestMapping("/user/login")
	@ResponseBody
	public Map<String, String> UserLogin(@RequestBody User loginUser, HttpServletRequest request) {

		@SuppressWarnings("rawtypes")
		Map map = new HashMap<String, String>();
		String loginMessage;

		if (loginUser.getUsername() == null || loginUser.getPassword() == null) {

			loginMessage = I18nUtil.getKey("user.login.nameandpass.required");
			map.put("loginMessage", loginMessage);
			return map;
		} else {

			User user = userService.getUser(loginUser.getUsername());

			if (userService.validateUser(user, loginUser.getPassword())) {
				request.getSession().setAttribute("username", loginUser.getUsername());
				request.getSession().setAttribute("role", user.getUserGroup());

				map.put("loginStatus", UserLoginUtil.LOGIN_STATUS_SUCCESS);

				map.put("loginRole", user.getUserGroup());

				return map;
			} else {
				loginMessage = I18nUtil.getKey("user.login.nameandpass.invalid");
				map.put("loginMessage", loginMessage);
				return map;
			}
		}
	}

	@RequestMapping("/user/checkLogined")
	@ResponseBody
	public Map<String, String> checkLogined(HttpServletRequest request) {
		@SuppressWarnings("rawtypes")
		Map map = new HashMap<String, String>();

		String username;
		String role;

		username = (String) request.getSession().getAttribute("username");
		role = (String) request.getSession().getAttribute("role");

		if (username != null && role != null) {
			map.put("checkStatus", UserLoginUtil.LOGIN_STATUS_LOGINED);
			map.put("loginRole", role);
		} else {
			map.put("checkStatus", UserLoginUtil.LOGIN_STATUS_NOT_LOGINED);
		}

		return map;
	}

	@RequestMapping("/user/logout")
	@ResponseBody
	public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String loginPage = ConstantReader.readByKey("USER_LOGIN_PAGE").trim();
		request.getSession().invalidate();

		response.sendRedirect(request.getContextPath() + loginPage);
	}

	@RequestMapping("/user/getusername")
	@ResponseBody
	public Map<String, String> getUsername(HttpServletRequest request, HttpServletResponse response) {

		@SuppressWarnings("rawtypes")
		Map map = new HashMap<String, String>();

		String username = (String) request.getSession().getAttribute("username");
		String role = (String) request.getSession().getAttribute("role");
		map.put("username", username);

		if (UserLoginUtil.LOGIN_ROLE_ADMIN.equalsIgnoreCase(role)) {
			map.put("role", UserLoginUtil.LOGIN_ROLE_DESC_ADMIN);
		} else if (UserLoginUtil.LOGIN_ROLE_COUPON_ADMIN.equalsIgnoreCase(role)) {
			map.put("role", UserLoginUtil.LOGIN_ROLE_DESC_COUPON_ADMIN);
		}
		return map;
	}

	@RequestMapping("/user/resetpassword")
	@ResponseBody
	public Map<String, String> resetPasword(@RequestBody ResetPassword reset, HttpServletRequest request) {

		Map<String, String> map = new HashMap<String, String>();

		String username = (String) request.getSession().getAttribute("username");
		if (username == null) {
			map.put("result", "false");
			map.put("error", "login");
			return map;
		} else {

			return userService.resetPassword(username, reset);

		}

	}

}
